package hook.xposed;

import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XSmsMessage extends XHook {
    private static final String className = "android.telephony.SmsMessage";
    private static List<String> logList = null;
    private static XSmsMessage xSmsMessage;

    public static XSmsMessage getInstance() {
        if (xSmsMessage == null) {
            xSmsMessage = new XSmsMessage();
        }
        return xSmsMessage;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        logList = new ArrayList<String>();
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader,
                "createFromPdu", byte[].class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        String time = Util.getSystemTime();
                        SmsMessage smsMessage = (SmsMessage) param.getResult();
                        String addr = smsMessage.getDisplayOriginatingAddress();
                        String body = smsMessage.getDisplayMessageBody();
                        String callRef = Stack.getCallRef();

                        Logger.log("[### Read SMS ###]");
                        Logger.log("[### Read SMS ###] " + addr);
                        Logger.log("[### Read SMS ###] " + body);
                        Logger.log("[### Read SMS ###] " + callRef);

                        logList.add("time:" + time);
                        logList.add("action:--receive sms--");
                        logList.add("adress:" + smsMessage.getDisplayOriginatingAddress());
                        logList.add("body:" + smsMessage.getDisplayMessageBody());
                        for (String log : logList) {
                            XposedBridge.log(log);
                        }
                        Util.writeLog(packageParam.packageName, logList);
                        logList.clear();
                    }
                });
    }

}
