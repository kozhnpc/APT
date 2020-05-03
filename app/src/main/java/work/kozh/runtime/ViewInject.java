package work.kozh.runtime;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 采用注解+反射的方式 注入控件
 * <p>
 * 为了解决项目组件化开发时，无法使用ButterKnife的问题，自己重新写了一个简易版的IOC注解框架
 * 这里简单的使用反射来处理，虽然对性能会有损耗，但实际过程中，影响很小
 */
public class ViewInject {

    /**
     * 这是兼容Activity界面
     *
     * @param activity
     */
    public static void inject(Activity activity) {
        inject(activity, new ViewCast(activity));
    }

    /**
     * 这是兼容普通的View界面
     *
     * @param view
     */
    public static void inject(View view) {
        inject(view, new ViewCast(view));
    }

    /**
     * 这是兼容fragment、Dialog等界面 传入fragment、Dialog等对象，再传入view
     *
     * @param object
     * @param view
     */
    public static void inject(Object object, View view) {
        inject(object, new ViewCast(view));
    }

    /**
     * @param viewCast 封装后的View转换器 这个目的只是为了统一fvb的方法
     * @param object   这是原来页面的对象 如activity fragment等 需要通过这个来反射源代码
     */
    private static void inject(final Object object, final ViewCast viewCast) {

        //view的注册
        Field[] fields = object.getClass().getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (Field field : fields) {

            boolean annotationPresent = field.isAnnotationPresent(InjectView.class);
            if (annotationPresent) {
                InjectView annotation = field.getAnnotation(InjectView.class);
                int id = annotation.value();
                if (id == -1) {
                    continue;
                }
                Log.i("TAG", "注册view" + " id:" + id);
                View view = viewCast.findViewById(id);
                Class<?> clazz = field.getType();
                try {
                    field.set(object, clazz.cast(view));

//                    Method listener = clazz.getDeclaredMethod("setOnClickListener", View.OnClickListener.class);
//                    listener.invoke(superclass, viewCast);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("TAG", "出错：" + e.getMessage());
                }
            }
        }

        //view的点击事件
        Method[] methods = object.getClass().getDeclaredMethods();
        AccessibleObject.setAccessible(methods, true);
        for (final Method method : methods) {
            boolean annotationPresent = method.isAnnotationPresent(ViewClick.class);
            if (annotationPresent) {
                ViewClick click = method.getAnnotation(ViewClick.class);
                int[] ids = click.value();
                if (ids.length == 0 || ids[0] == -1) {
                    break;
                }

                for (int id : ids) {
                    Log.i("TAG", "注册点击事件" + method.getName() + " id:" + id);
                    final View view = viewCast.findViewById(id);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                method.invoke(object, view);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        }


    }

    /**
     * 这是封装不同情况下获取view的方法
     * 不管传入的是什么（Activity或者 view），都是直接使用fvb
     * 但是类型可能不同，需要封装一层
     */
    static class ViewCast {

        private Activity mActivity;
        private View mView;

        ViewCast(Activity activity) {
            this.mActivity = activity;
        }

        ViewCast(View view) {
            this.mView = view;
        }

        View findViewById(int resId) {

            if (mActivity != null) {
                return mActivity.findViewById(resId);
            }

            if (mView != null) {
                return mView.findViewById(resId);
            }
            return null;
        }

    }


}
