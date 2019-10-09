package wang.julis.jwbase.Permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by julis.wang on 2018/12/07 12:33
 * <p>
 * Description:
 */

public class PermissionsHelper {
    public PermissionsHelper() {
    }

    public static void showSettingPermissionsDialog(Activity context, @StringRes int messageResId) {
        showSettingPermissionsDialog(context, messageResId, false);
    }

    public static void showSettingPermissionsDialog(final Activity context, @StringRes int messageResId, final boolean finishCurrentActivity, final PermissionsHelper.NegativeCallBack negativeCallBack) {
        if (context != null) {
            (new AlertDialog.Builder(context)).setTitle("权限申请").setCancelable(false).setMessage(messageResId).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                public void onClick(@NonNull DialogInterface dialog, int which) {
                    context.startActivity(new Intent("android.settings.MANAGE_APPLICATIONS_SETTINGS"));
                    if (negativeCallBack != null) {
                        negativeCallBack.positiveCallback();
                    }

                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (finishCurrentActivity) {
                        context.finish();
                    }

                    if (negativeCallBack != null) {
                        negativeCallBack.negativeCallback();
                    }

                }
            }).show();
        }
    }

    public static void showSettingPermissionsDialog(Activity context, @StringRes int messageResId, boolean finishCurrentActivity) {
        showSettingPermissionsDialog(context, messageResId, finishCurrentActivity, (PermissionsHelper.NegativeCallBack)null);
    }

    public interface NegativeCallBack {
        void positiveCallback();

        void negativeCallback();
    }
}
