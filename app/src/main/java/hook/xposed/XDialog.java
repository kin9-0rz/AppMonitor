package hook.xposed;

import android.app.Dialog;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XDialog extends XHook {
    private static final String className = Dialog.class.getName();
    private static List<String> logList = null;
    private static XDialog xDialog;

    public static XDialog getInstance() {
        if (xDialog == null) {
            xDialog = new XDialog();
        }
        return xDialog;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "setContentView", View.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String callRef = Stack.getCallRef();

                        Logger.log("[--- DIALOG ---]");
                        Logger.log("[--- DIALOG ---]" + callRef);

                        logList.add("time:" + time);
                        logList.add("POP DIALOG");
                        logList.add("StackTrace : " + callRef);

                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }
                });
    }

}
