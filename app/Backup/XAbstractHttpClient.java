package hook.xposed;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XAbstractHttpClient extends XHook {
    private static final String className = "org.apache.http.impl.client.AbstractHttpClient";
    private static List<String> logList = null;
    private static XAbstractHttpClient classLoadHook;

    public static XAbstractHttpClient getInstance() {
        if (classLoadHook == null) {
            classLoadHook = new XAbstractHttpClient();
        }
        return classLoadHook;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "execute",
                HttpHost.class, HttpRequest.class, HttpContext.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String callRef = Stack.getCallRef();
                        String url = param.args[0].toString();
                        url = Logger.isFeeUrl(url);

                        Logger.log("[AbstractHttpClient -> execute] " + url + " <- " + callRef);

                        logList.add("time:" + time);
                        logList.add("action:--executed--");
                        logList.add("function:execute");
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
