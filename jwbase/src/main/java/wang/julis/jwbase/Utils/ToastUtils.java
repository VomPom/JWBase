package wang.julis.jwbase.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import wang.julis.jwbase.basecompact.NaApplication;


/*******************************************************
 *
 * Created by julis.wang on 2019/04/11 11:47
 *
 * Description :
 * History   :
 *
 *******************************************************/
public class ToastUtils {
    private static Toast mToast;
    public static void showToast(Context context,String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public static void showToast(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            try {
                if (mToast == null) {
                    mToast = Toast.makeText(NaApplication.getApp(), text, Toast.LENGTH_LONG);
                    mToast.setGravity(17, 0, 0);
                } else {
                    mToast.setDuration(Toast.LENGTH_LONG);
                    mToast.setText(text);
                }

                mToast.show();
            } catch (Exception var2) {
                var2.printStackTrace();
            }

        }
    }

    public static void showToastShort(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            try {
                if (mToast == null) {
                    mToast = Toast.makeText(NaApplication.getApp(), text, Toast.LENGTH_SHORT);
                    mToast.setGravity(17, 0, 0);
                } else {
                    mToast.setDuration(Toast.LENGTH_SHORT);
                    mToast.setText(text);
                }

                mToast.show();
            } catch (Exception var2) {
                var2.printStackTrace();
            }

        }
    }

    public static void showToast(int resId) {
        showToast(NaApplication.getApp().getResources().getText(resId));
    }

    public static void closeToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }

    }
//    public static void showSnackbarToast(View view, String inFo) {
//        Snackbar.make(view,inFo,3000).show();
//    }
}
