package hook.xposed;

import util.Util;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookApp implements IXposedHookLoadPackage {

	private static String packageName = null;

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) {
		// TODO Auto-generated method stub
		XposedBridge.log("loaded app:" + lpparam.packageName);
		packageName = Util.readData();
		if (!packageName.equals(lpparam.packageName))
			return;

		// hook TelephoneManger
		hookall(XTelephoneyManager.getInstance(), packageName, lpparam.classLoader);
		// hook SmsManager
		hookall(XSmsManger.getInstance(), packageName, lpparam.classLoader);
		// hook Class
		hookall(XClass.getInstance(), packageName, lpparam.classLoader);
		// hook String
//		hookall(XString.getInstance(), packageName, lpparam.classLoader);
		// hook URL
		hookall(XURL.getInstance(), packageName, lpparam.classLoader);
		// hook HttpGet
		hookall(XHttpGet.getInstance(), packageName, lpparam.classLoader);
		// hook HttpPost
		hookall(XHttpPost.getInstance(), packageName, lpparam.classLoader);
		// hook AbstractHttpClient
		hookall(XAbstractHttpClient.getInstance(), packageName, lpparam.classLoader);
		// hook BroadcastReceiver
		hookall(XBroadcastReceiver.getInstance(), packageName, lpparam.classLoader);
		// hook System
		hookall(XSystem.getInstance(), packageName, lpparam.classLoader);
		//hook DexClassLoader
		hookall(XDexClassLoader.getInstance(), packageName, lpparam.classLoader);

	}
	
	public void hookall(XHook xhook, String pkgName, ClassLoader classLoader){
		xhook.hook(pkgName, classLoader);
	}
}
