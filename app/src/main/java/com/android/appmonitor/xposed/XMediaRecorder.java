package com.android.appmonitor.xposed;

import android.media.MediaRecorder;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import com.android.appmonitor.util.Logger;
import com.android.appmonitor.util.Stack;
import com.android.appmonitor.util.Util;

public class XMediaRecorder extends XHook {

    private static final String className = MediaRecorder.class.getName();
    private static List<String> logList = null;
    private static XMediaRecorder xMediaRecorder;

    public static XMediaRecorder getInstance() {
        if (xMediaRecorder == null) {
            xMediaRecorder = new XMediaRecorder();
        }
        return xMediaRecorder;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "start", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                Logger.log("[*** Media Recorder ***]");
                Logger.log("[*** Media Recorder ***] " + Stack.getCallRef());


                String time = Util.getSystemTime();
                logList.add("time:" + time);
                logList.add("action:--start record--");
                logList.add("function:MediaRecorder.start");
                for (String log : logList) {
                    XposedBridge.log(log);
                }
                Util.writeLog(packageParam.packageName, logList);
                logList.clear();
            }
        });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "stop", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                String time = Util.getSystemTime();
                logList.add("time:" + time);
                logList.add("action:--stop record--");
                logList.add("function:MediaRecorder.stop");
                for (String log : logList) {
                    XposedBridge.log(log);
                }
                Util.writeLog(packageParam.packageName, logList);
                logList.clear();
            }
        });
    }

}
