package com.express.wallet.walletexpress.fragments;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.express.wallet.walletexpress.BuildConfig;
import com.express.wallet.walletexpress.R;
import com.express.wallet.walletexpress.listener.CustomChromeClient;
import com.express.wallet.walletexpress.utils.CommonUtil;
import com.express.wallet.walletexpress.utils.HostJsScope;
import com.express.wallet.walletexpress.utils.SettingUtils;

/**
 * Created by cashbus on 6/22/16.
 */
public class MyFragment extends  PlaceholderFragment{
    private WebView webView;
    private ProgressBar progressBar;
    View rootView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.web_layout,null);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView = (WebView) rootView.findViewById(R.id.webView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        CommonUtil.setWebViewSettings(webView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            WebView.setWebContentsDebuggingEnabled(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadHistoryUrls.add(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                if (BuildConfig.DEBUG) {
                    CookieManager cookieManager = CookieManager.getInstance();
                    String CookieStr = cookieManager.getCookie(url);
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
                            SettingUtils.set(getActivity(),CommonUtil.OPEN_ID,CommonUtil.COOKIE);
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
        webView.loadUrl(CommonUtil.MY_URL);
        loadHistoryUrls.add(CommonUtil.MY_URL);
    }

    @Override
    public boolean onBackPressed() {
        Log.d("","webView.canGoBack() =====>"+webView.canGoBack());
        if (webView.canGoBack()){

            //过滤是否为重定向后的链接
            if(loadHistoryUrls.size()>0&&loadHistoryUrls.get(loadHistoryUrls.size()-1).contains("index.html")) {
                //移除加载栈中的最后两个链接
                loadHistoryUrls.remove(loadHistoryUrls.get(loadHistoryUrls.size() - 1));
            }
            if (loadHistoryUrls.size() == 0){
                return false;
            }
            loadHistoryUrls.remove(loadHistoryUrls.get(loadHistoryUrls.size()-1));

            if (loadHistoryUrls.size() == 0){
                return false;
            }
            //加载重定向之前的页
            webView.loadUrl(loadHistoryUrls.get(loadHistoryUrls.size()-1));

//            webView.goBack();
            return true;
        }else {
            return false;
        }
    }

}
