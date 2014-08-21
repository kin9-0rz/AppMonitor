package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XTelephoneyManager extends XHook {
	private static final String className = "android.telephony.TelephonyManager";
	private static String localpkgName = null;
	private static ClassLoader localcl = null;
	private static List<String> logList = null;
	private static XTelephoneyManager classLoadHook;

	public static XTelephoneyManager getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XTelephoneyManager();
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
		localcl = classLoader;
		logList = new ArrayList<String>();
		XposedHelpers.findAndHookMethod(className, classLoader, "getDeviceId",
				new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--get IMEI--");
						logList.add("function:getDeviceId");
						logList.add("call class:"+localcl.getClass().toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}

				});

		XposedHelpers.findAndHookMethod(className, classLoader,
				"getLine1Number", new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--get phnoeNumber--");
						logList.add("function:getLine1Number");
						logList.add("call class:"+localcl.getClass().toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}

				});

		XposedHelpers.findAndHookMethod(className, classLoader,
				"getSubscriberId", new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--get IMSI--");
						logList.add("function:getSubscriberId");
						logList.add("call class:"+localcl.getClass().toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}

				});
	}

}
