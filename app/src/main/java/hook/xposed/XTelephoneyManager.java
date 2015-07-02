package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XTelephoneyManager extends XHook {
    private static final String className = "android.telephony.TelephonyManager";
    private static List<String> logList = null;
    private static XTelephoneyManager xTelephoneyManager;

    public static XTelephoneyManager getInstance() {
        if (xTelephoneyManager == null) {
            xTelephoneyManager = new XTelephoneyManager();
        }
        return xTelephoneyManager;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "getDeviceId",
                new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        Logger.log("[=== getDeviceId ===]");
                        Logger.log("[=== getDeviceId ===] " + Stack.getCallRef());

                        String time = Util.getSystemTime();
                        logList.add("time:" + time);
                        logList.add("action:--get IMEI--");
                        logList.add("function:getDeviceId");
//                        for (String log : logList) {
//                            XposedBridge.log(log);
//                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }

                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "getLine1Number", new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();

                        Logger.log("[=== getLine1Number ===]");
                        Logger.log("[=== getLine1Number ===] " + Stack.getCallRef());

                        logList.add("time:" + time);
                        logList.add("action:--get phnoeNumber--");
                        logList.add("function:getLine1Number");
                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }

                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "getSubscriberId", new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();

                        Logger.log("[=== getSubscriberId ===]");
                        Logger.log("[=== getSubscriberId ===] " + Stack.getCallRef());

                        logList.add("time:" + time);
                        logList.add("action:--get IMSI--");
                        logList.add("function:getSubscriberId");

                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }

                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "getNetworkOperatorName", new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        param.getResult();
                        param.setResult(new String("46001"));
                    }

                });
    }

}
