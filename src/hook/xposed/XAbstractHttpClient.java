package hook.xposed;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import util.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XAbstractHttpClient extends XHook {
	private static final String className = "org.apache.http.impl.client.AbstractHttpClient";
	private static String localpkgName = null;
	private static ClassLoader localcl = null;
	private static List<String> logList = null;
	private static XAbstractHttpClient classLoadHook;

	public static XAbstractHttpClient getInstance() {
		if (classLoadHook == null) {
			classLoadHook = new XAbstractHttpClient();
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
		localcl = classLoader;
		XposedHelpers.findAndHookMethod(className, classLoader, "execute",
				HttpHost.class, HttpRequest.class, HttpContext.class,
				new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) {
						// TODO Auto-generated method stub
						String time = Util.getSystemTime();
						logList.add("time:" + time);
						logList.add("action:--executed--");
						logList.add("function:execute");
						logList.add("url:" + param.args[0].toString());
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
