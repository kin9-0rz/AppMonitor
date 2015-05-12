package hook.xposed;

import android.content.Context;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import util.Util;

public class HookApp implements IXposedHookLoadPackage {
    private static List<String> appList = null;
    public static Context context;



    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) {
        appList = Util.jsonStr2list(Util.readData());
        if (appList == null || !appList.contains(loadPackageParam.packageName)) {
            return;
        }
//        context = AndroidAppHelper.currentApplication();
//        if (context == null) {
//            Logger.log("***** HookApp context is NULL !!!! *****");
//        } else {
//            Logger.log("*** Nice ***");
//        }

        hook(XAbstractHttpClient.getInstance(), loadPackageParam);
        hook(XActivity.getInstance(), loadPackageParam);
        hook(XActivityManager.getInstance(), loadPackageParam);
        hook(XActivityThread.getInstance(), loadPackageParam);
        hook(XApplicationPackageManager.getInstance(), loadPackageParam);
        hook(XAssetManager.getInstance(), loadPackageParam);
        hook(XAudioRecord.getInstance(), loadPackageParam);
        hook(XBroadcastReceiver.getInstance(), loadPackageParam);
//        hook(XClass.getInstance(), loadPackageParam);
        hook(XContext.getInstance(), loadPackageParam);
        hook(XContextImpl.getInstance(), loadPackageParam);
        hook(XContentResolver.getInstance(), loadPackageParam);
        hook(XDexClassLoader.getInstance(), loadPackageParam);
        hook(XDialog.getInstance(), loadPackageParam);
        hook(XMediaRecorder.getInstance(), loadPackageParam);
        hook(XNotificationManager.getInstance(), loadPackageParam);
        hook(XProcessBuilder.getInstance(), loadPackageParam);
        hook(XRuntime.getInstance(), loadPackageParam);
        hook(XSmsManger.getInstance(), loadPackageParam);
        hook(XSmsMessage.getInstance(), loadPackageParam);
        hook(XString.getInstance(), loadPackageParam);
        hook(XTelephoneyManager.getInstance(), loadPackageParam);
        hook(XURL.getInstance(), loadPackageParam);
        hook(XWebView.getInstance(), loadPackageParam);
        hook(XViewGroup.getInstance(), loadPackageParam);
//        hook(XWindowManageService.getInstance(), loadPackageParam);
        hook(XWifiManager.getInstance(), loadPackageParam);

    }

    public void hook(XHook xhook, LoadPackageParam packageParam) {
        xhook.hook(packageParam);
    }
}
