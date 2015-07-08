package com.android.appmonitor.xposed;

import android.net.wifi.WifiManager;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import com.android.appmonitor.util.Logger;
import com.android.appmonitor.util.Stack;

public class XWifiManager extends XHook {

    private static final String className = WifiManager.class.getName();
    private static XWifiManager xWifiManager;

    public static XWifiManager getInstance() {
        if (xWifiManager == null) {
            xWifiManager = new XWifiManager();
        }
        return xWifiManager;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "setWifiEnabled",
                Boolean.TYPE, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String callRef = Stack.getCallRef();
                        boolean flag = (Boolean)param.args[0];
                        if (flag) {
                            Logger.log("[### Enable Wifi ###]");
                            Logger.log("[### Enable Wifi ###] " + callRef);
                        } else {
                            Logger.log("[### Disable Wifi ###]");
                            Logger.log("[### Disable Wifi ###] " + callRef);
                        }
                    }
                });
    }

}
