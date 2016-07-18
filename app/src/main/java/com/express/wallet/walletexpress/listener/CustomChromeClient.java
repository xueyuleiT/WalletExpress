package com.express.wallet.walletexpress.listener;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebView;

import cn.pedant.SafeWebViewBridge.InjectedChromeClient;

/**
 * Created by ricky on 15-10-26.
 */
public class CustomChromeClient extends InjectedChromeClient {

    public CustomChromeClient(String injectedName, Class injectedCls) {
        super(injectedName, injectedCls);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}
