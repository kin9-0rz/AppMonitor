package hook.xposed;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import util.Util;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XFlie extends XHook {
	private static final String className = "java.io.File";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XFlie classLoadHook;
	
	public static XFlie getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XFlie();
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

		XposedHelpers.findAndHookConstructor(className, classLoader,
				File.class, String.class, new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param){
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--new file--");
						logList.add("function:file");
						logList.add("file dir:" + param.args[0].toString());
						logList.add("file name:" + param.args[1].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
		
		XposedHelpers.findAndHookConstructor(className, classLoader,
				String.class, String.class, new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param){
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--new file--");
						logList.add("function:file");
						logList.add("file dir:" + param.args[0].toString());
						logList.add("file name:" + param.args[1].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
		
		XposedHelpers.findAndHookConstructor(className, classLoader,
				String.class, new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param){
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--new file--");
						logList.add("function:file");
						logList.add("file path:" + param.args[0].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
		
		XposedHelpers.findAndHookConstructor(className, classLoader,
				URI.class, new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param){
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--new file--");
						logList.add("function:file");
						logList.add("file path:" + param.args[0].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
	}

}
