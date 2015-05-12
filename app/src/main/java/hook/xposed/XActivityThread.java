package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XActivityThread extends XHook {

    private static final String className = "android.app.ActivityThread";
    private static List<String> logList = null;
    private static XActivityThread classLoadHook;

    public static XActivityThread getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XActivityThread();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();
        try {
            Class<?> receiverDataClass = Class.forName("android.app.ActivityThread$ReceiverData");

            if (receiverDataClass != null) {
                XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                        "handleReceiver", receiverDataClass, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) {
                                String time = Util.getSystemTime();
                                String callRef = Stack.getCallRef();
                                String revName = param.args[0].toString();

                                Logger.log("[=== ActivityThread$ReceiverData handleReceiver ===]");
                                Logger.log("[=== ActivityThread$ReceiverData handleReceiver ===] Receiver Name : " + revName);
                                Logger.log("[=== ActivityThread$ReceiverData handleReceiver ===] " + callRef);

                                logList.add("time:" + time);
                                logList.add("action:--handler data receiver--");
                                logList.add("function:handleReceiver");
                                logList.add("The Receiver Information:" + revName);
                                for (String log : logList) {
                                    XposedBridge.log(log);
                                }
                                Util.writeLog(packageParam.packageName, logList);
                                logList.clear();
                            }
                        });
            }
        } catch (ClassNotFoundException e) {
            System.out.println("class not found!!!");
        }
    }

}
