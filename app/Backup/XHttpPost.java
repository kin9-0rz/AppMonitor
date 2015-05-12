package hook.xposed;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XHttpPost extends XHook {
	private static final String className = "org.apache.http.client.methods.HttpPost";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XHttpPost classLoadHook;

	public static XHttpPost getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XHttpPost();
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
						logList.add("action:--http post--");
						logList.add("function:HttpPost");
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
						logList.add("action:--http post--");
						logList.add("function:HttpPost");
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
