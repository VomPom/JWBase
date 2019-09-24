package wang.julis.jwbase.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

import wang.julis.jwbase.R;
import wang.julis.jwbase.basecompact.BaseActivity;


/*******************************************************
 *
 * Created by julis.wang@beibei.com on 2019/09/23 09:57
 *
 * Description :
 * History   :
 *
 *******************************************************/
public class WebViewActivity extends BaseActivity {
    private final static int FINISH_ACTIVITY = 0;
    private final static int REQUEST_UPLOAD_FILE_CODE = 2;
    private ValueCallback<Uri> mUploadFile;
    private final String TAG = "WebViewActivity ";
    private WebView webview;
    private String url;
    private Handler handler = new MyHandler(this);

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_webview;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        webview = (WebView) findViewById(R.id.webview);
        url = getIntent().getStringExtra("url");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.color.grey));
        //设置webview的settings和client
        configWebview();
        // 加载 页面
        loadURL();
    }

    private void setActionbarTitle() {
        String title = getIntent().getStringExtra("title");
        if (title != null)
            getActionBar().setTitle(title);
        else
            getActionBar().setTitle(R.string.app_name);
    }

    private void loadURL() {
        try {
            webview.loadUrl(url);
        } catch (Exception e) {
            Log.e(TAG + url, e.getLocalizedMessage());
        }
    }

    @Override
    protected void onDestroy() {
        webview.destroy();
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    private void configWebview() {
        // 允许javascript代码执行
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheMaxSize(8 * 1024 * 1024);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setDefaultTextEncodingName("utf-8");

        // 在当前页面打开链接，而不是启动用户手机上安装的浏览器打开
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);
                return true;
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            // 使webview可以更新进度条
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                WebViewActivity.this.setTitle("加载中……");
                WebViewActivity.this.setProgress(newProgress * 100);
                if (newProgress == 100)
                    setActionbarTitle();
            }

            //使JS alert可以以Android的AlertDiaolog形式弹出
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this)
                        .setMessage(message).setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                    }
                                });
                builder.setCancelable(true);
                builder.show();
                return true;
            }

            //html中上传点击input type为file的控件时会调用下列方法，在Android4.4中无效，待修复
            // Android 4.1+
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadFile,
                                        String acceptType, String capture) {
                openFileChooser(uploadFile);
            }

            // Android 3.0 +
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadFile,
                                        String acceptType) {
                openFileChooser(uploadFile);
            }

            // Android 3.0
            public void openFileChooser(ValueCallback<Uri> uploadFile) {
                mUploadFile = uploadFile;
                startActivityForResult(createCameraIntent(),
                        REQUEST_UPLOAD_FILE_CODE);

            }
        });
        // 在js中用window.injs.方法名来调用InJavaScript类中的方法
        webview.addJavascriptInterface(new InJavaScript(), "android");
        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    private Intent createCameraIntent() {
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);// 选择图片文件
        imageIntent.setType("image/*");
        return imageIntent;
    }

    // 使后退键可以达到网页回退功能，而不是关闭activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 给javascript调用的代码
     */
    private final class InJavaScript {
        //可以用JS关闭本Activity
        @android.webkit.JavascriptInterface
        public void finish() {
            handler.sendEmptyMessage(FINISH_ACTIVITY);
        }

        //可以用JS触发一个分享文本信息的intent
        @android.webkit.JavascriptInterface
        public void sharelink(String link) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "分享");
            i.putExtra(Intent.EXTRA_TEXT, "share this:" + link);
            startActivity(Intent.createChooser(i, "请选择分享方式"));
        }
    }

    /*
     * (non-Javadoc)左上角回退可以结束本activity,另有前进、后退、刷新
     *
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //选择文件后回调给JS一个URI
        if (requestCode == REQUEST_UPLOAD_FILE_CODE && resultCode == RESULT_OK) {
            if (null == mUploadFile)
                return;
            Uri result = (null == data) ? null : data.getData();//注，此处data.getData(),若为data则仅是contentProvider的地址将不能为JS识别
            if (null != result) {
                mUploadFile.onReceiveValue(result);
                mUploadFile = null;
            }
            //如果用户取消了选择文件操作，必须回调一个null的URI给JS，否则webview将会死掉
        } else if (requestCode == REQUEST_UPLOAD_FILE_CODE && resultCode == RESULT_CANCELED) {
            Uri result = null;
            mUploadFile.onReceiveValue(result);
            mUploadFile = null;
        }

    }

    //用来处理UI操作的handler，可扩展，暂无太大用处……
    private static class MyHandler extends Handler {
        WeakReference<WebViewActivity> weakReference;

        public MyHandler(WebViewActivity webViewActivity) {
            weakReference = new WeakReference<WebViewActivity>(webViewActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH_ACTIVITY:
                    weakReference.get().finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}