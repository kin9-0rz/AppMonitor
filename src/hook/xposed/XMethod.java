package hook.xposed;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XMethod extends XHook{
	private static final String className = "java.lang.reflect.Method";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XMethod classLoadHook;

	public static XMethod getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XMethod();
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
		XposedHelpers.findAndHookMethod(className, classLoader, "invoke",
				Object.class, Object[].class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						Method method = (Method)param.thisObject;
						Object[] arg = (Object[])param.args[1];
//						StringBuffer argsb = new StringBuffer();
//						for(int i = 0;i<arg.length;i++){
//							argsb.append(arg[i]).append(" ");
//						}
						logList.add("time:" + time);
						logList.add("action:--invoke method--");
						logList.add("function:invoke");
						logList.add("method name:" + method.getName());
						logList.add("args:" + arg.toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
	}

}
