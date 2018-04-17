package me.mikusjelly.amon.ui.fragments.app;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AppContent {

    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public static List<Map<String, Object>> packageInfos = new ArrayList<Map<String, Object>>();

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class DummyItem {

        private String id;
        private String label;
        private String packageName;
        private Drawable icon;

        public DummyItem(String id, String label, String packageName, Drawable icon) {
            this.id = id;
            this.label = label;
            this.packageName = packageName;
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        public String getPackageName() {
            return packageName;
        }

        public Drawable getIcon() {
            return icon;
        }

        @Override
        public String toString() {
            return label;
        }
    }

//    class AppDummyItem {
//        private String appLabel;
//        private Drawable appIcon;
//        private String pkgName;
//
//        public AppDummyItem() {
//        }
//
//        public String getAppLabel() {
//            return appLabel;
//        }
//
//        public void setAppLabel(String appName) {
//            this.appLabel = appName;
//        }
//
//        public Drawable getAppIcon() {
//            return appIcon;
//        }
//
//        public void setAppIcon(Drawable appIcon) {
//            this.appIcon = appIcon;
//        }
//
//        public String getPkgName() {
//            return pkgName;
//        }
//
//        public void setPkgName(String pkgName) {
//            this.pkgName = pkgName;
//        }
//    }
}
