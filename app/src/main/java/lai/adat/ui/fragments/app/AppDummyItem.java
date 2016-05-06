package lai.adat.ui.fragments.app;

import android.graphics.drawable.Drawable;

public class AppDummyItem {
    private String appLabel;
    private Drawable appIcon;
    private String pkgName;

    public AppDummyItem() {
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appName) {
        this.appLabel = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }
}
