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
//		packageName = Util.readData();
		appList = Util.jsonStr2list(Util.readData());
		if (appList == null||!appList.contains(lpparam.packageName))
			return;
		else packageName = lpparam.packageName;
//		System.out.println("*****************succeed***********");

		// hook TelephoneManger
		hookall(XTelephoneyManager.getInstance(), packageName, lpparam.classLoader);
		// hook SmsManager
		hookall(XSmsManger.getInstance(), packageName, lpparam.classLoader);
		// hook Class
		hookall(XClass.getInstance(), packageName, lpparam.classLoader);
		// hook Method
		hookall(XMethod.getInstance(), packageName, lpparam.classLoader);
		// hook File
//		hookall(XFlie.getInstance(), packageName, lpparam.classLoader);
		// hook String too much
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
		// hook System will can not open app
//		hookall(XSystem.getInstance(), packageName, lpparam.classLoader);
		//hook DexClassLoader waiting to test
		hookall(XDexClassLoader.getInstance(), packageName, lpparam.classLoader);

	}
	
	public void hookall(XHook xhook, String pkgName, ClassLoader classLoader){
		xhook.hook(pkgName, classLoader);
	}
}
