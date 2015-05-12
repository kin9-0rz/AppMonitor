package hook.xposed;

import android.content.ContentValues;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XContentResolver extends XHook {

    private static final String className = "android.content.ContentResolver";
    private static final String[] privacyUris = {
            "content://com.android.contacts",
            "content://sms",
            "content://mms-sms",
            "content://contacts/",
            "content://com.android.contacts",
            "content://call_log",
            "content://telephony",
            "content://browser/bookmarks"};
    private static List<String> logList = null;

    private static XContentResolver xContentResolver;

    public static XContentResolver getInstance() {
        if (xContentResolver == null) {
            xContentResolver = new XContentResolver();
        }
        return xContentResolver;
    }

    private boolean isUriAvailable(String uri) {
        String url = uri.toLowerCase();
        for (int i = 0; i < privacyUris.length; i++) {
            if (url.contains(privacyUris[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "query",
                Uri.class, String[].class, String.class, String[].class,
                String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String uri = param.args[0].toString();
                        String selection = "";
                        if (param.args[2] != null) {
                            selection = (String) param.args[2];
                        }
                        String callRef = Stack.getCallRef();

                        if (isUriAvailable(uri)) {
                            String time = Util.getSystemTime();

                            Logger.log("[### ContentResolver query ###]");
                            Logger.log("[### ContentResolver query ###] " + uri);
                            if (selection != null){
                                Logger.log("[### ContentResolver query ###] " + selection);
                            }
                            Logger.log("[### ContentResolver query ###] " + callRef);

                            logList.add("time:" + time);
                            logList.add("action:--query database--");
                            logList.add("function:query");
                            logList.add("Uri:" + uri.toString());
                            logList.add("selection:" + param.args[2]);

                            for (String log : logList) {
                                XposedBridge.log(log);
                            }
                            Util.writeLog(packageParam.packageName, logList);
                            logList.clear();
                        } else {
                            Logger.log("[=== ContentResolver query ===]");
                            Logger.log("[=== ContentResolver query ===] " + uri);
                            if (selection != null){
                                Logger.log("[=== ContentResolver query ===] " + selection);
                            }
                            Logger.log("[=== ContentResolver query ===] " + callRef);
                        }


                    }
                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "insert",
                Uri.class, ContentValues.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String uri = (String) param.args[0];
                        String value = (String) param.args[1];
                        String callRef = Stack.getCallRef();

                        if (isUriAvailable(uri)) {
                            String time = Util.getSystemTime();

                            Logger.log("[### ContentResolver query ###]");
                            Logger.log("[### ContentResolver query ###] " + uri);
                            Logger.log("[### ContentResolver query ###] " + value);
                            Logger.log("[### ContentResolver query ###] " + callRef);

                            logList.add("time:" + time);
                            logList.add("action:--add data to database--");
                            logList.add("function:insert");
                            logList.add("Uri:" + uri);
                            logList.add("value:" + value);
                            for (String log : logList) {
                                XposedBridge.log(log);
                            }
                            Util.writeLog(packageParam.packageName, logList);
                        } else {
                            Logger.log("[=== ContentResolver query ===]");
                            Logger.log("[=== ContentResolver query ===] " + uri);
                            Logger.log("[=== ContentResolver query ===] " + value);
                            Logger.log("[=== ContentResolver query ===] " + callRef);
                        }
                        logList.clear();
                    }
                });

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "delete",
                Uri.class, String.class, String[].class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String uri = (String) param.args[0];
                        String where = (String) param.args[1];
                        String selection = (String) param.args[2];
                        String callRef = Stack.getCallRef();

                        if (isUriAvailable(uri)) {
                            String time = Util.getSystemTime();

                            Logger.log("[### ContentResolver query ###]");
                            Logger.log("[### ContentResolver query ###] " + uri);
                            Logger.log("[### ContentResolver query ###] " + where);
                            Logger.log("[### ContentResolver query ###] " + selection);
                            Logger.log("[### ContentResolver query ###] " + callRef);

                            logList.add("time:" + time);
                            logList.add("action:--delete data from database--");
                            logList.add("function:delete");
                            logList.add("Uri:" + uri.toString());
                            logList.add("where:" + param.args[1].toString());
                            logList.add("selectionArgs:" + param.args[2].toString());
                            for (String log : logList) {
                                XposedBridge.log(log);
                            }
                            Util.writeLog(packageParam.packageName, logList);
                            logList.clear();
                        } else {
                            Logger.log("[=== ContentResolver query ===]");
                            Logger.log("[=== ContentResolver query ===] " + uri);
                            Logger.log("[=== ContentResolver query ===] " + where);
                            Logger.log("[=== ContentResolver query ===] " + selection);
                            Logger.log("[=== ContentResolver query ===] " + callRef);
                        }

                    }
                });
    }

}
