package com.android.appmonitor;

import java.util.List;

import util.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NewInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收广播：设备上新安装了一个应用程序包。
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            System.out.println("****************app insatll****************");
            String packageNmae = intent.getDataString().substring(8);
            String jsonStr = Util.readData();
            List<String> monitoredList = Util.jsonStr2list(jsonStr);
            monitoredList.add(packageNmae);
            jsonStr = Util.list2json(monitoredList);
            Util.saveData(jsonStr);
        }

        //接收广播：设备上删除了一个应用程序包。
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            System.out.println("****************app remove****************");
            String packageNmae = intent.getDataString().substring(8);
            String jsonStr = Util.readData();
            List<String> monitoredList = Util.jsonStr2list(jsonStr);
            monitoredList.remove(packageNmae);
            jsonStr = Util.list2json(monitoredList);
            Util.saveData(jsonStr);
        }
    }
}
