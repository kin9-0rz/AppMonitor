package com.android.appmonitor.xposed;

import android.app.ActivityManager;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import com.android.appmonitor.util.Logger;
import com.android.appmonitor.util.Stack;
import com.android.appmonitor.util.Util;

public class XActivityManager extends XHook {

    private static final String className = ActivityManager.class.getName();
    private static List<String> logList = null;
    private static XActivityManager classLoadHook;

    public static XActivityManager getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XActivityManager();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "killBackgroundProcesses", String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String callRef = Stack.getCallRef();
                        String processes = param.args[0].toString();

                        Logger.log("[### ActivityManager killBackgroundProcesses ###]");
                        Logger.log("[### ActivityManager killBackgroundProcesses ###] " + processes);
                        Logger.log("[### ActivityManager killBackgroundProcesses ###] " + callRef);

                        logList.add("time:" + time);
                        logList.add("action:--kill background process--");
                        logList.add("function:killBackgroundProcesses");
                        logList.add("killed processes:" + processes);
                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }
                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "forceStopPackage", String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String callRef = Stack.getCallRef();
                        String pkgName = param.args[0].toString();

                        Logger.log("[### ActivityManager -> forceStopPackage ###]");
                        Logger.log("[### ActivityManager -> forceStopPackage ###] " + pkgName);
                        Logger.log("[### ActivityManager -> forceStopPackage ###] " + callRef);

                        logList.add("time:" + time);
                        logList.add("action:--force stop package--");
                        logList.add("function:forceStopPackage");
                        logList.add("stoped package:" + pkgName);
                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }
                });
    }
}
