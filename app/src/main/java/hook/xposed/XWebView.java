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

public class XWebView extends XHook {

    private static final String className = "android.webkit.WebView";
    private static List<String> logList = null;
    private static XWebView xWebView;

    public static XWebView getInstance() {
        if (xWebView == null) {
            xWebView = new XWebView();
        }
        return xWebView;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();


        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "loadUrl", String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        String url = param.args[0].toString();
                        String callRef = Stack.getCallRef();

                        Logger.log("[--- Webview loadUrl ---] ");
                        Logger.log("[--- Webview loadUrl ---] " + url);
                        Logger.log("[--- Webview loadUrl ---] " + callRef);

                        logList.add("time:" + time);
                        logList.add("Webview -> loadUrl");
                        logList.add("url : " + url);

                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }
                });


    }

}
