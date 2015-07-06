package hook.xposed;

import android.view.View;
import android.view.ViewGroup;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;

public class XViewGroup extends XHook {
    private static final String className = ViewGroup.class.getName();
    private static XViewGroup xViewGroup;

    public static XViewGroup getInstance() {
        if (xViewGroup == null) {
            xViewGroup = new XViewGroup();
        }
        return xViewGroup;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "addView",
                View.class, Integer.TYPE, ViewGroup.LayoutParams.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        View view = (View) param.args[0];
                        String viewName = view.getClass().getName();

                        // TODO maybe read the view api to a list could be better ...
                        if (viewName.startsWith("android.widget.") || viewName.startsWith("android.view.")
                                || viewName.startsWith("android.support.v7.widget.") || viewName.startsWith("android.support.v7.internal.widget.")
                                || viewName.startsWith("com.android.internal.widget.") || viewName.startsWith("com.android.internal.view")) {
                            return;
                        }

                        Logger.log("[=== ViewGroup addView ===] ");
                        Logger.log("[=== ViewGroup addView ===] " + viewName);
                        Logger.logCallRef("[=== ViewGroup addView ===]");


                    }

                });

    }

}
