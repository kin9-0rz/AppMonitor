package funnyparty.appmonitor.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import funnyparty.appmonitor.R;

public class MainActivity extends Activity implements OnItemClickListener {

    private static final int MENU_SAVE = 1;
    private static final int MENU_INIT_APIS = 2;
    SharedPreferences settingPref = null;
    SharedPreferences pkgsPref = null;
    private ListView packageList;
    private List<String> selectedApp;
    private ArrayList<AppInfo> appInfoList;
    private boolean[] isSeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.packageList = (ListView) findViewById(R.id.packageList);
        appInfoList = new ArrayList<AppInfo>();
        selectedApp = new ArrayList<String>();

        settingPref = getSharedPreferences("setting", Context.MODE_PRIVATE);
        pkgsPref = getSharedPreferences("pkgs", Context.MODE_WORLD_READABLE);
        if (settingPref.getBoolean("isFirst", true)) {
            initDatabase();
            SharedPreferences.Editor editor = settingPref.edit();
            editor.putBoolean("isFirst", false);
            editor.commit();

        }

        initAppInfoList();
        initUI();

        PackageInfoAdapter packgaeAdapter = new PackageInfoAdapter(this, appInfoList, isSeleted);
        packageList.setAdapter(packgaeAdapter);
        packageList.setOnItemClickListener(this);
        packageList.setAlwaysDrawnWithCacheEnabled(true);

    }

    private void initDatabase() {
        DataBaseManager.initManager(getApplication());
        DataBaseManager mg = DataBaseManager.getManager();
        SQLiteDatabase db1 = mg.getDatabase("system_api.db");
        Cursor cursor = db1.query("apis", null, null, null, null, null, null);

        HashSet<String> apis = new HashSet<String>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String api = cursor.getString(1);
            int level = cursor.getInt(2);
            int flag = cursor.getInt(3);

            System.out.println(id + " : " + api + " : " + level + " : " + flag);
            if (flag == 1) {
                apis.add(level + api);
            }
        }

        SharedPreferences apisPref = getSharedPreferences("apis", Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = apisPref.edit();
        editor.putStringSet("system_api", apis);
        editor.commit();
    }


    public void initUI() {
        isSeleted = new boolean[appInfoList.size()];
        Set<String> pkgs = pkgsPref.getStringSet("pkgs", null);

        if (pkgs != null) {
            for (AppInfo appinfo : appInfoList) {
                if (pkgs.contains(appinfo.getPkgName())) {
                    int i = appInfoList.indexOf(appinfo);
                    isSeleted[i] = true;
                }
            }
        }

    }

    public void initAppInfoList() {
        PackageManager manager = this.getPackageManager();
        List<PackageInfo> packages = manager.getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            AppInfo appInfo = new AppInfo();
            PackageInfo info = packages.get(i);
            appInfo.setAppIcon(manager
                    .getApplicationIcon(info.applicationInfo));
            appInfo.setAppLabel(manager.getApplicationLabel(
                    info.applicationInfo).toString());
            appInfo.setPkgName(info.applicationInfo.packageName);

            appInfoList.add(appInfo);
        }

        Collections.sort(appInfoList, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo lhs, AppInfo rhs) {
                return lhs.getPkgName().compareTo(rhs.getPkgName());
            }
        });
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
        menu.add(0, MENU_INIT_APIS, 1, "Init APIs");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            whichSelect(appInfoList, isSeleted);

            SharedPreferences.Editor edit = pkgsPref.edit();
            edit.clear();
            edit.putStringSet("pkgs", new HashSet<String>(selectedApp));
            edit.commit();

//            Toast.makeText(this, "save config", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == 2) {
            initDatabase();
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
