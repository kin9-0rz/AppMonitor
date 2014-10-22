package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XActivityManager extends XHook{
	
	private static final String className = "android.app.ActivityManager";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XActivityManager classLoadHook;

	public static XActivityManager getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XActivityManager();
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
				"killBackgroundProcesses", String.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--kill background process--");
						logList.add("function:killBackgroundProcesses");
						logList.add("killed processes:" + param.args[0].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
		
		XposedHelpers.findAndHookMethod(className, classLoader,
				"forceStopPackage", String.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--force stop package--");
						logList.add("function:forceStopPackage");
						logList.add("stoped package:" + param.args[0].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
	}

}
