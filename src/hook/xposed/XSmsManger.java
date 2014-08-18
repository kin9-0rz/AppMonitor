package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;
import android.app.PendingIntent;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XSmsManger extends XHook {
	private static final String className = "android.telephony.SmsManager";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XSmsManger classLoadHook;
	
	public static XSmsManger getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XSmsManger();
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
				"sendTextMessage", String.class, String.class, String.class,
				PendingIntent.class, PendingIntent.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--sms sent--");
						logList.add("function:sendTextMessage");
						logList.add("target:" + param.args[0].toString());
						logList.add("text:" + param.args[2].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}

				});

		XposedHelpers.findAndHookMethod(className, classLoader,
				"sendMultipartTextMessage", String.class, String.class,
				ArrayList.class, ArrayList.class, ArrayList.class,
				new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--sms sent--");
						logList.add("function:sendMultipartTextMessage");
						logList.add("target:" + param.args[0].toString());
						logList.add("text:" + param.args[2].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}

				});

		XposedHelpers.findAndHookMethod(className, classLoader,
				"sendDataMessage", String.class, String.class, short.class,
				byte[].class, PendingIntent.class, PendingIntent.class,
				new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--sms sent--");
						logList.add("function:sendDataMessage");
						logList.add("target:" + param.args[0].toString());
						logList.add("text:" + param.args[3].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}

				});

	}

}
