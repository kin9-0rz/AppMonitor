package hook.xposed;

import java.util.List;

import util.Util;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookApp implements IXposedHookLoadPackage {

	private static String packageName = null;
	private static List<String> appList = null;

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) {
		// TODO Auto-generated method stub
		appList = Util.jsonStr2list(Util.readData());
		if (appList == null||!appList.contains(lpparam.packageName))
			return;
		else packageName = lpparam.packageName;

		//hook TelephoneManger
		hookall(XTelephoneyManager.getInstance(), packageName, lpparam.classLoader);
		//hook SmsManager
		hookall(XSmsManger.getInstance(), packageName, lpparam.classLoader);
		//hook Class
		hookall(XClass.getInstance(), packageName, lpparam.classLoader);
		//hook Method
		hookall(XMethod.getInstance(), packageName, lpparam.classLoader);
		//hook URL
		hookall(XURL.getInstance(), packageName, lpparam.classLoader);
		//hook HttpGet
		hookall(XHttpGet.getInstance(), packageName, lpparam.classLoader);
		//hook HttpPost
		hookall(XHttpPost.getInstance(), packageName, lpparam.classLoader);
		//hook AbstractHttpClient
		hookall(XAbstractHttpClient.getInstance(), packageName, lpparam.classLoader);
		//hook BroadcastReceiver
		hookall(XBroadcastReceiver.getInstance(), packageName, lpparam.classLoader);
		//hook DexClassLoader
		hookall(XDexClassLoader.getInstance(), packageName, lpparam.classLoader);
		//hook ActivityManager
		hookall(XActivityManager.getInstance(), packageName, lpparam.classLoader);
		//hook ContentResolver
		hookall(XContentResolver.getInstance(), packageName, lpparam.classLoader);
		//hook ContextImpl
		hookall(XContextImpl.getInstance(), packageName, lpparam.classLoader);
		//hook MediaRecorder
		hookall(XMediaRecorder.getInstance(), packageName, lpparam.classLoader);
		//hook Runtime
		hookall(XRuntime.getInstance(), packageName, lpparam.classLoader);
		//hook ActivityThread
		hookall(XActivityThread.getInstance(), packageName, lpparam.classLoader);
		//hook AudioRecord
		hookall(XAudioRecord.getInstance(), packageName, lpparam.classLoader);
		//hook Notification
		hookall(XNotificationManager.getInstance(), packageName, lpparam.classLoader);
		//hook ProcessBuilder
		hookall(XProcessBuilder.getInstance(), packageName, lpparam.classLoader);
	}
	
	public void hookall(XHook xhook, String pkgName, ClassLoader classLoader){
		xhook.hook(pkgName, classLoader);
	}
}
