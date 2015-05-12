package hook.xposed;

import android.content.res.AssetManager;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import util.Logger;
import util.Stack;

public class XAssetManager extends XHook {
    private static final String className = AssetManager.class.getName();
    private static XAssetManager xAssetManager;

    public static XAssetManager getInstance() {
        if (xAssetManager == null) {
            xAssetManager = new XAssetManager();
        }
        return xAssetManager;
    }

    @Override
    void hook(final XC_LoadPackage.LoadPackageParam packageParam) {
        XposedHelpers.findAndHookMethod(className, packageParam.classLoader, "open",
                String.class, Integer.TYPE, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String fileName = (String) param.args[0];
                        String callRef = Stack.getCallRef();


                        Logger.log("[=== AssetManager open ===]");
                        Logger.log("[=== AssetManager open ===] " + fileName);
                        Logger.log("[=== AssetManager open ===] " + callRef);

                    }

                });

    }

}
