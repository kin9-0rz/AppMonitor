package hook.xposed;

import android.app.Notification;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XNotificationManager extends XHook {

    private static final String className = "android.app.NotificationManager";
    private static List<String> logList = null;
    private static XNotificationManager classLoadHook;

    public static XNotificationManager getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XNotificationManager();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "notify",
                int.class, Notification.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String notificationName = param.args[1].toString();
                        String callRef = Stack.getCallRef();

                        Logger.log("[=== AD ===]");
                        Logger.log("[=== AD ===] " + notificationName);
                        Logger.log("[=== AD ===] " + callRef);

                        logList.add("time:" + time);
                        logList.add("action:--Send Notification--");
                        logList.add("function:notify");
                        logList.add("Notification:" + notificationName);
                        logList.add(callRef);

                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }
                });
    }

}
