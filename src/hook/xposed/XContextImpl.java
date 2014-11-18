package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XContextImpl extends XHook{

	private static final String className = "android.app.ContextImpl";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XContextImpl classLoadHook;

	public static XContextImpl getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XContextImpl();
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
		XposedHelpers.findAndHookMethod(className, classLoader, "registerReceiver",
				BroadcastReceiver.class, IntentFilter.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--register broadcastReceiver--");
						logList.add("function:registerReceiver");
						logList.add("Receiver Name:" + param.args[0].getClass().toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
	}

}
