package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XSystem extends XHook{
	private static final String className = "java.lang.System";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XSystem classLoadHook;

	public static XSystem getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XSystem();
		}
		return classLoadHook;
	}

	@Override
	String getClassName() {
		return className;
	}

	@Override
	void hook(String pkgName, ClassLoader classLoader) {
		localpkgName = pkgName;
		logList = new ArrayList<String>();

		XposedHelpers.findAndHookMethod(className, classLoader, "loadLibrary",
				String.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--load so file--");
						logList.add("function:loadLibrary");
						logList.add("lib:" + param.args[0].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}

				});

		XposedHelpers.findAndHookMethod(className, classLoader, "currentTimeMillis",
				String.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// time to accelerate
						param.setResult((Long)param.getResult() + 24 * 60 * 60);
					}

				});
	}

}
