package hook.xposed;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XHttpGet extends XHook {
	private static final String className = "org.apache.http.client.methods.HttpGet";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XHttpGet classLoadHook;

	public static XHttpGet getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XHttpGet();
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

		XposedHelpers.findAndHookConstructor(className, classLoader, URI.class,
				new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--http get--");
						logList.add("function:HttpGet");
						logList.add("url:" + param.args[0].toString());
						for (String log : logList) {
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName, logList);
						logList.clear();
					}
				});

		XposedHelpers.findAndHookConstructor(className, classLoader,
				String.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--http get--");
						logList.add("function:HttpGet");
						logList.add("url:" + param.args[0].toString());
						for (String log : logList) {
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName, logList);
						logList.clear();
					}

				});
	}

}
