package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XString extends XHook {
	private static final String className = "java.lang.String";
	private static List<String> logList = null;
	private static XString classLoadHook;

	public static XString getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XString();
		}
		return classLoadHook;
	}

	@Override
	void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
		logList = new ArrayList<String>();
		XposedHelpers.findAndHookConstructor(className, packageParam.classLoader,
				String.class, new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String str = param.args[0].toString();
						String callRef = Stack.getCallRef();

						Logger.log("[- String -] ");
						Logger.log("[- String -] " + str);
						Logger.log("[- String -] " + callRef);

//						String time = Util.getSystemTime();
//						logList.add("time:" + time);
//						logList.add("action:--new String--");
//						logList.add("function:String");
//						logList.add("string:" + str);

						//TO FIXED java.util.ConcurrentModificationException
						// TODO java.util.ConcurrentModificationException
//						for (final String log : logList) {
//							XposedBridge.log(log);
//						}
//						Util.writeLog(packageParam.packageName, logList);
//						logList.clear();
					}

				});
	}

}
