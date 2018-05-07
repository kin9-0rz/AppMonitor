package me.mikusjelly.amon.utils;

public class Stack {
    public static String getCallRef() {
        StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
        String value = "";
        boolean isFirst = true;
        int i;
        for (i = 0; i < traceElements.length; ++i) {

            String elements = traceElements[i].toString();
            if (!elements.startsWith(Global.SELF_PACKAGE_NAME)
                    && !elements.startsWith("dalvik.")
                    && !elements.startsWith("java.lang")
                    && !elements.startsWith("de.robv")
                    && !elements.startsWith("android.app")
                    && !elements.startsWith("android.os")) {

                if (isFirst) {
                    value = String.valueOf(elements.substring(0, elements.indexOf("(")));
                    isFirst = false;
                    continue;
                }

                value = value + " <- " + String.valueOf(elements.substring(0, elements.indexOf("(")));
            }
        }

        return value;
    }
}
