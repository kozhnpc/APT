package work.kozh.gs;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class GenerateGSProcessor extends AbstractProcessor {

    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类


    //************************* 以下是常用的模板 *********************************//

    /**
     * 在 init() 可以初始化拿到一些实用的工具类
     *
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    /**
     * @return 指定哪些注解应该被注解处理器注册
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(GS.class.getCanonicalName());
        return types;
    }

    /**
     * @return 指定使用的 Java 版本。通常返回 SourceVersion.latestSupported()。
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    //************************* 以上是常用的模板 *********************************//


    private Map<String, HashSet<Element>> nameMap = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> annotated = roundEnvironment.getElementsAnnotatedWith(GS.class);
        //遍历处理带有注解的Element，将他们分类保存在Map中，key = 类名， value = 类中使用注解的搜索Element
        for (Element element : annotated) {
            Element parent = element.getEnclosingElement();//获取父类
//            List<? extends Element> enclosedElements = element.getEnclosedElements();//获取子类
            String parentName = parent.getSimpleName().toString();
            //从缓存中读取
            HashSet<Element> elements = nameMap.get(parentName);
            if (elements == null) {
                elements = new HashSet<>();
            }
            elements.add(element);
            nameMap.put(parentName, elements);
        }

        //生成JAVA文件
        generateJavaFile(nameMap);

        return true;
    }

    private void generateJavaFile(Map<String, HashSet<Element>> nameMap) {
        //遍历缓存中的所有类信息
        Set<Map.Entry<String, HashSet<Element>>> entries = nameMap.entrySet();
        for (Map.Entry<String, HashSet<Element>> entry : entries) {
            String className = entry.getKey();
            HashSet<Element> fields = entry.getValue();
            //生成一个JAVA类文件
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className + "$Bean")
                    .addModifiers(Modifier.PUBLIC);

            //遍历添加属性和对应的getter setter方法
            for (Element field : fields) {
                //只处理属性
                if (field.getKind().isField()) {
                    //获取字段名字
                    String fieldName = field.getSimpleName().toString();
                    //字段名字首字母大写 为了生成相应的方法用
                    char[] chars = fieldName.toCharArray();
                    chars[0] -= 32;
                    String firstUpperName = String.valueOf(chars);
                    //字段类型
                    TypeName type = TypeName.get(field.asType());
                    //生成一个字段
                    FieldSpec fieldSpec = FieldSpec.builder(type, fieldName, Modifier.PRIVATE).build();

                    //生成getter setter方法
                    MethodSpec getterMethod = MethodSpec.methodBuilder("get" + firstUpperName)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(type)
                            .addStatement("return " + fieldName)
                            .build();

                    MethodSpec setterMethod = MethodSpec.methodBuilder("set" + firstUpperName)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .addStatement("this." + fieldName + " = " + fieldName)
                            .addParameter(type, fieldName)
                            .build();

                    //将生成的字段属性和方法添加到java文件中
                    classBuilder.addField(fieldSpec)
                            .addMethod(getterMethod)
                            .addMethod(setterMethod);
                }

            }

            //添加信息完毕后 开始编译
            TypeSpec classFile = classBuilder.build();
            JavaFile javaFile = JavaFile.builder("work.kozh.apt.bulid", classFile).build();
            try {
                javaFile.writeTo(mFiler);
                System.out.println("开始自动生成java文件：" + className + "$Bean");
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
