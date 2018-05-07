package me.mikusjelly.amon.ui.fragments.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Set;

import me.mikusjelly.amon.utils.Global;

public class PackageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        SharedPreferences sp = ctx.getSharedPreferences(Global.SHARED_PREFS_HOOK_PACKAGE, Context.MODE_WORLD_READABLE);
        Set<String> pkgSet = sp.getStringSet("pkgs", null);

//        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED") ||
//                intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
//            String packageName = intent.getDataString().substring(8);
//
//            Log.d(Global.LOG_TAG, packageName);
//            Log.d(Global.LOG_TAG, ctx.getPackageName());
//            if (packageName.equals(ctx.getPackageName())) {
//                return;
//            }
//
//            HashSet<String> hashSet = new HashSet<>();
//            hashSet.add(packageName);
//
//            if (pkgSet == null) {
//                pkgSet = hashSet;
//            } else {
//                pkgSet.addAll(hashSet);
//            }
//
//            SharedPreferences.Editor editor = sp.edit();
//            editor.clear();
//            editor.putStringSet(Global.SHARED_PREFS_HOOK_PACKAGE, pkgSet);
//            editor.apply();
//
//
//        } else

        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            if (pkgSet == null) {
                return;
            }

            String packageName = intent.getDataString().substring(8);
            pkgSet.remove(packageName);
            SharedPreferences.Editor edit = sp.edit();
            edit.clear();
            edit.putStringSet(Global.SHARED_PREFS_HOOK_PACKAGE, pkgSet);
            edit.apply();

        }
    }
}
