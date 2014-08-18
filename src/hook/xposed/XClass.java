package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XClass extends XHook {
	private static final String className = "java.lang.Class";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XClass classLoadHook;

	public static XClass getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XClass();
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
		XposedHelpers.findAndHookMethod(className, classLoader, "forName",
				String.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--class for name--");
						logList.add("function:Class.forName");
						logList.add("class:" + param.args[0].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
		
		XposedHelpers.findAndHookMethod(className, classLoader, "getMethod",
				String.class, Class[].class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--get method--");
						logList.add("function:getMethod");
						logList.add("method:" + param.args[0].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
		
		XposedHelpers.findAndHookMethod("java.lang.reflect.Method", classLoader, "invoke",
				Object.class, Object[].class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--invoke method--");
						logList.add("function:invoke");
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
	}

}
