package hook.xposed;

import java.io.File;

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

    // public BaseDexClassLoader(String	dexPath,File optimizedDirectory, String	libraryPath, ClassLoader parent)
    // libcore/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
    // http://developer.android.com/reference/dalvik/system/BaseDexClassLoader.html

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookConstructor(className, packageParam.classLoader,
                String.class, File.class, String.class,
                ClassLoader.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String dexPath = (String) param.args[0];
                        File optimizedDir = (File) param.args[1];
                        String libPath = (String) param.args[2];
                        ClassLoader parent = (ClassLoader) param.args[3];


                        Logger.log("[### DexClassLoader ###] ");
                        Logger.log("[### DexClassLoader ###] dexPath : " + dexPath);
                        Logger.log("[### DexClassLoader ###] optimizedDir : " + optimizedDir);
                        Logger.log("[### DexClassLoader ###] libPath : " + libPath);
                        Logger.log("[### DexClassLoader ###] parent : " + parent);

                        Logger.logCallRef("[### DexClassLoader ###]");

                    }
                });

    }

}
