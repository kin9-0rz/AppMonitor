package funnyparty.appmonitor.utils;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Globle {
    public static final String SELF_PACKAGE_NAME = "funnyparty.appmonitor";
    public static final String LOG_TAG = "AppMonitor";
    public static final String APP_API_HOOK_CONFIG = "app_apis.config";
    public static final String HOOK_SYSTEM_API = "system_api";
    public static final String HOOK_APP_API = "app_api";
    public static int APP_API_NUM = 500;
    public static ArrayList<String> APP_UN_HOOKED_APIS = new ArrayList<String>();
    public static Class<?> pathConvertorClass = null;
    public static Method logFilePathMethod = null;

    public static int DATA_BYTES_TO_LOG = 768;


}


