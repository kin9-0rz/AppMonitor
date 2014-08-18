package com.android.appmonitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.android.appmoniter.R;

import util.SDUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AppActivity extends Activity {

	private TextView appPackageName = null;
	private Button beginButton = null;
	private String packageName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		Intent localIntent = this.getIntent();

		appPackageName = (TextView) findViewById(R.id.appPackageName);
		beginButton = (Button) findViewById(R.id.BeginButton);

		packageName = localIntent.getStringExtra("packageName");
		appPackageName.setText(packageName);
		beginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveData(packageName);
				AppActivity.this.finish();
			}
		});
	}

	public void saveData(String data) {
		if(SDUtils.isSdCardAvailable()){
//			System.out.println("**************write file*****************");
			File moniterDir = SDUtils.createDir("Appmonitor");
			File logDir = SDUtils.createDir(moniterDir.getName()+"/AppLog");
			File file = SDUtils.createFile(moniterDir.getName(), "data");
			Log.d("AppActivity", "created the path:" + logDir.getAbsolutePath());
			try {
				FileOutputStream out = new FileOutputStream(file);
				String content = data;
				out.write(content.getBytes());
				out.close();
			} catch (FileNotFoundException e) {
				System.out.println("file not found!");
			} catch (IOException e) {
				System.out.println("Output error!");
			}
		}
		Toast.makeText(this, "moniter begin", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app, menu);
		return true;
	}

}
