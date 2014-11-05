package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XApplicationPackageManager extends XHook {

	private static final String className = "android.app.ApplicationPackageManager";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XApplicationPackageManager classLoadHook;

	public static XApplicationPackageManager getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XApplicationPackageManager();
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
		XposedHelpers.findAndHookMethod(className, classLoader,
				"setComponentEnabledSetting", ComponentName.class, int.class,
				int.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--set icon disable or enable--");
						logList.add("function:setComponentEnabledSetting");
						// ComponentName cn = (ComponentName) param.args[0];
						// String cnName = cn.getPackageName() + "/" +
						// cn.getClassName();
						logList.add("componetName:" + param.args[0]);
						
						int state = (Integer) param.args[1];
						if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
							logList.add("action:--COMPONENT_ENABLED_STATE_DISABLED--");
						if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
							logList.add("action:--COMPONENT_ENABLED_STATE_ENABLED--");
						if (state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
							logList.add("action:--COMPONENT_ENABLED_STATE_DEFAULT--");
						
						for (String log : logList) {
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName, logList);
						logList.clear();
					}
				});
		
		
	}
}
