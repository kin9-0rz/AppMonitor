package hook.xposed;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XURL extends XHook {
    private static final String className = "java.net.URL";
    private static List<String> logList = null;
    private static XURL classLoadHook;

    public static XURL getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XURL();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "openConnection", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        URL url = (URL) param.thisObject;
                        String callRef = Stack.getCallRef();


                        boolean flag = Logger.isFeeUrl(url);
                        if (flag) {
                            Logger.log("[### URL openConnection ###]");
                            Logger.log("[### URL openConnection ###] " + url);
                            Logger.log("[### URL openConnection ###] " + callRef);
                        } else {
                            Logger.log("[=== URL openConnection ===]");
                            Logger.log("[=== URL openConnection ===] " + url);
                            Logger.log("[=== URL openConnection ===] " + callRef);
                        }

                        logList.add("time:" + time);
                        logList.add("action:--connect url--");
                        logList.add("function:openConnection");
                        logList.add("url:" + url);
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
