package wang.julis.jwbase.basecompact;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import wang.julis.jwbase.LoadingDialog.LoadingDialog;

/*******************************************************
 *
 * Created by julis.wang on 2019/04/29 10:03
 *
 * Description :
 * History   :
 *
 *******************************************************/

public abstract class BaseActivity extends AppCompatActivity {
    private LoadingDialog loadingDialog;

    private static final String TAG = "BaseActivity";

    /**
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = false;
    /**
     * 是否允许全屏
     **/
    private boolean isAllowFullScreen = false;
    /**
     * 是否禁止旋转屏幕
     **/
    private boolean isAllowScreenRotate = true;
    /**
     * context
     **/
    protected Context ctx;

    /**
     * 初始化界面
     **/
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    protected abstract int getContentView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAllowFullScreen) {
            setFullScreen();
        }
        setContentView(getContentView());
        initView();
        initData();
        ctx = this;
        if (isSetStatusBar) {
            steepStatusBar();
        }

        if (!isAllowScreenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
    }

    /**
     * 窗口全屏
     */
    private void setFullScreen() {
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * [是否允许全屏]
     *
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.isAllowFullScreen = allowFullScreen;
    }

    /**
     * [是否设置沉浸状态栏]
     *
     * @param isSetStatusBar
     */
    public void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }

    /**
     * [是否允许屏幕旋转]
     *
     * @param isAllowScreenRoate
     */
    public void setScreenRoate(boolean isAllowScreenRoate) {
        this.isAllowScreenRotate = isAllowScreenRoate;
    }
    public void showLoadingDialog() {
        loadingDialog.showLoading();
    }
    public void stopLoadingDialog() {
        loadingDialog.stopLoading();
    }

//    public void requsetPermission() {
//        AndPermission.with(this)
//                .runtime()
//                .permission(Permission.Group.STORAGE)
//                .onGranted(permissions -> {
//                    // Storage permission are allowed.
//                })
//                .onDenied(permissions -> {
//                    // Storage permission are not allowed.
//                })
//                .start();
//    }
}
