package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Util {
	
	public static String getSystemTime(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd----hh:mm:ss", Locale.getDefault());
		Date date = new Date(System.currentTimeMillis());
		String dateTime = sDateFormat.format(date);
		return dateTime;
	}

	public static String readData() {
		String data = null;
		if(SDUtils.isSdCardAvailable()){
			File moniterDir = SDUtils.createDir("Appmonitor");
			File file = SDUtils.createFile(moniterDir.getName(), "data");
			try {
				FileInputStream in = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String str = br.readLine();
				data = str;
				in.close();
			} catch (FileNotFoundException e) {
				System.out.println("file not found!");
			} catch (IOException e) {
				System.out.println("Output error!");
			}
		}
		return data;
	}
	
	public static void writeLog(String pkgName, List<String> logList){
		if(SDUtils.isSdCardAvailable()){
//			File logDir = SDUtils.createDir("Appmonitor/AppLog");
			File logFile = SDUtils.createFile("Appmonitor/AppLog", pkgName);
			FileWriter fw = null;
			try{
				//解决打开文件从头开始问题，换行问题
				fw = new FileWriter(logFile, true);
				for(String log : logList){
					fw.write(log+"\n");
				}
				fw.write("\n");
				fw.flush();
				fw.close();
			}catch (FileNotFoundException e) {
				System.out.println("file not found!");
			} catch (IOException e) {
				System.out.println("Output error!");
			}
		}
	}
}
