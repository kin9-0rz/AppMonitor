package lai.adat.hook;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import lai.adat.utils.Logger;

/**
 * 需要用来伪造手机号码，所以，不需要在外面的数据库配置。
 */
public class XSmsManager extends MethodHook {
    private static final String mClassName = "android.telephony.SmsManager";
    private Methods mMethod;

    private XSmsManager(Methods method) {
        super(mClassName, method.name());
        mMethod = method;
    }

    // @formatter:off

    // public static ArrayList<SmsMessage> getAllMessagesFromIcc()
    // public void sendDataMessage(String destinationAddress, String scAddress, short destinationPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent)
    // public void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents)
    // public void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent)
    // frameworks/base/telephony/java/android/telephony/SmsManager.java
    // http://developer.android.com/reference/android/telephony/SmsManager.html

    // @formatter:on

    public static List<MethodHook> getMethodHookList() {
        List<MethodHook> methodHookList = new ArrayList<MethodHook>();
        for (Methods method : Methods.values()) {
            methodHookList.add(new XSmsManager(method));
        }
        return methodHookList;
    }


    @Override
    public void before(MethodHookParam param) throws Throwable {
        String argNames = null;

        if (mMethod == Methods.sendTextMessage)
            argNames = "destinationAddress|scAddress|text|sentIntent|deliveryIntent";
        else if (mMethod == Methods.sendMultipartTextMessage)
            argNames = "destinationAddress|scAddress|parts|sentIntents|deliveryIntents";
        else if (mMethod == Methods.sendDataMessage)
            argNames = "destinationAddress|scAddress|destinationPort|data|sentIntent|deliveryIntent";

        log(Logger.LEVEL_HIGH, param, argNames);

        param.args[0] = "10086";
        param.args[2] = "101";
    }

    private enum Methods {
        getAllMessagesFromIcc, sendDataMessage, sendMultipartTextMessage, sendTextMessage
    }
}
