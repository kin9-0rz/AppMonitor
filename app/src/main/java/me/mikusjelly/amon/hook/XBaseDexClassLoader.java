package me.mikusjelly.amon.hook;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import me.mikusjelly.amon.utils.Logger;

public class XBaseDexClassLoader extends MethodHook {
    private static final String mClassName = "dalvik.system.BaseDexClassLoader";

    private XBaseDexClassLoader(Methods method, boolean logTrace) {
        super(mClassName, null);
    }

    // @formatter:off
    // public BaseDexClassLoader(String	dexPath,File optimizedDirectory, String	libraryPath, ClassLoader parent)
    // libcore/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
    // http://developer.android.com/reference/dalvik/system/BaseDexClassLoader.html
    // @formatter:on

    public static List<MethodHook> getMethodHookList() {
        List<MethodHook> methodHookList = new ArrayList<MethodHook>();
        for (Methods method : Methods.values())
            methodHookList.add(new XBaseDexClassLoader(method, true));

        return methodHookList;
    }

    public void after(MethodHookParam param) throws Throwable {
        String argNames = "dexPath|optimizedDirectory|libraryPath|parent";
        log(Logger.LEVEL_MID, param, argNames);
    }

    private enum Methods {
        BaseDexClassLoader
    }
}
