package wang.julis.jwbase.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.DisplayMetrics;
import android.view.View;

import wang.julis.jwbase.basecompact.NaApplication;


public class CommonUtils {


    public static int getWidth(Context context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getHeight(Context context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
    public static float dip2px(float length) {
        float scale = NaApplication.getApp().getResources().getDisplayMetrics().density;
        return (int) (length * scale + 0.5f);
    }

    /**
     * try get host activity from view.
     * views hosted on floating window like dialog and toast will sure return null.
     *
     * @return host activity; or null if not available
     */
    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
