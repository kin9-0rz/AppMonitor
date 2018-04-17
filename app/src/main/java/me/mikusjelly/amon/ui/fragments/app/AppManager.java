package me.mikusjelly.amon.ui.fragments.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by acgmohu on 11/28/14.
 *
 * 应用管理
 */
public class AppManager {
    public final static String LABEL = "label";
    public final static String PACKAGE_NAME = "package";
    public final static String ICON = "icon";


    public static ArrayList<PackageInfo> getInstallApp(Context context) {
        ArrayList<PackageInfo> infos = new ArrayList<PackageInfo>();
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packages = manager.
                getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

        for (PackageInfo info : packages) {
            // 判断是否为非系统预装的应用程序
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                infos.add(info);
            }
        }

        return infos;
    }

    public static List<Map<String, Object>> getPackageInfoList(Context context) {
        PackageManager manager = context.getPackageManager();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        List<PackageInfo> packageInfos = manager
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

        for (PackageInfo packageInfo : packageInfos) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)
                    == ApplicationInfo.FLAG_SYSTEM) {// 过滤掉系统程序
                continue;
            }

            String packageName = packageInfo.packageName;
            String label = packageInfo.applicationInfo.loadLabel(manager).toString();
            Drawable icon = packageInfo.applicationInfo.loadIcon(manager);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(LABEL, label);
            map.put(PACKAGE_NAME, packageName);
            map.put(ICON, icon);

            list.add(map);
        }

        return list;
    }


}
