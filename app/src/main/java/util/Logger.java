package util;

import android.util.Log;

import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class Logger {


    private static final String TAG = "AppMonitor";

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


}
