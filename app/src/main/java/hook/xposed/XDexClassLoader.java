package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XDexClassLoader extends XHook {
	private static final String className = DexClassLoader.class.getName();
	private static List<String> logList = null;
	private static XDexClassLoader classLoadHook;

	public static XDexClassLoader getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XDexClassLoader();
		}
		return classLoadHook;
	}

	@Override
	void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
		logList = new ArrayList<String>();
		XposedHelpers.findAndHookConstructor(className, packageParam.classLoader,
				String.class, String.class, String.class,
				ClassLoader.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String dexPath = (String) param.args[0];
						String optimizedDir = (String) param.args[1];
						String libPath = (String) param.args[2];
						ClassLoader parent = (ClassLoader) param.args[3];
						String callRef = Stack.getCallRef();

						Logger.log("[--- DexClassLoader ---] ");
						Logger.log("[--- DexClassLoader ---] dexPath : " + dexPath);
						Logger.log("[--- DexClassLoader ---] optimizedDir : " + optimizedDir);
						Logger.log("[--- DexClassLoader ---] libPath : " + libPath);
						Logger.log("[--- DexClassLoader ---] parent : " + parent);
						Logger.log("[--- DexClassLoader ---] " + callRef);

						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--load dex--");
						logList.add("function:DexClassLoader");
						logList.add("dex path:" + dexPath);
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(packageParam.packageName,logList);
						logList.clear();
					}
				});

	}

}
