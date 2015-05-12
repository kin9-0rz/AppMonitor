package hook.xposed;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;
import util.Util;

public class XAbstractHttpClient extends XHook {
    // TODO add other network hook api
    private static final String className = "org.apache.http.impl.client.AbstractHttpClient";
    private static List<String> logList = null;
    private static XAbstractHttpClient xAbstractHttpClient;

    public static XAbstractHttpClient getInstance() {
        if (xAbstractHttpClient == null) {
            xAbstractHttpClient = new XAbstractHttpClient();
        }
        return xAbstractHttpClient;
    }

    public List<String> handleHttpGet(HttpHost httpHost, HttpGet httpGet) {
        List<String> logList = new ArrayList<String>();
        String host = httpHost.toURI().toString();
        String url = httpGet.getURI().toString();

        logList.add("time:" + Util.getSystemTime());
        logList.add("HTTP METHOD:" + httpGet.getMethod());
        logList.add("HOST:" + host);
        logList.add("URL:" + url);

        Logger.log("[=== AbstractHttpClient execute HttpGet ===] host : " + host);
        Logger.log("[=== AbstractHttpClient execute HttpGet ===] url  : " + url);

        Header[] header = httpGet.getAllHeaders();
        if (header != null) {
            for (int i = 0; i < header.length; i++) {
                logList.add(header[i].getName() + ":" + header[i].getValue());
                Logger.log("[=== AbstractHttpClient execute HttpGet ===] (Header) " + header[i].getName() + " : " + header[i].getValue());
            }
        }

        return logList;
    }

    public List<String> handleHttpPost(HttpHost httpHost, HttpPost httpPost) {
        List<String> logList = new ArrayList<String>();
        String host = httpHost.toURI().toString();
        String url = httpPost.getURI().toString();

        logList.add("time:" + Util.getSystemTime());
        logList.add("HTTP METHOD:" + httpPost.getMethod());
        logList.add("HOST:" + host);
        logList.add("URL:" + url);
        Logger.log("[=== AbstractHttpClient execute HttpPost ===] host : " + host);
        Logger.log("[=== AbstractHttpClient execute HttpPost ===] url  : " + url);

        Header[] header = httpPost.getAllHeaders();
        if (header != null) {
            for (int i = 0; i < header.length; i++) {
                logList.add(header[i].getName() + ":" + header[i].getValue());
                Logger.log("[=== AbstractHttpClient execute HttpPost ===] (Header) " + header[i].getName() + " : " + header[i].getValue());
            }
        }

        HttpEntity entity = httpPost.getEntity();
        String contentType = null;
        if (entity.getContentType() != null) {
            contentType = entity.getContentType().getValue();
            if (URLEncodedUtils.CONTENT_TYPE.equals(contentType)) {
                try {
                    byte[] data = new byte[(int) entity.getContentLength()];
                    entity.getContent().read(data);
                    String content = new String(data, HTTP.DEFAULT_CONTENT_CHARSET);
                    logList.add("HTTP POST CONTENT:" + content);

                    Logger.log("[=== AbstractHttpClient execute HttpPost ===] Content  : " + content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (contentType.startsWith(HTTP.DEFAULT_CONTENT_TYPE)) {
                try {
                    byte[] data = new byte[(int) entity.getContentLength()];
                    entity.getContent().read(data);
                    String content = new String(data, contentType.substring(contentType.lastIndexOf("=") + 1));
                    logList.add("HTTP POST CONTENT:" + content);

                    Logger.log("[=== AbstractHttpClient execute HttpPost ===] Content : " + content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                byte[] data = new byte[(int) entity.getContentLength()];
                entity.getContent().read(data);
                String content = new String(data, HTTP.DEFAULT_CONTENT_CHARSET);
                logList.add("HTTP POST CONTENT:" + content);

                Logger.log("[=== AbstractHttpClient execute HttpPost ===] Content : " + content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logList;
    }

    public List<String> handleResult(BasicHttpResponse respone) {
        List<String> logList = new ArrayList<String>();
        if (respone != null) {
            logList.add("result code:" + respone.getStatusLine().getStatusCode());
            Header[] header = respone.getAllHeaders();
            if (header != null) {
                for (int i = 0; i < header.length; i++) {
                    logList.add(header[i].getName() + ":" + header[i].getValue());
                    Logger.log("[=== AbstractHttpClient execute ===] (Header) " + header[i].getName() + " : " + header[i].getValue());
                }
            }
            String result = null;
            try {
                result = EntityUtils.toString(respone.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.replace("\r", "");
            logList.add("result:" + result);

            Logger.log("[=== AbstractHttpClient execute ===] result : " + result);
        } else {
            logList.add("result:null");
        }
        logList.add("****************************************");
        return logList;
    }

    public List<String> writeAppLog(HttpHost host) {
        List<String> logList = new ArrayList<String>();
        String time = Util.getSystemTime();
        logList.add("time:" + time);
        logList.add("action:--executed--");
        logList.add("function:execute");
        logList.add("url:" + host.toURI().toString());
        for (String log : logList) {
            XposedBridge.log(log);
        }

        return logList;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {

        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "execute",
                HttpHost.class, HttpRequest.class, HttpContext.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        HttpHost host = (HttpHost) param.args[0];
                        HttpRequestBase request = (HttpRequestBase) param.args[1];
                        BasicHttpResponse respone = (BasicHttpResponse) param.getResult();
                        String callRef = Stack.getCallRef();


                        if (request instanceof HttpGet) {
                            HttpGet httpGet = (HttpGet) request;
                            logList = handleHttpGet(host, httpGet);
                            Util.writeNetLog(packageParam.packageName, logList);
                        } else if (request instanceof HttpPost) {
                            HttpPost httpPost = (HttpPost) request;
                            logList = handleHttpPost(host, httpPost);
                            Util.writeNetLog(packageParam.packageName, logList);
                        } else {
                            logList = handleResult(respone);
                            Logger.log("[=== AbstractHttpClient execute ===] host : " + host);
                            Util.writeNetLog(packageParam.packageName, logList);
                        }

                        Logger.log("[=== AbstractHttpClient execute ===] " + callRef);
                        logList = writeAppLog(host);
                        Util.writeLog(packageParam.packageName, logList);
                    }
                });
    }

}
