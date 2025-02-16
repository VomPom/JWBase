package wang.julis.jwbase.utils;

/**
 * Created by julis.wang on 2018/12/12 15:55
 * <p>
 * Description:
 */

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by ddf on 17/7/22.
 */

public class StatusBarUtils {

    public static void setStatusBarColor(Activity activity, int color) {
        if (activity == null) {
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏颜色
                window.setStatusBarColor(color);
            }
        } catch (Exception pE) {
            pE.printStackTrace();
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorString 类似 #RRGGBB 或者 #AARRGGBB.
     */
    public static void setStatusBarColor(Activity activity, String colorString) {
        try {
            setStatusBarColor(activity, Color.parseColor(colorString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getDefaultStatusBarColor(Activity pActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return pActivity.getWindow().getStatusBarColor();
        }
        return Color.WHITE;
    }

    public static void translucentStatusBar(Activity activity, int color, boolean dark) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //乐视手机 View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 设置不生效，导致状态栏背景与文本颜色一样
            if ("Letv".equalsIgnoreCase(Build.MANUFACTURER) && color == Color.WHITE) {
                color = Color.parseColor("#e4e4e4");
            }
            window.setStatusBarColor(color);
            if (dark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }


            ViewGroup contentView = window.findViewById(Window.ID_ANDROID_CONTENT);
            View childView = contentView.getChildAt(0);
            if (childView != null) {
                ViewCompat.setFitsSystemWindows(childView, false);
                ViewCompat.requestApplyInsets(childView);
            }
        }
    }

}
