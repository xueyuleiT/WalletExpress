package com.express.wallet.walletexpress.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zenghui on 7/2/16.
 */
public class CommonUtil {

    public static final String DOMAIN = "http:// jj.zljianjie.com/public/api_zsjr";

    public static final String GUIDE_URL = "http://jj.zljianjie.com/public/api_zsjr/guide.html?v5=1";
    public static final String REWARD_URL = "http://jj.zljianjie.com/public/api_zsjr/news?id=5&v5=1";

    public static final String TAB_2_PAGE = "http:// jj.zljianjie.com/public/api_zsjr";

    public static final String REQUEST_PARAM_USER_AGENT = "com.express.wallet.walletexpress.v1.0.0";
    public static final String WEBVIEW_PARAM_USER_AGENT = "com.express.wallet.walletexpress.v1.0.0.micromessenger";
    public static final String WEIXINJSBRIDGE= "WeixinJSBridge";
    public static final String WEBACTIVITY_LINK = "web_activity_link";
    public static final String HTTPREQUEST_COOKIE = "Cookie";
    public static String JSESSIONID = "";
    public static String SERVERID = "";
    public static long downTime = 0;

    public static int screem_width,screem_height;

    public static OkhttpTask getTask(final String apiUrl) {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request;

                        if (!TextUtils.isEmpty(JSESSIONID) || !TextUtils.isEmpty(SERVERID)){
                            StringBuilder value = new StringBuilder();
                            if (!TextUtils.isEmpty(JSESSIONID)) {
                                value.append(JSESSIONID);
                                if (!TextUtils.isEmpty(SERVERID)){
                                    value.append(";"+SERVERID);
                                }
                            } else {
                                value.append(SERVERID);
                                if (!TextUtils.isEmpty(JSESSIONID)){
                                    value.append(";"+JSESSIONID);
                                }
                            }

                            Log.d("","value =====>"+value);
                            request = chain.request()
                                    .newBuilder()
                                    .addHeader(HTTPREQUEST_COOKIE, value.toString())
                                    .build();
                        }else{
                            request = chain.request()
                                    .newBuilder()
                                    .build();
                        }

                        Response originalResponse = chain.proceed(request);
                        return originalResponse;
                    }
                })
//                .cookieJar(new CookiesManager())
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)

                .build();


        OkhttpTask task = retrofit.create(OkhttpTask.class);

        return task;
    }
    public static void setWebViewSettings(final WebView webView) {

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
        webView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!webView.hasFocus()) {
                            v.requestFocusFromTouch();
                        }
                        break;
                }
                return false;
            }
        });
    }
}
