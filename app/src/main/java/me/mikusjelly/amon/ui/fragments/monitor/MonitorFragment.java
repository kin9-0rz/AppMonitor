package me.mikusjelly.amon.ui.fragments.monitor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.mikusjelly.amon.R;
import me.mikusjelly.amon.ui.Home;
import me.mikusjelly.amon.ui.fragments.BaseFragment;
import me.mikusjelly.amon.ui.fragments.app.AppManager;
import me.mikusjelly.amon.utils.Global;

public class MonitorFragment extends BaseFragment implements AdapterView.OnItemClickListener {


    SharedPreferences settingPreferences = null;
    SharedPreferences hookPackagePreferences = null;
    Context mContext;
    private ListView packageList;
    private List<String> hookPackageList;
    private ArrayList<AppInfo> appInfoList = new ArrayList<>();
    private boolean[] isCheckedArr;

    public MonitorFragment() {
        super();
    }

    public static MonitorFragment newInstance(int sectionNum) {
        MonitorFragment fragment = new MonitorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SECTION_NUM, sectionNum);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();

        settingPreferences = mContext.getSharedPreferences(Global.SHARED_PREFS_SETTING, Context.MODE_PRIVATE);
        hookPackagePreferences = mContext.getSharedPreferences(Global.SHARED_PREFS_HOOK_PACKAGE, Context.MODE_WORLD_READABLE);

        if (settingPreferences.getBoolean("isFirst", true)) {
            initSystemAPIs();
            SharedPreferences.Editor editor = settingPreferences.edit();
            editor.putBoolean("isFirst", false);
            editor.apply();
        }

        if (appInfoList!=null) {
            initAppInfoList();
        }
        initUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        this.packageList = (ListView) view.findViewById(R.id.packageList);

        PackageInfoAdapter packageAdapter = new PackageInfoAdapter(inflater.getContext(), appInfoList, isCheckedArr);
        packageList.setAdapter(packageAdapter);
        packageList.setOnItemClickListener(this);
        packageList.setAlwaysDrawnWithCacheEnabled(true);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Home) activity).onSectionAttached(
                getArguments().getInt(SECTION_NUM));
    }


    private void initSystemAPIs() {
        DataBaseManager.initManager(mContext);
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

        SharedPreferences apisPref = mContext.getSharedPreferences(Global.SHARED_PREFS_APIS, Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = apisPref.edit();
        editor.putStringSet("system_api", apis);
        editor.apply();
    }


    public void initUI() {
        isCheckedArr = new boolean[appInfoList.size()];
        Set<String> hookPackageSet = hookPackagePreferences.getStringSet("pkgs", null);

        if (hookPackageSet != null) {
            hookPackageList = new ArrayList<>(hookPackageSet);
        } else {
            hookPackageList = new ArrayList<>();
        }

        if (hookPackageSet != null) {
            for (AppInfo appinfo : appInfoList) {
                if (hookPackageSet.contains(appinfo.getPkgName())) {
                    int i = appInfoList.indexOf(appinfo);
                    isCheckedArr[i] = true;
                }
            }
        }

    }

    public void initAppInfoList() {
        PackageManager manager = mContext.getPackageManager();

        List<PackageInfo> packages = AppManager.getInstallApp(mContext);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo info = packages.get(i);

            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                AppInfo appInfo = new AppInfo();

                appInfo.setAppIcon(manager
                        .getApplicationIcon(info.applicationInfo));
                appInfo.setAppLabel(manager.getApplicationLabel(
                        info.applicationInfo).toString());
                appInfo.setPkgName(info.applicationInfo.packageName);

                appInfoList.add(appInfo);

            }
        }

        Collections.sort(appInfoList, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo lhs, AppInfo rhs) {
                return lhs.getPkgName().compareTo(rhs.getPkgName());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String pkgName = appInfoList.get(position).getPkgName();
        RelativeLayout relativeLayout = (RelativeLayout) view;

        CheckBox checkBox = (CheckBox) relativeLayout.getChildAt(2);
        checkBox.toggle();

        if (checkBox.isChecked()) {
            hookPackageList.add(pkgName);
        } else {
            hookPackageList.remove(pkgName);
        }

        SharedPreferences.Editor editor = hookPackagePreferences.edit();
        editor.clear();
        editor.putStringSet("pkgs", new HashSet<>(hookPackageList));
        editor.apply();
    }
}
