package me.mikusjelly.amon.utils;

import android.util.Log;

import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class Logger {


    public static final String TAG = "AMON";
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_LOW = 1;
    public static final int LEVEL_MID = 2;
    public static final int LEVEL_HIGH = 3;

    public final static void log(String str) {
        if (str.startsWith("[*** ")) {  // High
            Log.e(TAG, str);
        } else if (str.startsWith("[### ")) { // Middle
            Log.w(TAG, str);
        } else if (str.startsWith("[=== ")) {   // Low
            Log.i(TAG, str);
        } else if (str.startsWith("[--- ")) { // Info
            Log.d(TAG, str);
        } else {
            Log.v(TAG, str);        // others
        }
    }

    public static boolean isWhite(String num) {
        String[] whiteNumber = {"10658422", "10658424"};
        List<String> strings = Arrays.asList(whiteNumber);

        return strings.contains(num);
    }

    public static boolean isFeeUrl(URL url) {
        String host = url.getHost();
        if (host.contains("cmgame") || host.contains("cmvideo") || host.contains("10086")) {
            return true;
        }

        return false;
    }

    public static String isFeeStr(String str) {
        if (str.contains("cmgame") || str.contains("cmvideo") ||
                str.contains("10086") || str.contains("106")
                || str.contains("fee") || str.contains("支付")) {
            return "***** FEE *****\n" + str;
        }

        return str;
    }

    public static void logCallRef(String prefix) {
    }

    public static void log(int level, String logMsg) {
        String callRef = "CallRef : " + Stack.getCallRef();
        switch (level) {
            case LEVEL_VERBOSE:    // verbose
                Log.d(Global.LOG_TAG, logMsg);
                Log.d(Global.LOG_TAG, callRef);
                break;
            case LEVEL_LOW:    // debug - low
                Log.i(Global.LOG_TAG, logMsg);
                Log.i(Global.LOG_TAG, callRef);
                break;
            case LEVEL_MID:    // info - middle
                Log.w(Global.LOG_TAG, logMsg);
                Log.w(Global.LOG_TAG, callRef);
                break;
            case LEVEL_HIGH:    // warn - high
                Log.e(Global.LOG_TAG, logMsg);
                Log.e(Global.LOG_TAG, callRef);
                break;
            default:
                Log.v(Global.LOG_TAG, logMsg);
                Log.v(Global.LOG_TAG, callRef);
        }
    }

}
