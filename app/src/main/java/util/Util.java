package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

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
			File moniterDir = SDUtils.createDir("AppMonitor");
			File file = SDUtils.createFile(moniterDir.getName(), "data");
			try {
				FileInputStream in = new FileInputStream(file);
//				System.out.println("***********readData************");
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
	
	public static List<String> jsonStr2list(String jsonStr){
		List<String> appList = new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONArray jsonArr = jsonObject.getJSONArray("APP");
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject jo = (JSONObject) jsonArr.opt(i);
				appList.add(jo.getString("packageName"));
			}
		} catch (Exception e) {
			System.out.println("json error!!!");
		}
		return appList;
	}
	
	public static String list2json(List<String> list) {
		String jsonresult = null;
		JSONObject jsonObj = new JSONObject();
		try {
			JSONArray jsonArr = new JSONArray();
			for (String pkg : list) {
				JSONObject jobj = new JSONObject();
				jobj.put("packageName", pkg);
				jsonArr.put(jobj);
			}
			jsonObj.put("APP", jsonArr);
		} catch (Exception e) {
			System.out.println("json error!!!");
		}

		jsonresult = jsonObj.toString();
		return jsonresult;
	}
	
	public static void saveData(String data) {
		if(SDUtils.isSdCardAvailable()){
			File moniterDir = SDUtils.createDir("Appmonitor");
			File logDir = SDUtils.createDir(moniterDir.getName()+"/AppLog");
			File file = SDUtils.createFile(moniterDir.getName(), "data");
			Log.d("AppActivity", "created the path:" + logDir.getAbsolutePath());
			FileWriter fw = null;
			try {
				fw = new FileWriter(file, false);
				fw.write(data);
				fw.flush();
				fw.close();
			} catch (FileNotFoundException e) {
				System.out.println("file not found!");
			} catch (IOException e) {
				System.out.println("Output error!");
			}
		}
	}
	
	public static void writeLog(String pkgName, List<String> logList){
		if(SDUtils.isSdCardAvailable()){
			File logFile = SDUtils.createFile("Appmonitor/AppLog", pkgName);
			FileWriter fw = null;
			try{
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
	
	public static void writeNetLog(String pkgName, List<String> logList){
		if(SDUtils.isSdCardAvailable()){
			File logFile = SDUtils.createFile("Appmonitor/NetLog", pkgName);
			FileWriter fw = null;
			try{
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
	
	public static Method findMethodExact(String className, ClassLoader classLoader, String methodName, Class<?>... parameterTypes) {

		try {
			Class<?> clazz = classLoader.loadClass(className);
			Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return method;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
