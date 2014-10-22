package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XMediaRecorder extends XHook{
	
	private static final String className = "android.media.MediaRecorder";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XMediaRecorder classLoadHook;

	public static XMediaRecorder getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XMediaRecorder();
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
		XposedHelpers.findAndHookMethod(className, classLoader, "start", new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--start record--");
						logList.add("function:MediaRecorder.start");
						for(String log : logList){
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName,logList);
						logList.clear();
					}
				});
		
		XposedHelpers.findAndHookMethod(className, classLoader, "stop", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				String time = Util.getSystemTime();
				logList.add("time:" + time);
				logList.add("action:--stop record--");
				logList.add("function:MediaRecorder.stop");
				for(String log : logList){
					XposedBridge.log(log);
				}
				Util.writeLog(localpkgName,logList);
				logList.clear();
			}
		});
	}

}
