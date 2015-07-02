package hook.xposed;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XRuntime extends XHook {

    private static final String className = "java.lang.Runtime";
    private static List<String> logList = null;
    private static XRuntime classLoadHook;

    public static XRuntime getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XRuntime();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "exec",
                String[].class, String[].class, File.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String[] prog = (String[]) param.args[0];
                        String[] envp = (String[]) param.args[1];
                        File dir = (File) param.args[2];
                        String cmd = "";
                        String env = "";
                        String dirName = "";
                        for (String str : prog) {
                            cmd += str;
                            cmd += " ";
                        }

                        if (envp != null) {
                            for (String str : envp) {
                                env += str;
                                env += " ";
                            }
                        }

                        if (dir != null) {
                            dirName = dir.getAbsolutePath();
                        }
                        String callRef = Stack.getCallRef();

                        Logger.log("[=== Runtime exec ===] ");
                        Logger.log("[=== Runtime exec ===] cmd : " + cmd );
                        Logger.log("[=== Runtime exec ===] env : " + cmd );
                        Logger.log("[=== Runtime exec ===] dir : " + cmd );
                        Logger.log("[=== Runtime exec ===] " + callRef);

                        logList.add("time:" + time);
                        logList.add("action:--Create New Process--");
                        logList.add("function:exec");
                        for (String str : prog) {
                            logList.add("command:" + str);
                        }
                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }
                });
    }

}
