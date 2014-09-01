package com.android.appmonitor;

import java.util.ArrayList;
import java.util.List;

import com.android.appmoniter.R;

import util.AppInfo;
import util.PackageInfoAdapter;
import util.Util;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity implements OnItemClickListener {

	private ListView packageList;
	private List<AppInfo> appInfoList;
	private boolean[] isSeleted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		packageList = (ListView) findViewById(R.id.packageList);
		appInfoList = new ArrayList<AppInfo>();

		getData();
		isSeleted = new boolean[appInfoList.size()];

		PackageInfoAdapter packgaeAdapter = new PackageInfoAdapter(this,appInfoList,isSeleted);
		packageList.setAdapter(packgaeAdapter);
		packageList.setOnItemClickListener(this);
		packageList.setAlwaysDrawnWithCacheEnabled(true);

	}

	//初始化isSelected数组，初始化多选按钮
	public void loadInit(){
		String data = Util.readData();
		if(data == null){
			
		}else{
			
		}
	}
	
	
	public void getData() {
		// System.out.println("*******getDatea*********");

		PackageManager packManager = this.getPackageManager();
		List<PackageInfo> packageInfoList = packManager.getInstalledPackages(0);

		for (int i = 0; i < packageInfoList.size(); i++) {
			AppInfo appInfo = new AppInfo();
			PackageInfo packageInfo = packageInfoList.get(i);
			appInfo.setAppIcon(packManager
					.getApplicationIcon(packageInfo.applicationInfo));
			appInfo.setAppLabel(packManager.getApplicationLabel(
					packageInfo.applicationInfo).toString());
			appInfo.setPkgName(packageInfo.applicationInfo.packageName);

			appInfoList.add(appInfo);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String pkgName = appInfoList.get(arg2).getPkgName();
		System.out.println(pkgName);
		RelativeLayout lr = (RelativeLayout) arg1;
		CheckBox tmp = (CheckBox) lr.getChildAt(2);
		tmp.setSelected(true);
		isSeleted[arg2] = true;
		Intent localIntent = new Intent(MainActivity.this, AppActivity.class);
		localIntent.putExtra("packageName",appInfoList.get(arg2).getPkgName());
//		startActivity(localIntent);
		
	}

}
