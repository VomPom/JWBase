package wang.julis.jwbase.basecompact;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import wang.julis.jwbase.Request.BaseApiRequest;
import wang.julis.jwbase.Request.RequestQueueUtils;

/*******************************************************
 *
 * Created by julis.wang on 2019/04/29 10:03
 *
 * Description :
 * History   :
 *
 *******************************************************/

public abstract class BaseActivity extends AppCompatActivity {
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
        initData();
        initView();
        ctx = this;
        if (isSetStatusBar) {
            steepStatusBar();
        }

        if (!isAllowScreenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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
        // 透明状态栏
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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
     * @param setScreenRotate
     */
    public void setScreenRotate(boolean setScreenRotate) {
        this.isAllowScreenRotate = setScreenRotate;
    }

    /**
     * 设置 actionBar title 以及 up 按钮事件
     *
     * @param title
     */
    protected void initActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            if (null != title) {
                actionBar.setTitle(title);
            }

            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            finishActivity(RESULT_OK);
        }
    }

    protected void addRequestToQueue(BaseApiRequest request) {
        RequestQueueUtils.getInstance().addRequestToQueue(request);
    }


}
