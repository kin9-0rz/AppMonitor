package me.mikusjelly.amon.xposed;


import android.content.pm.ApplicationInfo;
import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import me.mikusjelly.amon.hook.MethodHook;
import me.mikusjelly.amon.hook.XActivity;
import me.mikusjelly.amon.hook.XBaseDexClassLoader;
import me.mikusjelly.amon.hook.XCipher;
import me.mikusjelly.amon.hook.XIoBridge;
import me.mikusjelly.amon.hook.XSecretKeySpecHook;
import me.mikusjelly.amon.hook.XSmsManager;
import me.mikusjelly.amon.hook.XThread;
import me.mikusjelly.amon.hook.XViewGroup;
import me.mikusjelly.amon.utils.Global;
import me.mikusjelly.amon.utils.LogWriter;
import me.mikusjelly.amon.utils.MethodParser;
import me.mikusjelly.amon.utils.Util;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class Launcher implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private static XC_MethodHook xcMethodHookApp = null;
    private static XC_MethodHook xcMethodHookSystem = null;

    // Api config pattern, validated one is like Lcom/example/class;->Fun(Ljava/lang/String;)Ljava/lang/String;
    private static Pattern apiConfigPattern = Pattern.compile("^L.*(;->.*\\(.*\\).*)?$");
    private static Set<String> apiSet;
    private static HashMap<String, Integer> apiMap;
    private Set<String> pkgs;


    // Hook customized app apis
    // 如果需要hook特定应用的api需要放到对应到应用到目录？从下面到代码上看，没看到有什么有意义到东西。
    private static void hookCustomizedAppApis(String packageName, ClassLoader classLoader) {
        File appApiConfigFile = new File(String.format("/data/data/%s/%s",
                packageName, Global.APP_API_HOOK_CONFIG));
        if (!appApiConfigFile.exists())
            return;

//        Util.APP_API_NUM = getAppApiNumLimit();
        Global.APP_UN_HOOKED_APIS = readApiConfig(appApiConfigFile.getAbsolutePath(), Global.APP_API_NUM);
        ArrayList<String> tmpUnHookedApis = Util.copyArrayList(Global.APP_UN_HOOKED_APIS);

        for (String methodInfo : tmpUnHookedApis) {
            Log.d(Global.LOG_TAG, "hook customized app apis:" + methodInfo);
            if (hookMethodInfo(methodInfo, classLoader, Global.HOOK_APP_API))
                Global.APP_UN_HOOKED_APIS.remove(methodInfo);
        }
    }

    private static void hookAll(List<MethodHook> methodHookList) {
        for (MethodHook methodHook : methodHookList)
            hook(methodHook);
    }

    private static void hook(MethodHook methodHook) {
        hook(methodHook, null);
    }

    private static void hook(final MethodHook methodHook, ClassLoader classLoader) {
        try {
            // Create hook method
            XC_MethodHook xcMethodHook = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    try {
                        methodHook.before(param);
                    } catch (Throwable ignore) {
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (!param.hasThrowable())
                        try {
                            methodHook.after(param);
                        } catch (Throwable ignore) {
                        }
                }
            };

            // Find hook class
            Class<?> hookClass = null;
            try {
                hookClass = findClass(methodHook.getClassName(), classLoader);
            } catch (ClassNotFoundError ignore) {
            }

            if (hookClass == null) {
                String message = String.format("Hook-Class not found: %s", methodHook.getClassName());
                Log.d(Global.LOG_TAG, message);
                return;
            }

            // Add hook
            if (methodHook.getMethodName() == null) {
                for (Constructor<?> constructor : hookClass.getDeclaredConstructors()) {
                    XposedBridge.hookMethod(constructor, xcMethodHook);
                }
            } else {
                for (Method method : hookClass.getDeclaredMethods())
                    if (method.getName().equals(methodHook.getMethodName()))
                        XposedBridge.hookMethod(method, xcMethodHook);
            }

        } catch (Throwable ex) {
            XposedBridge.log(ex);
        }
    }

    // Hook customized methods
    private static ArrayList<String> readApiConfig(String filePath, int apiNumLimit) {
        ArrayList<String> apiConfigList = new ArrayList<String>();
        BufferedReader reader = null;
        int apiCount = 0;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String data = null;
            while ((data = reader.readLine()) != null) {
                if (apiCount >= apiNumLimit)
                    break;
                if (isApiConfigValidated(data)) {
                    apiConfigList.add(data);
                    apiCount += 1;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return apiConfigList;
    }

    private static boolean isApiConfigValidated(String data) {
        Matcher matcher = apiConfigPattern.matcher(data);
        return matcher.matches();
    }

    // Hook constructors for customized API configure
    private static void hookCustomizeConstructors(Class<?> hookClass, String[] methodInfoItems, String hookType) {
        String parameterTypes = methodInfoItems[2];
        for (Constructor<?> constructor : hookClass.getDeclaredConstructors()) {
            try {
                boolean match = true;
                String parsedParameterTypes = Util.parseParameterTypes(constructor);
                if (parameterTypes != null)
                    if (parameterTypes.equals(parsedParameterTypes))
                        match = true;
                    else
                        match = false;
                if (match) {
                    if (hookType.equals(Global.HOOK_APP_API))
                        XposedBridge.hookMethod(constructor, xcMethodHookApp);
                    else if (hookType.equals(Global.HOOK_SYSTEM_API))
                        XposedBridge.hookMethod(constructor, xcMethodHookSystem);
                }

            } catch (Exception ex) {
                XposedBridge.log(ex);
            }
        }
    }

    /**
     * Convert class name in dalvik format to java format
     *
     * @param classNameDalvik Lcom/example/class;
     * @return com.example.class
     */
    private static String convertClassName(String classNameDalvik) {
        String className = classNameDalvik.substring(1);
        className = className.replace("/", ".");
        return className;
    }

    private static void log(MethodHookParam param, String hookType) {
        String argsValue = MethodParser.parseParameters(param);

        if (argsValue.contains("/amon/")) {
            return;
        }

        String returnValue = MethodParser.parseReturnValue(param);
        String className = null;
        String methodName = null;

        if (param.method instanceof Constructor) {
            Constructor<?> constructor = (Constructor<?>) param.method;
            className = constructor.getDeclaringClass().getName();
            methodName = constructor.getName();
        } else if (param.method instanceof Method) {
            Method method = (Method) param.method;
            className = method.getDeclaringClass().getName();
            methodName = method.getName();
        }

        className = String.format("L%s", className.replace(".", "/"));
        String logMsg = String.format("{\"Func\": \"%s;->%s\", \"parameters\":[%s], \"return\":[%s], ",
                className, methodName, argsValue, returnValue);

        LogWriter.logStack(logMsg);
    }

    public static void hookMethodInfo(String methodInfo, String hookType) {
        hookMethodInfo(methodInfo, null, hookType);
    }

    /**
     * Lcom/example/class;->Fun(Ljava/lang/String;)Ljava/lang/String;
     * Lcom/example/class;->fun
     *
     * @param methodInfo
     * @param classLoader
     * @param hookType
     * @return
     */
    public static boolean hookMethodInfo(String methodInfo, ClassLoader classLoader, String hookType) {
        boolean methodHooked = false;
        String[] methodInfoItems = parseMethodInfo(methodInfo);

        String className = methodInfoItems[0];
        String methodName = methodInfoItems[1];
        Class<?> hookClass = null;

//        if (className != null) {
            try {
                hookClass = findClass(className, classLoader);
            } catch (ClassNotFoundError ignore) {
                XposedBridge.log(ignore);
            }

            if (hookClass != null && methodName != null) {
                String shortClassName = className;
                if (className.lastIndexOf(".") != -1) {
                    shortClassName = className.substring(className.lastIndexOf(".") + 1);
                }

                if (shortClassName.equals(methodName)) {
                    hookCustomizeConstructors(hookClass, methodInfoItems, hookType);
                    methodHooked = true;
                } else {
                    hookMethods(hookClass, methodInfoItems, hookType);
                    methodHooked = true;
                }
            }
//        } else {
//        }

        return methodHooked;
    }

    /**
     * @param methodInfo Lcom/example/class;->Fun(Ljava/lang/String;)Ljava/lang/String;
     * @return {class name, method name, parameters, return type} <br/>
     * {com.example.class, Fun, string, string}
     */
    private static String[] parseMethodInfo(String methodInfo) {
        String[] methodInfoItems = new String[]{null, null, null, null};
        String[] methodClassSigItems = methodInfo.split(";->");

        if (methodClassSigItems.length == 2) {
            String className = methodClassSigItems[0];
            methodInfoItems[0] = convertClassName(className);

            String methodSignature = methodClassSigItems[1];
            int leftParIndex = methodSignature.indexOf("(");
            if (leftParIndex != -1) {
                methodInfoItems[1] = methodSignature.substring(0, leftParIndex).trim();
                int rightParIndex = methodSignature.indexOf(")");
                if (rightParIndex != -1) {
                    methodInfoItems[2] = methodSignature.substring(leftParIndex + 1, rightParIndex).trim();
                    if (rightParIndex + 1 < methodSignature.length()) {
                        methodInfoItems[3] = methodSignature.substring(rightParIndex + 1).trim();
                    }
                }

            } else {
                methodInfoItems[1] = methodSignature.trim();
            }
        } else if (methodClassSigItems.length == 1) {
            String className = methodClassSigItems[0];
            methodInfoItems[0] = convertClassName(className);
        }

        return methodInfoItems;
    }

    private static void hookMethods(Class<?> hookClass, String[] methodInfoItems, String hookType) {
        String methodName = methodInfoItems[1];
        String parameterTypes = methodInfoItems[2];
        String returnType = methodInfoItems[3];

        for (Method method : hookClass.getDeclaredMethods()) {
            try {
                boolean match = false;
                if (method.getName().equals(methodName)) {
                    match = true;
                    String parsedParameterTypes = Util.parseParameterTypes(method);
                    String parsedReturnType = Util.parseReturnType(method);
                    if (parameterTypes != null)
                        if (parameterTypes.equals(parsedParameterTypes))
                            match = true;
                        else
                            match = false;
                    if (returnType != null)
                        if (returnType.equals(parsedReturnType))
                            match = true;
                        else
                            match = false;

                    if (match) {
                        if (hookType.equals(Global.HOOK_APP_API))
                            XposedBridge.hookMethod(method, xcMethodHookApp);
                        else if (hookType.equals(Global.HOOK_SYSTEM_API))
                            XposedBridge.hookMethod(method, xcMethodHookSystem);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void initZygote(StartupParam startupParam) throws Throwable {
        XposedBridge.log("initZygote");
        XposedBridge.log("modulePath:" + startupParam.modulePath);

        XSharedPreferences apis = new XSharedPreferences(Global.SELF_PACKAGE_NAME, Global.SHARED_PREFS_APIS);
        apiSet = apis.getStringSet("system_api", null);

        apiMap = new HashMap<>();
        for (String api : apiSet) {
            String key = api.substring(1);
            int level = Integer.valueOf(api.substring(0, 1));
            apiMap.put(key, level);
            XposedBridge.log(key);
        }


    }

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        ApplicationInfo appInfo = lpparam.appInfo;
        if (appInfo == null) {
            return;
        }

        XSharedPreferences pre = new XSharedPreferences(Global.SELF_PACKAGE_NAME, Global.SHARED_PREFS_HOOK_PACKAGE);
        pkgs = pre.getStringSet("pkgs", null);

        if (pkgs == null) {
            return;
        }

        if (!pkgs.contains(lpparam.packageName)) {
            return;
        }

        XposedBridge.log(pkgs.toString());


        xcMethodHookApp = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!param.hasThrowable())
                    try {
                        if (Process.myUid() <= 0)
                            return;
                        log(param, Global.HOOK_APP_API);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
            }
        };

        xcMethodHookSystem = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log(param, Global.HOOK_SYSTEM_API);
            }
        };

        hookBuIldInAPI();
        hookDatabaseAPI();

//        hookCustomizedAppApis(appInfo.packageName, lpparam.classLoader);
    }



    private void hookBuIldInAPI() {
        hookAll(XThread.getMethodHookList());
        hookAll(XActivity.getMethodHookList());
        hookAll(XBaseDexClassLoader.getMethodHookList());
        hookAll(XCipher.getMethodHookList());
        hookAll(XIoBridge.getMethodHookList());
        hookAll(XSecretKeySpecHook.getMethodHookList());
        hookAll(XSmsManager.getMethodHookList());
        hookAll(XViewGroup.getMethodHookList());
    }


    private static void hookDatabaseAPI() {
        for (String methodInfo : apiSet) {
            hookMethodInfo(methodInfo.substring(1), Global.HOOK_SYSTEM_API);
        }
    }


}