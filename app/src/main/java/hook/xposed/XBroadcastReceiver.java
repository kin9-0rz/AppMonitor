package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XBroadcastReceiver extends XHook {
	private static final String className = "android.content.BroadcastReceiver";
	private static List<String> logList = null;
	private static XBroadcastReceiver xBroadcastReceiver;

	public static XBroadcastReceiver getInstance() {
		if (xBroadcastReceiver == null) {
			xBroadcastReceiver = new XBroadcastReceiver();
		}
		return xBroadcastReceiver;
	}

	@Override
	void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
		logList = new ArrayList<String>();
		XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
				"abortBroadcast", new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String callRef = Stack.getCallRef();
						Logger.log("[*** abort Broadcast ***]");
						Logger.log("[*** abort Broadcast ***] " + callRef);

						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--sms abort--");
						logList.add("function:abortBroadcast");

						for(String log : logList){
							XposedBridge.log(log);
						}

						Util.writeLog(packageParam.packageName,logList);
						logList.clear();
					}
				});
	}
}
