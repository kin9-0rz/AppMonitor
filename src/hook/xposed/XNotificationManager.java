package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XNotificationManager extends XHook {

	private static final String className = "android.app.NotificationManager";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XNotificationManager classLoadHook;

	public static XNotificationManager getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XNotificationManager();
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
		XposedHelpers.findAndHookMethod(className, classLoader, "notify",
				int.class, Notification.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--Send Notification--");
						logList.add("function:notify");
						logList.add("Notification:" + param.args[1].toString());
						for (String log : logList) {
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName, logList);
						logList.clear();
					}
				});
	}

}
