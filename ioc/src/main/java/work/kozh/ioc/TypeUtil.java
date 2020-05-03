package work.kozh.ioc;

import com.squareup.javapoet.ClassName;

public class TypeUtil {
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName FINDER = ClassName.get("work.kozh.apt", "Finder");
    public static final ClassName PROVIDER = ClassName.get("work.kozh.apt.provider", "Provider");
}
