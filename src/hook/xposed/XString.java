package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XString extends XHook {
	private static final String className = "java.lang.String";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XString classLoadHook;

	public static XString getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XString();
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
		XposedHelpers.findAndHookConstructor(className, classLoader, "String",
				String.class, new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--new String--");
						logList.add("function:String");
						logList.add("string:" + param.args[0].toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}

				});
	}

}
