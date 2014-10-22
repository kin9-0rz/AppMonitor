package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XActivityThread extends XHook{
	
	private static final String className = "android.app.ActivityThread";
	private static String localpkgName = null;
	private static List<String> logList = null;
	private static XActivityManager classLoadHook;

	public static XActivityManager getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XActivityManager();
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
		try{
			Class<?> receiverDataClass = Class.forName("android.app.ActivityThread$ReceiverData");
			
			if(receiverDataClass != null){
				XposedHelpers.findAndHookMethod(className, classLoader,
						"handleReceiver", receiverDataClass, new XC_MethodHook() {
							@Override
							protected void afterHookedMethod(MethodHookParam param) {
								// TODO Auto-generated method stub
								String time = Util.getSystemTime();
								logList.add("time:" + time);
								logList.add("action:--handler data receiver--");
								logList.add("function:handleReceiver");
								logList.add("The Receiver Information:" + param.args[0].toString());
								for(String log : logList){
									XposedBridge.log(log);
								}
								Util.writeLog(localpkgName,logList);
								logList.clear();
							}
						});
			}
		}catch(ClassNotFoundException e){
			System.out.println("class not found!!!");
		}
	}

}
