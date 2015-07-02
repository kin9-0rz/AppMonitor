package com.android.appmonitor;

import java.util.ArrayList;
import java.util.List;

import com.android.appmoniter.R;

import util.AppInfo;
import util.PackageInfoAdapter;
import util.Util;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

    private static final int MENU_SAVE = 1;
    private ListView packageList;
    private List<String> selectedApp;
    private List<AppInfo> appInfoList;
    private boolean[] isSeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        packageList = (ListView) findViewById(R.id.packageList);
        appInfoList = new ArrayList<AppInfo>();
        selectedApp = new ArrayList<String>();

        getPkgList();
        loadInit();

        PackageInfoAdapter packgaeAdapter = new PackageInfoAdapter(this, appInfoList, isSeleted);
        packageList.setAdapter(packgaeAdapter);
        packageList.setOnItemClickListener(this);
        packageList.setAlwaysDrawnWithCacheEnabled(true);

    }

    //inti array isSelected and checkbox
    public void loadInit() {
        isSeleted = new boolean[appInfoList.size()];
        String data = Util.readData();
        if (data == null) {
            //first boot
            for (int i = 0; i < isSeleted.length; i++) {
                isSeleted[i] = false;
                System.out.println(isSeleted[i]);
            }
        } else {
            //load json
            List<String> selectedApp = Util.jsonStr2list(data);
            findSelected(selectedApp);
        }
    }

    //to find which app is monitoring
    public void findSelected(List<String> selectedApp) {
        for (AppInfo appinfo : appInfoList) {
            if (selectedApp.contains(appinfo.getPkgName())) {
                int i = appInfoList.indexOf(appinfo);
                isSeleted[i] = true;
            }
        }
    }

    //get all app packagename
    public void getPkgList() {
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

    //mark which app is selected
    public void whichSelect(List<AppInfo> appInfo, boolean[] selected) {
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                selectedApp.add(appInfo.get(i).getPkgName());

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, MENU_SAVE, 0, "Save");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            whichSelect(appInfoList, isSeleted);
            String data = Util.list2json(selectedApp);
            Util.saveData(data);
            Toast.makeText(this, "moniter begin", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        String pkgName = appInfoList.get(arg2).getPkgName();
        System.out.println(pkgName);
        RelativeLayout lr = (RelativeLayout) arg1;
        CheckBox tmp = (CheckBox) lr.getChildAt(2);
        tmp.toggle();
        isSeleted[arg2] = tmp.isChecked();
    }

}
