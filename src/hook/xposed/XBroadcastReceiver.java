package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XBroadcastReceiver extends XHook {
	private static final String className = "android.content.BroadcastReceiver";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XBroadcastReceiver classLoadHook;

	public static XBroadcastReceiver getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XBroadcastReceiver();
		}
		return classLoadHook;
	}

	@Override
	String getClassName() {
		// TODO Auto-generated method stub
		return className;
	}

	@Override
	void hook(String pkgName, ClassLoader classLoader) {
		// TODO Auto-generated method stub
		localpkgName = pkgName;
		logList = new ArrayList<String>();
		XposedHelpers.findAndHookMethod(className, classLoader,
				"abortBroadcast", new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--sms abort--");
						logList.add("function:abortBroadcast");
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
	}

}
