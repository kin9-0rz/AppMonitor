package com.android.appmonitor.xposed;

import android.media.AudioRecord;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import com.android.appmonitor.util.Logger;
import com.android.appmonitor.util.Stack;
import com.android.appmonitor.util.Util;

public class XAudioRecord extends XHook {

    private static final String className = AudioRecord.class.getName();
    private static List<String> logList = null;
    private static XAudioRecord xAudioRecord;

    public static XAudioRecord getInstance() {
        if (xAudioRecord == null) {
            xAudioRecord = new XAudioRecord();
        }
        return xAudioRecord;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "startRecording", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {

                        Logger.log("[*** Audio Record ***]");
                        Logger.log("[*** Audio Record ***] " + Stack.getCallRef());

                        String time = Util.getSystemTime();
                        logList.add("time:" + time);
                        logList.add("action:--start record--");
                        logList.add("function:startRecordinge");
                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }
                });
    }

}
