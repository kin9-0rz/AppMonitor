package hook.xposed;

import java.io.File;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;

public class XBaseDexClassLoader extends XHook {
    private static final String className = "dalvik.system.BaseDexClassLoader";
    private static XBaseDexClassLoader classLoadHook;

    public static XBaseDexClassLoader getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XBaseDexClassLoader();
        }
        return classLoadHook;
    }

    // @formatter:off
    // public BaseDexClassLoader(String	dexPath,File optimizedDirectory, String	libraryPath, ClassLoader parent)
    // libcore/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
    // http://developer.android.com/reference/dalvik/system/BaseDexClassLoader.html
    // @formatter:on

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookConstructor(className, packageParam.classLoader,
                String.class, File.class, String.class,
                ClassLoader.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String dexPath = (String) param.args[0];
                        String optimizedDir = (String) param.args[1];
                        String libPath = (String) param.args[2];
                        ClassLoader parent = (ClassLoader) param.args[3];


                        Logger.log("[### BaseDexClassLoader ###] ");
                        Logger.log("[### BaseDexClassLoader ###] dexPath : " + dexPath);
                        Logger.log("[### BaseDexClassLoader ###] optimizedDir : " + optimizedDir);
                        Logger.log("[### BaseDexClassLoader ###] libPath : " + libPath);
                        Logger.log("[### BaseDexClassLoader ###] parent : " + parent);

                        Logger.logCallRef();

                    }
                });

    }

}
