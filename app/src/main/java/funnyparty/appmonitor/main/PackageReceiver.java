package funnyparty.appmonitor.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import funnyparty.appmonitor.utils.Globle;

public class PackageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        SharedPreferences sp = ctx.getSharedPreferences("pkgs", Context.MODE_WORLD_READABLE);
        Set<String> pkgSet = sp.getStringSet("pkgs", null);

        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED") ||
                intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            String packageName = intent.getDataString().substring(8);

            Log.d(Globle.LOG_TAG, packageName);
            Log.d(Globle.LOG_TAG, ctx.getPackageName());
            if (packageName.equals(ctx.getPackageName())) {
                return;
            }

            HashSet<String> hashSet = new HashSet<String>();
            hashSet.add(packageName);

            if (pkgSet == null) {
                pkgSet = hashSet;
            } else {
                pkgSet.addAll(hashSet);
            }

            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.putStringSet("pkgs", pkgSet);
            editor.commit();


        } else if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            if (pkgSet == null) {
                Log.e(Globle.LOG_TAG, "REMOVED is null");
                return;
            }

            String packageNmae = intent.getDataString().substring(8);
            pkgSet.remove(packageNmae);
            SharedPreferences.Editor edit = sp.edit();
            edit.clear();
            edit.putStringSet("pkgs", pkgSet);
            edit.commit();

        }
    }
}
