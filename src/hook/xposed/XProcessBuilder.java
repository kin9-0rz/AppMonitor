package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XProcessBuilder extends XHook {

	private static final String className = "java.lang.ProcessBuilder";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XProcessBuilder classLoadHook;

	public static XProcessBuilder getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XProcessBuilder();
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

		XposedHelpers.findAndHookMethod(className, classLoader, "start",
				new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--Create New Process--");
						logList.add("function:ProcessBuilder.start");
						ProcessBuilder pb = (ProcessBuilder) param.thisObject;
						List<String> cmds = pb.command();
						StringBuilder sb = new StringBuilder();
						sb.append(sb.append("CMD:"));
						for(int i=0 ;i <cmds.size(); i++){
							sb.append(cmds.get(i)+" ");
						}
						logList.add(sb.toString());
						for (String log : logList) {
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName, logList);
						logList.clear();
					}
				});
	}

}
