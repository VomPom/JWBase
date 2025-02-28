package wang.julis.jwbase.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

public class MarkdownWebView extends WebView {

    private static final String TAG = MarkdownWebView.class.getSimpleName();

    private static final String HTML_LOCATION = "file:///android_asset/AndroidMarkdown.html";

    String mText;

    private boolean mWebViewLoaded = false;

    public MarkdownWebView(Context context) {
        this(context, null);
    }

    public MarkdownWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkdownWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MarkdownWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        this.init(context);
    }

    void init(Context context) {

        {
            this.setBackgroundColor(Color.TRANSPARENT);

            // be careful, we do not need internet access
            this.getSettings().setBlockNetworkLoads(true);

            //
            this.getSettings().setLoadWithOverviewMode(true);
            this.getSettings().setJavaScriptEnabled(true);
//            this.getSettings().setUseWideViewPort(true);
        }

        if (false) {
            // caching
            File dir = context.getCacheDir();
            if (!dir.exists()) {
                Log.d(TAG, "directory does not exist");
                boolean mkdirsStatus = dir.mkdirs();
                if (!mkdirsStatus) {
                    Log.e(TAG, "directory creation failed");
                }
            }

            getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        }

        {
            this.mWebViewLoaded = false;
            this.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    if (mWebViewLoaded) {
                        // WebView was already finished
                        // do not load content again
                        return;
                    }

                    mWebViewLoaded = true;
                    if (!TextUtils.isEmpty(mText)) {
                        setText(mText);
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    Uri uri = request.getUrl();
                    String scheme = uri.getScheme();
                    if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
                        open(view.getContext(), uri);

                        return true;
                    }

                    return super.shouldOverrideUrlLoading(view, request);
                }

            });
        }

        {
            this.loadUrl(HTML_LOCATION);
        }
    }

    public void setText(String text) {
        this.mText = text;

        //wait for WebView to finish loading
        if (!mWebViewLoaded) {
            return;
        }

        String escapeText;
        if (text != null) {
            escapeText = escape(text);
        } else {
            escapeText = "";
        }

        String javascriptCommand = "javascript:setText(\'" + escapeText + "\')";
        this.loadUrl(javascriptCommand);
    }

    static final boolean open(Context context, Uri uri) {
        int launchFlags = Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(launchFlags);

        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {

        }

        return false;
    }

    static String escape(String s) {

        StringBuilder out = new StringBuilder(s.length() + 128);

        for (int i = 0, length = s.length(); i < length; i++) {
            char c = s.charAt(i);

            /*
             * From RFC 4627, "All Unicode characters may be placed within the
             * quotation marks except for the characters that must be escaped:
             * quotation mark, reverse solidus, and the control characters
             * (U+0000 through U+001F)."
             */
            switch (c) {
                case '"':
                case '\\':
                case '/':
                    out.append('\\').append(c);
                    break;

                case '\t':
                    out.append("\\t");
                    break;

                case '\b':
                    out.append("\\b");
                    break;

                case '\n':
                    out.append("\\n");
                    break;

                case '\r':
                    out.append("\\r");
                    break;

                case '\f':
                    out.append("\\f");
                    break;

                default:
                    if (c <= 0x1F) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
                    break;
            }

        }

        return out.toString();
    }

}
