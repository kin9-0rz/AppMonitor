package me.mikusjelly.amon.hook;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import me.mikusjelly.amon.utils.Global;

/**
 * Created by bin on 1/29/16.
 */
public class XThread  extends MethodHook {

    private static final String mClassName = "java.lang.Thread";
    private Methods mMethod;

    private XThread(Methods method) {
        super(mClassName, method.name());
        mMethod = method;
    }

    @Override
    public void before(XC_MethodHook.MethodHookParam param) throws Throwable {
        if (mMethod == Methods.getStackTrace) {
//            StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
            StackTraceElement[] traceElements = new Throwable().getStackTrace();
            if (traceElements == null) {
                return;
            }

            int num = 0;
            for (StackTraceElement traceElement : traceElements) {
                String str = traceElement.toString();
                if (str.startsWith("de.robv") || str.startsWith(Global.SELF_PACKAGE_NAME)) {
                    num++;
                }
            }

            StackTraceElement[] newTraceElement = new StackTraceElement[traceElements.length - num];
            for (int i = 0, j =0 ; i < traceElements.length; i++) {
                String str = traceElements[i].toString();
                if (str.startsWith("de.robv") || str.startsWith(Global.SELF_PACKAGE_NAME)) {
                    continue;
                }

                newTraceElement[j++] = traceElements[i];
            }

            param.setResult(newTraceElement);

        }
    }

    public static List<MethodHook> getMethodHookList() {
        List<MethodHook> methodHookList = new ArrayList<MethodHook>();
        for (Methods method : Methods.values()) {
            methodHookList.add(new XThread(method));
        }
        return methodHookList;
    }

    private enum Methods {
        getStackTrace
    }
}
