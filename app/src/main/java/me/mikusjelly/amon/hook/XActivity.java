package me.mikusjelly.amon.hook;

import android.app.ActivityManager;
import android.app.AndroidAppHelper;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import me.mikusjelly.amon.utils.LogWriter;

public class XActivity extends MethodHook {
    private static final String mClassName = "android.app.Activity";
    private Methods mMethod;

    private XActivity(Methods method) {
        super(mClassName, method.name());
        mMethod = method;
    }


    public static List<MethodHook> getMethodHookList() {
        List<MethodHook> methodHookList = new ArrayList<MethodHook>();
        for (Methods method : Methods.values()) {
            methodHookList.add(new XActivity(method));
        }
        return methodHookList;
    }

    @Override
    public void after(MethodHookParam param) throws Throwable {
        if (mMethod == Methods.onCreate) {
            Context context = AndroidAppHelper.currentApplication();
            if (context == null) {
                return;
            }

            if (Build.VERSION.SDK_INT < 0x15) {
                List<ActivityManager.RunningTaskInfo> taskList;

                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                if (activityManager == null) {
                    return;
                }

                int isOK = context.checkCallingPermission("android.permission.GET_TASKS");
                if (isOK == PackageManager.PERMISSION_DENIED) {
                    return;
                }

                taskList = activityManager.getRunningTasks(1);

                if (taskList != null) {
                    ComponentName topActivity = taskList.get(0).topActivity;
                    
                    String logMsg = String.format("{\"TOP Activity\": \"%s\", ", topActivity.toString());
                    LogWriter.logStack(logMsg);
                }
            } else {

            }


        }


    }


    private enum Methods {
        onCreate
    }

}
