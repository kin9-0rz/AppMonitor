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

public class XWindowManageService extends XHook {
    // TODO ClassNotFound
    private static final String className = "com.android.server.wm.WindowManageService";
    private static List<String> logList = null;
    private static XWindowManageService xWindowManageService;

    public static XWindowManageService getInstance() {
        if (xWindowManageService == null) {
            xWindowManageService = new XWindowManageService();
        }
        return xWindowManageService;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "startViewServer", Integer.TYPE,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String port = (String) param.args[0];
                        String callRef = Stack.getCallRef();


                        Logger.log("[--- WindowManageService startViewServer ---]");
                        Logger.log("[--- WindowManageService startViewServer ---] " + port);
                        Logger.log("[--- WindowManageService startViewServer ---] " + callRef);

                        logList.add("time:" + time);
                        logList.add("startViewServer");
                        logList.add("port before " + port);

                        param.setResult(Boolean.valueOf(false));

                        for (String log : logList) {
                            XposedBridge.log(log);
                        }

                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }

                });

    }

}
