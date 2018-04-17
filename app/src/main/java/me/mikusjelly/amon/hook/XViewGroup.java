package me.mikusjelly.amon.hook;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import me.mikusjelly.amon.utils.Logger;

/**
 * 需要做过滤，所以，不能放在外面。
 */
public class XViewGroup extends MethodHook {
    private static final String mClassName = "android.view.ViewGroup";
    private Methods mMethod;

    private XViewGroup(Methods method) {
        super(mClassName, method.name());
        mMethod = method;
    }


    public static List<MethodHook> getMethodHookList() {
        List<MethodHook> methodHookList = new ArrayList<MethodHook>();
        for (Methods method : Methods.values()) {
            methodHookList.add(new XViewGroup(method));
        }
        return methodHookList;
    }

//    void	addView(View child, int index, ViewGroup.LayoutParams params)
//    void	addView(View child, ViewGroup.LayoutParams params)
//    void	addView(View child, int index)
//    void	addView(View child)
//    void	addView(View child, int width, int height)

    @Override
    public void before(MethodHookParam param) throws Throwable {
        String argNames = null;

        if (mMethod == Methods.addView) {
            View view = (View) param.args[0];
            String viewName = view.getClass().getName();
            if (viewName.startsWith("android.widget.") || viewName.startsWith("android.view.")
                    || viewName.startsWith("android.support.v7.widget.") || viewName.startsWith("android.support.v7.internal.widget.")
                    || viewName.startsWith("com.android.internal.widget.") || viewName.startsWith("com.android.internal.view")) {
                return;
            }

            int len = param.args.length;
            if (len == 1) {
                argNames = "child";
            } else if (len == 2) {
                if (param.args[1] instanceof Integer) {
                    argNames = "child|index";
                } else {
                    argNames = "child|params";
                }
            } else {
                if (param.args[2] instanceof Integer) {
                    argNames = "child|width|height";
                } else {
                    argNames = "child|index|params";
                }
            }
        }

        log(Logger.LEVEL_MID, param, argNames);
    }


    private enum Methods {
        addView
    }

}
