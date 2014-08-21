package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XDexClassLoader extends XHook {
	private static final String className = "dalvik.system.DexClassLoader";
	private static String localpkgName = null;
	private static ClassLoader localcl = null;
	private static List<String> logList = null;
	private static XDexClassLoader classLoadHook;

	public static XDexClassLoader getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XDexClassLoader();
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
		localcl = classLoader;
		logList = new ArrayList<String>();
		XposedHelpers.findAndHookConstructor(className, classLoader,
				String.class, String.class, String.class,
				ClassLoader.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--load dex--");
						logList.add("function:DexClassLoader");
						logList.add("dex path:" + param.args[0].toString());
						logList.add("call class:"+localcl.getClass().toString());
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
	}

}
