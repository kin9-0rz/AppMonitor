package lai.adat.utils;


import java.lang.reflect.Method;
import java.util.ArrayList;

public class Global {
    public static final String SELF_PACKAGE_NAME = "lai.adat";
    public static final String LOG_TAG = "AppMonitor";

//    API Configure
    public static final String APP_API_HOOK_CONFIG = "app_apis.config";
    public static final String HOOK_SYSTEM_API = "system_api";
    public static final String HOOK_APP_API = "app_api";

    //
    public static final String SHARED_PREFS_HOOK_PACKAGE = "pkgs";
    public static final String SHARED_PREFS_SETTING = "setting";
    public static final String SHARED_PREFS_APIS = "apis";


    public static int APP_API_NUM = 500;
    public static ArrayList<String> APP_UN_HOOKED_APIS = new ArrayList<String>();
    public static Class<?> pathConvertorClass = null;
    public static Method logFilePathMethod = null;

    public static int DATA_BYTES_TO_LOG = 768;


}


