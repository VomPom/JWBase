package wang.julis.jwbase.LoadingDialog;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.victor.loading.rotate.RotateLoading;

import wang.julis.jwbase.R;


/**
 * Created by Julis on 2019/02/12 16:35
 * <p>
 * description:
 */
public class LoadingDialog {
    private Context context;
    private RotateLoading bookLoading;
    private Dialog dialog;
    private boolean isShowing = false;
    private TextView tvLoading;

    public LoadingDialog(@NonNull Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.sdk_loading_dialog, null, false);
        dialog = new Dialog(context, R.style.dialog_loading);
        dialog.setContentView(view);
        bookLoading = view.findViewById(R.id.book_loading);
        tvLoading = view.findViewById(R.id.tv_loading);
    }

    public void showLoading() {
        if (bookLoading != null && !bookLoading.isStart() && !isShowing) {
            dialog.show();
            isShowing = true;
            bookLoading.start();
        }
    }

    public void showLoading(String loadingText) {
        if (bookLoading != null && !bookLoading.isStart() && !isShowing) {
            dialog.show();
            isShowing = true;
            tvLoading.setText(loadingText);
            bookLoading.start();
        }
    }

    public void stopLoading() {
        if (bookLoading != null && bookLoading.isStart() && isShowing) {
            dialog.cancel();
            isShowing = false;
            bookLoading.stop();
        }
    }

}
