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

public class XProcessBuilder extends XHook {

    private static final String className = "java.lang.ProcessBuilder";
    private static List<String> logList = null;
    private static XProcessBuilder classLoadHook;

    public static XProcessBuilder getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XProcessBuilder();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "start",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();

                        ProcessBuilder pb = (ProcessBuilder) param.thisObject;
                        List<String> cmds = pb.command();
                        StringBuilder sb = new StringBuilder();
                        sb.append(sb.append("CMD:"));
                        for (int i = 0; i < cmds.size(); i++) {
                            sb.append(cmds.get(i) + " ");
                        }

                        String callRef = Stack.getCallRef();
                        Logger.log("[=== ProcessBuilder start ===] ");
                        Logger.log("[=== ProcessBuilder start ===] " + sb);
                        Logger.log("[=== ProcessBuilder start ===] " + callRef);

                        logList.add("time:" + time);
                        logList.add("action:--Create New Process--");
                        logList.add("function:ProcessBuilder.start");
                        logList.add(sb.toString());
                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }
                });
    }

}
