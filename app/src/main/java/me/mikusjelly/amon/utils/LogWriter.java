package me.mikusjelly.amon.utils;


import android.os.Environment;
import android.os.Process;
import android.util.Log;
import android.util.StateSet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.robv.android.xposed.XposedBridge;
import me.mikusjelly.amon.BuildConfig;

public class LogWriter {
    private static final String DEBUG_TAG = "AMON";
    public static boolean isDebug = BuildConfig.DEBUG_MODE;
    public static boolean isWriteToLog = false;


    public static void setDebug(boolean isDebug) {
        LogWriter.isDebug = isDebug;
    }

    public static void setWriteToLog(boolean isWriteToLog) {
        LogWriter.isWriteToLog = isWriteToLog;
    }

    public static void d(final Object thiz, final String msg) {
        if (LogWriter.isDebug) {
            Log.d(LogWriter.DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void d(final String msg) {
        if (LogWriter.isDebug) {
            Log.d(LogWriter.DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void d(final String DEBUG_TAG, final Object thiz, final String msg) {
        if (LogWriter.isDebug) {
            Log.d(DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(DEBUG_TAG, msg);
        }
    }

    public static void d(final String DEBUG_TAG, final String msg) {
        if (LogWriter.isDebug) {
            Log.d(DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(DEBUG_TAG, msg);
            // LogWriter.d(DEBUG_TAG, msg);
        }
    }

    public static void debugError(final String msg) {
        if (LogWriter.isDebug) {
            Log.e(LogWriter.DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void debugError(final String tag, final String msg) {
        if (LogWriter.isDebug) {
            Log.e(tag, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void debugInfo(final String msg) {
        if (LogWriter.isDebug) {
            Log.i(LogWriter.DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void e(final Object thiz, final String msg) {
        if (LogWriter.isDebug) {
            Log.e(LogWriter.DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void e(final String msg) {
        if (LogWriter.isDebug) {
            if (msg != null) {
                Log.e(LogWriter.DEBUG_TAG, msg);
            }
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void e(final String DEBUG_TAG, final Object thiz, final String msg) {
        if (LogWriter.isDebug) {
            Log.e(DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(DEBUG_TAG, msg);
        }
    }

    public static void e(final String DEBUG_TAG, final String msg) {
        if (LogWriter.isDebug) {
            Log.e(DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(DEBUG_TAG, msg);
        }
    }

    public static void i(final Object thiz, final String msg) {
        if (LogWriter.isDebug) {
            Log.i(LogWriter.DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void i(final String msg) {
        if (LogWriter.isDebug) {
            Log.i(LogWriter.DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void i(final String DEBUG_TAG, final Object thiz, final String msg) {
        if (LogWriter.isDebug) {
            Log.i(DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
    }

    public static void i(final String DEBUG_TAG, final String msg) {
        if (LogWriter.isDebug) {
            Log.i(DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(DEBUG_TAG, msg);
        }
    }

    public static void LogToFile(final String newLog) {
        LogWriter.LogToFile(LogWriter.DEBUG_TAG, newLog);
    }

    public static void logStack(final String msg) {
        String stack = Stack.getCallRef();
//        if(stack.contains("com.jaredrummler")) {
//            return;
//        }
        String log = String.format(msg + "\"Stack\":\"%s\"}", stack);
        LogWriter.log2File(log);
    }

    public static void log2File(final String text) {
        final String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "amon";
        final File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        final String fileName = Process.myUid() + ".log";
        File file = new File(filePath, fileName);
        try {
            final FileWriter filerWriter = new FileWriter(file, true);
            final BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(text);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void LogToFile(final String tag, final String text) {
        final String needWriteMessage = tag + ":" + text;
        final String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator;
        XposedBridge.log(filePath);
        final String fileName = "amon.log";
        final File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(filePath, fileName);
        try {
            final FileWriter filerWriter = new FileWriter(file, true);
            final BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void v(final Object thiz, final String msg) {
        if (LogWriter.isDebug) {
            Log.v(LogWriter.DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
    }

    public static void v(final String msg) {
        if (LogWriter.isDebug) {
            Log.v(LogWriter.DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void v(final String DEBUG_TAG, final Object thiz, final String msg) {
        if (LogWriter.isDebug) {
            Log.v(DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
    }

    public static void v(final String DEBUG_TAG, final String msg) {
        if (LogWriter.isDebug) {
            Log.v(DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(DEBUG_TAG, msg);
        }
    }

    public static void w(final Object thiz) {
        if (LogWriter.isDebug) {
            Log.w(LogWriter.DEBUG_TAG, thiz.getClass().getSimpleName());
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, thiz.getClass().getSimpleName());
        }
    }

    public static void w(final Object thiz, final String msg) {
        if (LogWriter.isDebug) {
            Log.w(LogWriter.DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
    }

    public static void w(final String msg) {
        if (LogWriter.isDebug) {
            Log.w(LogWriter.DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void w(final String DEBUG_TAG, final Object thiz, final String msg) {
        if (LogWriter.isDebug) {
            Log.w(DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(DEBUG_TAG, thiz.getClass().getSimpleName() + ": " + msg);
        }
    }

    public static void w(final String DEBUG_TAG, final String msg) {
        if (LogWriter.isDebug) {
            Log.w(DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(DEBUG_TAG, msg);
        }
    }

    public static void warnInfo(final String msg) {
        if (LogWriter.isDebug) {
            Log.w(LogWriter.DEBUG_TAG, msg);
        }
        if (LogWriter.isWriteToLog) {
            LogWriter.LogToFile(LogWriter.DEBUG_TAG, msg);
        }
    }

    public static void crash(final String DEBUG_TAG, final String msg) {
        Log.e(DEBUG_TAG, msg);
        LogWriter.LogToFile(DEBUG_TAG, msg);
    }

    public static void crash(final String msg) {
        Log.e(DEBUG_TAG, msg);
        LogWriter.LogToFile(msg);
    }

    public static void setIsDebug(boolean isDebug) {
        LogWriter.isDebug = isDebug;
    }

    public static void setIsWriteToLog(boolean isWriteToLog) {
        LogWriter.isWriteToLog = isWriteToLog;
    }
}
