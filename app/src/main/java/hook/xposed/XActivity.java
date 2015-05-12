package hook.xposed;

import android.app.ActivityManager;
import android.app.AndroidAppHelper;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XActivity extends XHook {

    private static final String className = "android.app.Activity";
    private static List<String> logList = null;
    private static XActivity xActivity;

    public static XActivity getInstance() {
        if (xActivity == null) {
            xActivity = new XActivity();
        }
        return xActivity;
    }

    public static String getTopActivity(Context ctx) {
        String topActivityName = "";
        List<ActivityManager.RunningTaskInfo> taskList;

        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return topActivityName;
        }

        int isOK = ctx.checkCallingPermission("android.permission.GET_TASKS");
        if (isOK == PackageManager.PERMISSION_DENIED) {
            return topActivityName;
        }

        taskList = activityManager.getRunningTasks(1);

        if (taskList != null) {
            Logger.log("[=== POP_AD ===] number : " + taskList.get(0).numActivities);
            ComponentName topActivity = taskList.get(0).topActivity;
            Logger.log("[=== POP_AD ===] name : " + topActivity.toString());

            if (!ctx.getApplicationInfo().packageName.equals(topActivity.getPackageName())) {
                Logger.log("[=== POP_AD ===] POP_AD from another Application.");
            }

            topActivityName = taskList.get(0).topActivity.toString();
        }

        return topActivityName;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String callRef = Stack.getCallRef();


                        Context context = AndroidAppHelper.currentApplication();
                        if (context != null) {
                            HookApp.context = context;
                            String topActivityName = getTopActivity(context);
//                            String strakTace = Stack.getCallRef();

                            Logger.log("[=== ACTIVITY ===] ");
                            Logger.log("[=== ACTIVITY ===] " + topActivityName);
                            Logger.log("[=== ACTIVITY ===] " + callRef);

                            logList.add("time:" + time);
                            logList.add("=== POP_ACTIVITY ===");
                            logList.add(topActivityName);
                            logList.add(callRef);

                            for (String log : logList) {
                                XposedBridge.log(log);
                            }

                            Util.writeLog(packageParam.packageName, logList);
                            logList.clear();
                        } else {
                            Logger.log("[=== Activity ===] " + callRef);
                        }
                    }
                });
    }

}
