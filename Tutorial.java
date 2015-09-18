import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

//import android.app.AlertDialog$Builder;
//import android.content.DialogInterface$OnClickListener;
//import android.view.WindowManager$LayoutParams;
//import de.robv.android.xposed.XC_MethodHook$MethodHookParam;
//import de.robv.android.xposed.callbacks.XC_LoadPackage$LoadPackageParam;

public class Tutorial {
    public String TAG;
    public Context context;
    public boolean isGetRunningTask;
    public List pkg;
    public HashMap pkgFilter;
    public String[] pkgs;
    public long t1;
    public long t2;

    public Tutorial() {

    // DumpDex
        XposedHelpers.findAndHookMethod("com.android.internal.policy.impl.PhoneWindowManager", v3, "checkAddPermission",
                v5);
        v3 = lpparam.classLoader;
        v5 = new Object[v12];
        v5[0] = String.class;
        v5[1] = String.class;
        v5[v10] = Integer.TYPE;
        v5[v11] = new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
            }

            protected void beforeHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
                int v3;
                StackTraceElement[] v19 = Thread.currentThread().getStackTrace();
                int v11;
                for (v11 = 0; v11 < v19.length; ++v11) {
                    String v14 = v19[v11].toString();
                    if ((v14.contains("unpack")) || (v14.contains("Unpack")) || (v14.contains("decrypt"))
                            || (v14.contains("Decrypt"))) {
                        Tutorial.this.Mylog("LoadDEX");
                        Object v8 = param.args[0];
                        Object v9 = param.args[1];
                        String v15 = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())
                                + File.separator + "new1.dex";
                        String v16 = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())
                                + File.separator + "new2.dex";
                        File v4 = new File(v15);
                        if (!v4.exists() && !v4.createNewFile()) {
                            Tutorial.this.Mylog("create file1 failue!");
                        }

                        File v6 = new File(v16);
                        if (!v6.exists() && !v6.createNewFile()) {
                            Tutorial.this.Mylog("create file2 failue!");
                        }

                        byte[] v20 = new byte[8192];
                        File v5 = new File(((String) v8));
                        if ((v5.exists()) && v5.length() > 8192 && (v4.exists())) {
                            Tutorial.this.Mylog("dumping first file...");
                            FileInputStream v12 = new FileInputStream(v5);
                            FileOutputStream v17 = new FileOutputStream(v4);
                            while (true) {
                                v3 = v12.read(v20);
                                if (v3 <= 0) {
                                    break;
                                }

                                v17.write(v20, 0, v3);
                            }

                            v12.close();
                            v17.close();
                            Tutorial.this.Mylog("dumping successfully => newpath1");
                        }

                        File v7 = new File(((String) v9));
                        if (!v7.exists()) {
                            goto label_139;
                        }

                        if (v7.length() <= 8192) {
                            goto label_139;
                        }

                        if (!v6.exists()) {
                            goto label_139;
                        }

                        Tutorial.this.Mylog("dumping first file...");
                        FileInputStream v13 = new FileInputStream(v7);
                        FileOutputStream v18 = new FileOutputStream(v6);
                        while (true) {
                            v3 = v13.read(v20);
                            if (v3 <= 0) {
                                break;
                            }

                            v18.write(v20, 0, v3);
                        }

                        v13.close();
                        v18.close();
                        Tutorial.this.Mylog("dumping successfully => newpath2");
                    }

                    label_139:
                }
            }
        };

        XposedHelpers.findAndHookMethod("dalvik.system.DexFile", v3, "loadDex", v5);




        if (!this.pkg.contains(lpparam.packageName)) {
            XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", lpparam.classLoader,
                    "getDeviceId", new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
                            Object v0 = param.getResult();
                            if ((((String) v0).equals("000000000000000")) || v0 == null || v0 == "") {
                                Tutorial.this.Mylog("GetIMEI * _ *_ *_ *_ *_ *_ *_ *_ *_ *_ *_ *_ * GetIMEI");
                                param.setResult(new String("357409052261460"));
                                Tutorial.this.call_link("GetIMEI");
                            }
                        }

                        protected void beforeHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
                        }
                    }});
            XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", lpparam.classLoader,
                    "getSubscriberId", new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
                            param.getResult();
                            Tutorial.this.Mylog("GetIMSI * _ *_ *_ *_ *_ *_ *_ *_ *_ *_ *_ *_ * GetIMSI");
                            param.setResult(new String("809357409052261461"));
                            Tutorial.this.call_link("GetIMSI");
                        }

                        protected void beforeHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
                        }
                    }});
            XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", lpparam.classLoader,
                    "getLine1Number", new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
                            param.getResult();
                            Tutorial.this.Mylog("GetPhoneNumber * _ *_ *_ *_ *_ *_ *_ *_ *_ *_ *_ *_ * GetPhoneNumber");
                            param.setResult(new String("18065874158"));
                            Tutorial.this.call_link("GetPhoneNumber");
                        }

                        protected void beforeHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
                        }
                    }});



            v3 = lpparam.classLoader;
            v5 = new Object[v10];
            v5[0] = URI.class;
            v5[1] = new XC_MethodHook() {
                protected void afterHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
                }

                protected void beforeHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
                    String v0 = param.args[0].toString();
                    Tutorial.this.Mylog("HTTP_Get/Post * _ *_ *_ *_ *_ *_ *_ *_ *_ *_ *_ *_ * HTTP_Get/Post");
                    Tutorial.this.Mylog("HTTP_Get/Post url : " + v0);
                    Tutorial.this.call_link("HTTP_Get/Post");
                }
            };
            XposedHelpers.findAndHookMethod("org.apache.http.client.methods.HttpRequestBase", v3, "setURI",
                    v5);


            v3 = lpparam.classLoader;
            v5 = new Object[v12];
            v5[0] = HttpHost.class;
            v5[1] = HttpRequest.class;
            v5[v10] = HttpContext.class;
            v5[v11] = new XC_MethodHook() {
                protected void afterHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
                }

                protected void beforeHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
                    byte[] v2;
                    Header[] v5;
                    Tutorial.this.Mylog("HTTP_Execute * _ *_ *_ *_ *_ *_ *_ *_ *_ *_ *_ *_ * HTTP_Execute");
                    Object v10 = param.args[1];
                    if ((v10 instanceof HttpGet)) {
                        Tutorial.this.Mylog("HTTP_Method * " + v10.getMethod());
                        Tutorial.this.Mylog("HTTP_URL : " + v10.getURI().toString());
                        v5 = ((HttpRequest) v10).getAllHeaders();
                        if (v5 == null) {
                            goto label_32;
                        }

                        int v9;
                        for (v9 = 0; true; ++v9) {
                            if (v9 >= v5.length) {
                                goto label_32;
                            }

                            Tutorial.this.Mylog(String.valueOf(v5[v9].getName()) + ":" + v5[v9].getName());
                        }
                    }

                    if ((v10 instanceof HttpPost)) {
                        Object v8 = v10;
                        Tutorial.this.Mylog("HTTP_Method * " + ((HttpPost) v8).getMethod());
                        Tutorial.this.Mylog("HTTP_URL : " + ((HttpPost) v8).getURI().toString());
                        v5 = ((HttpRequest) v10).getAllHeaders();
                        if (v5 != null) {
                            for (v9 = 0; v9 < v5.length; ++v9) {
                                Tutorial.this.Mylog(String.valueOf(v5[v9].getName()) + ":" + v5[v9].
                                        getValue());
                            }
                        }

                        HttpEntity v4 = ((HttpPost) v8).getEntity();
                        if (v4.getContentType() != null) {
                            String v1 = v4.getContentType().getValue();
                            if ("application/x-www-form-urlencoded".equals(v1)) {
                                try {
                                    v2 = new byte[((int) v4.getContentLength())];
                                    v4.getContent().read(v2);
                                    Tutorial.this.Mylog("HTTP_POST Content : " + new String(v2, "ISO-8859-1"));
                                } catch (IOException v3) {
                                    v3.printStackTrace();
                                } catch (IllegalStateException v3_1) {
                                    v3_1.printStackTrace();
                                }

                                goto label_32;
                            }

                            if (!v1.startsWith("application/octet-stream")) {
                                goto label_32;
                            }

                            try {
                                v2 = new byte[((int) v4.getContentLength())];
                                v4.getContent().read(v2);
                                Tutorial.this.Mylog("HTTP_POST Content : " + new String(v2, v1.substring(
                                        v1.lastIndexOf("=") + 1)));
                            } catch (IOException v3) {
                                v3.printStackTrace();
                            } catch (IllegalStateException v3_1) {
                                v3_1.printStackTrace();
                            }

                            goto label_32;
                        }

                        v2 = new byte[((int) v4.getContentLength())];
                        try {
                            v4.getContent().read(v2);
                            Tutorial.this.Mylog("HTTP_POST Content : " + new String(v2, "ISO-8859-1"));
                        } catch (IOException v3) {
                            v3.printStackTrace();
                        } catch (IllegalStateException v3_1) {
                            v3_1.printStackTrace();
                        }
                    }

                    label_32:
                    Tutorial.this.call_link("HTTP_Execute");
                }
            };
            XposedHelpers.findAndHookMethod("org.apache.http.impl.client.AbstractHttpClient", v3, "execute",
                    v5);

//            v3 = lpparam.classLoader;
//            v5 = new Object[v10];
//            v5[0] = Integer.TYPE;
//            v5[1] = new XC_MethodHook() {
//                protected void afterHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
//                }
//
//                protected void beforeHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
//                    if (param.args[0].intValue() == 1) {
//                        Tutorial.this.isGetRunningTask = true;
//                    }
//
//                    Tutorial.this.t1 = System.currentTimeMillis();
//                }
//            };
//            XposedHelpers.findAndHookMethod("android.app.ActivityManager", v3, "getRunningTasks", v5);


//            XposedHelpers.findAndHookMethod("android.content.ComponentName", lpparam.classLoader, "getPackageName",
//                    new Object[]{new XC_MethodHook() {
//                        protected void afterHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
//                            if (Tutorial.this.isGetRunningTask) {
//                                Tutorial.this.t2 = System.currentTimeMillis();
//                                Tutorial.this.isGetRunningTask = false;
//                                Tutorial.this.t1 = 0;
//                                Tutorial.this.t2 = 1000000;
//                            }
//                        }
//
//                        protected void beforeHookedMethod(XC_MethodHook$MethodHookParam param) throws Throwable {
//                        }
//                    }});
//



//    @SuppressLint(value = {"NewApi"})
//    public void pkg_check(Context c) {
//        Iterator v4 = c.getPackageManager().getInstalledPackages(1).iterator();
//        while (v4.hasNext()) {
//            Object v2 = v4.next();
//            if (!new Date(((PackageInfo) v2).firstInstallTime).after(new Date(new Date().getTime() - 900000))
//                    ) {
//                continue;
//            }
//
//            if (((PackageInfo) v2).packageName.equals("com.speedsoftware.rootexplorer")) {
//                continue;
//            }
//
//            this.pkgFilter.put(((PackageInfo) v2).packageName, Boolean.valueOf(true));
//            this.Mylog("pkg check true : " + ((PackageInfo) v2).packageName);
//        }
//    }
}

