package hook.xposed;

import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XApplicationPackageManager extends XHook {

    private static final String className = "android.app.ApplicationPackageManager";
    private static List<String> logList = null;
    private static XApplicationPackageManager classLoadHook;

    public static XApplicationPackageManager getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XApplicationPackageManager();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "setComponentEnabledSetting", ComponentName.class, int.class,
                int.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        logList.add("time:" + time);
                        logList.add("action:--set icon disable or enable--");
                        logList.add("function:setComponentEnabledSetting");
                        // ComponentName cn = (ComponentName) param.args[0];
                        // String cnName = cn.getPackageName() + "/" +
                        // cn.getClassName();
                        logList.add("componetName:" + param.args[0]);

                        int state = (Integer) param.args[1];
                        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                            logList.add("action:--COMPONENT_ENABLED_STATE_DISABLED--");

                            String callRef = Stack.getCallRef();
                            Logger.log("[*** Hide Icon ***]");
                            Logger.log("[*** Hide Icon ***] " + callRef);
                        } else if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
                            logList.add("action:--COMPONENT_ENABLED_STATE_ENABLED--");
                        else if (state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
                            logList.add("action:--COMPONENT_ENABLED_STATE_DEFAULT--");

                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }
                });


        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "getInstalledPackages", Integer.TYPE, Integer.TYPE, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String callRef = Stack.getCallRef();
                        Logger.log("[=== getInstalledPackages ===] " + callRef);

                        // TODO modify result
//                        Object obj = param.getResult();
//                        if (obj == null) {
//                            Logger.log("[=== getInstalledPackages ===] " + "NULL");
//                        } else if (obj instanceof List) {
//                            List<PackageInfo> list = (List<PackageInfo>) obj;
//                            List<PackageInfo> tmp = (List<PackageInfo>) obj;
//                            for (PackageInfo info : list) {
//                                Logger.log("[*** Test PackageInfo ***] " + info.packageName);
//                                if (info.packageName.contains("xposed")
//                                        || info.packageName.contains("acgmohu")) {
//                                    tmp.remove(info);
//                                }
//                            }
//                            param.setResult(tmp);
//                        } else {
//                            List<PackageInfo> list = new ArrayList<PackageInfo>();
//                            PackageInfo info = new PackageInfo();
//                            info.packageName = "com.game.kill2kill";
//                            info.packageName = "";
//                            list.add(info);
//                            param.setResult(list);
//                        }

                    }
                });


        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "getInstalledApplications", Integer.TYPE, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String callRef = Stack.getCallRef();
//                        Logger.log("[=== getInstalledApplications ===]");
                        Logger.log("[=== getInstalledApplications ===] " + callRef);

//                        Object obj = param.getResult();
//                        if (obj instanceof List) {
//                            List<ApplicationInfo> list = (List<ApplicationInfo>) obj;
//                            List<ApplicationInfo> tmp = (List<ApplicationInfo>) obj;
//
//                            for (ApplicationInfo info : list) {
//                                if (info.packageName.contains("xposed")
//                                        || info.packageName.contains("acgmohu")) {
//                                    tmp.remove(info);
//                                }
//                            }
//                            param.setResult(tmp);
//                        } else {
//                            List<ApplicationInfo> list = new ArrayList<ApplicationInfo>();
//                            ApplicationInfo info = new ApplicationInfo();
//                            info.processName = "com.game.kill2kill";
//
//                            list.add(info);
//                            param.setResult(list);
//                        }
                    }
                });
    }
}
