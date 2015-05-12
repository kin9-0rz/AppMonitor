package hook.xposed;

import android.content.ContextWrapper;
import android.content.Intent;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;

public class XContext extends XHook {

    private static final String className = ContextWrapper.class.getName();
    private static XContext xContext;

    public static XContext getInstance() {
        if (xContext == null) {
            xContext = new XContext();
        }
        return xContext;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "sendBroadcast",
                Intent.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        Intent intent = (Intent) param.args[0];
                        String receiverPermission = (String) param.args[1];
                        String callRef = Stack.getCallRef();

                        Logger.log("[=== sendBroadcast ===] " + intent.getAction());
                        Logger.log("[=== sendBroadcast ===] " + receiverPermission);
                        Logger.log("[=== sendBroadcast ===] " + callRef);
                    }
                });
    }

}
