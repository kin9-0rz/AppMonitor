package funnyparty.appmonitor.hook;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import funnyparty.appmonitor.utils.MethodParser;
import funnyparty.appmonitor.utils.Logger;

public abstract class MethodHook {
    private String className;
    private String methodName;

    protected MethodHook(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void before(MethodHookParam param) throws Throwable {
    }

    public void after(MethodHookParam param) throws Throwable {
    }

    protected void log(int level, MethodHookParam param, String argNames) {
        String[] argNamesArray = null;
        if (argNames != null)
            argNamesArray = argNames.split("\\|");

        String formattedArgs = MethodParser.parseMethodArgs(param, argNamesArray);
        if (formattedArgs == null)
            formattedArgs = "";

        String logMsg = String.format("=== %s->%s === %s", className, methodName, formattedArgs);
        Logger.log(level, logMsg);
    }
}
