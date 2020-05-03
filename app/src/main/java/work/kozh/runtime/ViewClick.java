package work.kozh.runtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.IdRes;

/**
 * 注册控件的点击事件
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface ViewClick {
    @IdRes int[] value() default {-1};
}
