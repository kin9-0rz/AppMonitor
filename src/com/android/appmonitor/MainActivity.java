package com.android.appmonitor;

import java.util.ArrayList;
import java.util.List;

import com.android.appmoniter.R;

import util.AppInfo;
import util.PackageInfoAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity implements OnItemClickListener {

	private ListView packageList;
	private List<AppInfo> appInfoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		packageList = (ListView) findViewById(R.id.packageList);
		appInfoList = new ArrayList<AppInfo>();

		getData();

		PackageInfoAdapter packgaeAdapter = new PackageInfoAdapter(this,appInfoList);
		packageList.setAdapter(packgaeAdapter);
		packageList.setOnItemClickListener(this);
		packageList.setAlwaysDrawnWithCacheEnabled(true);

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
		System.out.println(appInfoList.get(arg2).getPkgName());
		
		Intent localIntent = new Intent(MainActivity.this, AppActivity.class);
		localIntent.putExtra("packageName",appInfoList.get(arg2).getPkgName());
		startActivity(localIntent);
		
	}

}
