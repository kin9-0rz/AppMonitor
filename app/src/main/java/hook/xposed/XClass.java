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

public class XClass extends XHook {
    private static final String className = "java.lang.Class";
    private static List<String> logList = null;
    private static XClass classLoadHook;

    public static XClass getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XClass();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "forName",
                String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String name = (String) param.args[0];
                        Logger.log("[--- Class forName ---] " + name);

                        String callRef = Stack.getCallRef();
                        Logger.log("[--- Class forName ---] " + callRef);

//                        String time = Util.getSystemTime();
//                        logList.add("time:" + time);
//                        logList.add("action:--class for name--");
//                        logList.add("function:Class.forName");
//                        logList.add("target:" + name);
//                        for (String log : logList) {
//                            XposedBridge.log(log);
//                        }
//                        Util.writeLog(packageParam.packageName, logList);
//                        logList.clear();
                    }
                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "getMethod",
                String.class, Class[].class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String name = (String) param.args[0];
                        Logger.log("[--- Class getMethod ---] " + name);

                        Object obj = param.args[1];
                        if (obj instanceof Class) {
                            Logger.log("[--- Class getMethod ---] " + ((Class) obj).getName());
                        } else if (obj instanceof Class[] && ((Class[])obj).length > 0) {
                            Logger.log("[--- Class getMethod ---] " + ((Class[])obj)[0].getName());
                        }
                        String callRef = Stack.getCallRef();
                        Logger.log("[--- Class getMethod ---] " + callRef);

                        String time = Util.getSystemTime();
                        logList.add("time:" + time);
                        logList.add("action:--get method--");
                        logList.add("function:getMethod");
                        logList.add("method:" + name);
                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }
                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "getDeclaredMethod",
                String.class, Class[].class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String name = (String) param.args[0];
                        Logger.log("[--- Class getDeclaredMethod ---] " + name);

                        Object obj = param.args[1];
                        if (obj instanceof Class) {
                            Logger.log("[--- Class getDeclaredMethod ---] " + ((Class) obj).getName());
                        } else if (obj instanceof Class[] && ((Class[])obj).length > 0) {
                            Logger.log("[--- Class getDeclaredMethod ---] " + ((Class[])obj)[0].getName());
                        }
                        String callRef = Stack.getCallRef();


                        Logger.log("[--- Class getDeclaredMethod ---] " + callRef);
                    }
                });
    }

}
