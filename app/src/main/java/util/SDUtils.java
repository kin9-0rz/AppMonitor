package util;

import android.os.Environment;
import android.os.Looper;
import android.text.Html;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class SDUtils {
    public static File createDir(String dirName) {
        String sdcard = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        File dir = new File(sdcard + File.separator + dirName);
        if (!dir.exists()) {
            dir.mkdir();
            Log.d("SDUtils", "created the path:" + dir.getAbsolutePath());
        }
        return dir;
    }

    public static File createFile(String dirName, String fileName) {
        String sdcard = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        File dir = new File(sdcard + File.separator + dirName);
        File localFile = new File(dir.getAbsolutePath() + File.separator
                + fileName);

        if (!dir.exists()) {
            dir.mkdir();
            Log.d("SDUtils", "created the path:" + dir.getAbsolutePath());
        }
        if (!localFile.exists()) {
            try {
                localFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("SDUtils", "created the file:" + fileName);
        }
        return localFile;
    }

    public static boolean isSdCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
