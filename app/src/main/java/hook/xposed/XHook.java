package hook.xposed;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public abstract class XHook {
//    abstract public String getClassName();
//    abstract void hook(String pkgName, XC_LoadPackage.LoadPackageParam classLoader);

    abstract void hook(XC_LoadPackage.LoadPackageParam packageParam);
}