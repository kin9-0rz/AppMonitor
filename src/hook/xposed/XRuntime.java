package hook.xposed;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XRuntime extends XHook {

	private static final String className = "java.lang.Runtime";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XRuntime classLoadHook;

	public static XRuntime getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XRuntime();
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
		XposedHelpers.findAndHookMethod(className, classLoader, "exec",
				String[].class, String[].class, File.class,
				new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						String[] prog = (String[]) param.args[0];
						logList.add("time:" + time);
						logList.add("action:--Create New Process--");
						logList.add("function:exec");
						for (String str : prog) {
							logList.add("command:" + str);
						}
						for (String log : logList) {
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName, logList);
						logList.clear();
					}
				});
	}

}
