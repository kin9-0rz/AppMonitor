package hook.xposed;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XWifiManager extends XHook {

    private static final String className = WifiManager.class.getName();
    private static List<String> logList = null;
    private static XWifiManager xWifiManager;

    public static XWifiManager getInstance() {
        if (xWifiManager == null) {
            xWifiManager = new XWifiManager();
        }
        return xWifiManager;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "setWifiEnabled",
                Boolean.TYPE, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String callRef = Stack.getCallRef();
                        boolean flag = (Boolean)param.args[0];
                        if (!flag) {
                            Logger.log("[### Disable Wifi ###]");
                            Logger.log("[### Disable Wifi ###] " + callRef);
                        }
                    }
                });
    }

}
