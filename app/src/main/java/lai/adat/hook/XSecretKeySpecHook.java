package lai.adat.hook;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import lai.adat.utils.Global;

/**
 * TODO 这里仅仅显示了密钥，可能还需要hook加密解密，这样看起来会好一些。
 *
 */
public class XSecretKeySpecHook extends MethodHook {
    private static final String mClassName = "javax.crypto.spec.SecretKeySpec";

    private XSecretKeySpecHook(Methods method) {
        super(mClassName, null);
    }

    // public SecretKeySpec(byte[] key, int offset, int len, String algorithm)
    // public SecretKeySpec(byte[] key, String algorithm)
    // libcore/luni/src/main/java/javax/crypto/spec/SecretKeySpec.java


    public static List<MethodHook> getMethodHookList() {
        List<MethodHook> methodHookList = new ArrayList<MethodHook>();
        for (Methods method : Methods.values())
            methodHookList.add(new XSecretKeySpecHook(method));

        return methodHookList;
    }

    @Override
    public void after(MethodHookParam param) throws Throwable {
        logSpecial(param);
    }

    private void logSpecial(MethodHookParam param) {

        byte[] key = null;
        String k = "";
        String algorithm = "";

        if (param.args.length > 0 && param.args[0] != null) {
            key = (byte[]) param.args[0];
        }

        if (param.args.length == 2 && param.args[1] != null) {
            algorithm = (String) param.args[1];
        } else if (param.args.length == 4 && param.args[3] != null) {
            algorithm = (String) param.args[3];
        }

        if (key != null) {
            for (int i = 0; i < key.length; i++) {
                k += (int) key[i];
                k += ", ";
            }

            k = k.substring(0, k.length() - 2);
        }

        String logMsg = String.format(
                "=== javax.crypto.spec.SecretKeySpec === {\"key\": \"%s\", \"algorithm\": \"%s\"}", k, algorithm);

        Log.v(Global.LOG_TAG, logMsg);
    }

    private enum Methods {
        SecretKeySpec
    }
}
