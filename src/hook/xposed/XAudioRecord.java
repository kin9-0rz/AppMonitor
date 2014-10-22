package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XAudioRecord extends XHook {

	private static final String className = "android.media.AudioRecord";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XAudioRecord classLoadHook;

	public static XAudioRecord getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XAudioRecord();
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
				"startRecording", new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--start record--");
						logList.add("function:startRecordinge");
						for (String log : logList) {
							XposedBridge.log(log);
						}
						Util.writeLog(localpkgName, logList);
						logList.clear();
					}
				});
	}

}
