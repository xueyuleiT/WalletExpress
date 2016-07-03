package com.express.wallet.walletexpress.utils;

import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONObject;

import cn.pedant.SafeWebViewBridge.JsCallback;
import de.greenrobot.event.EventBus;

/**
 * Created by ricky on 15-10-16.
 */
public class HostJsScope {

    /**
     * selfie
     *
     * @param webView
     * @param msg
     * @param object
     */
    public static void invoke(WebView webView, String msg, String object) {

    }

    /**
     * close window
     *
     * @param webView
     * @param msg
     * @param object
     * @param jsCallback
     */
    public static void invoke(WebView webView, String msg, JSONObject object, final JsCallback jsCallback) {

    }



    /**
     * 获取联系人列表,获取实时地址,调用card io 扫描银行卡 注销
     *
     * @param webView
     * @param msg
     * @param object
     * @param jsCallback
     */
    public static void invoke(final WebView webView, final String msg, String object, final JsCallback jsCallback) {

    }
}
