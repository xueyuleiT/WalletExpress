package com.express.wallet.walletexpress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.express.wallet.walletexpress.listener.CustomChromeClient;
import com.express.wallet.walletexpress.listener.MessageCallBackEvent;
import com.express.wallet.walletexpress.model.MessageEvent;
import com.express.wallet.walletexpress.utils.CommonUtil;
import com.express.wallet.walletexpress.utils.HostJsScope;
import com.express.wallet.walletexpress.utils.SettingUtils;

import de.greenrobot.event.EventBus;

public class WebViewActivity extends UmengActivity implements MessageCallBackEvent {
    private Toolbar mToolbar;
    private TextView mTitle;
    private WebView webView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.titleLeft);
        findViewById(R.id.split).setVisibility(View.VISIBLE);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String url = getIntent().getStringExtra(CommonUtil.WEBACTIVITY_LINK);
        Log.d("","url =====>"+url);
        mTitle.setText(getIntent().getStringExtra(CommonUtil.WEBACTIVITY_TITLE));
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        setWebViewSettings(webView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            WebView.setWebContentsDebuggingEnabled(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                if (BuildConfig.DEBUG) {
                    CookieManager cookieManager = CookieManager.getInstance();
                    String CookieStr = cookieManager.getCookie(CommonUtil.DOMAIN);
                    Log.d("","CookieStr ===>"+CookieStr);
                }
                CookieManager cookieManager = CookieManager.getInstance();
                String cookie = cookieManager.getCookie(CommonUtil.DOMAIN);
                if (cookie.contains(CommonUtil.OPEN_ID+"=")){
                    String[] arr = cookie.split(";");
                    String temp = arr[arr.length-1];
                    int index = temp.indexOf("=");
                    if (index != -1){
                        CommonUtil.COOKIE = temp.substring(index+1);
                        SettingUtils.set(WebViewActivity.this,CommonUtil.OPEN_ID,CommonUtil.COOKIE);
                    }
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                }
            });
        webView.setWebChromeClient(new CustomChromeClient(CommonUtil.WEIXINJSBRIDGE, HostJsScope.class) {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });
        webView.loadUrl(url);

    }

    @Override
    public void onEvent(MessageEvent event) {

    }

    public void setWebViewSettings(final WebView webView) {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString(CommonUtil.WEBVIEW_PARAM_USER_AGENT);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.getSettings().setLightTouchEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setFocusable(true);
        webView.setClickable(true);
        webView.setHapticFeedbackEnabled(true);
        webView.setFocusableInTouchMode(true);
        webView.getSettings().setUseWideViewPort(true);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
