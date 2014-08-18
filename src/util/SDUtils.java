package util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class SDUtils {
	/**
	 * @param dirNmae
	 *            This function is used for create dir on sdcard
	 * @return
	 */
	public static File createDir(String dirName) {
		String sdcard = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File dir = new File(sdcard + File.separator + dirName);
		if (!dir.exists()) {
			dir.mkdir();
//			System.out.println(dir.exists());
			Log.d("SDUtils", "created the path:" + dir.getAbsolutePath());
		}
		return dir;
	}

	/**
	 * @param dirName
	 * @param fileName
	 * 
	 *            This function is used for create file on sdcard
	 * 
	 * @return
	 */
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

	public static boolean isSdcardExists() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static boolean isSdCardAvailable() {
		boolean isAvailable = Environment.getExternalStorageState().equals(
				"mounted");

		Log.d("getExternalStorageState", String.valueOf(isAvailable));

		return isAvailable;
	}
}
