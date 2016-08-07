package com.express.wallet.walletexpress.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    //引导页是否展示过
    public static final  String SPASH_SHOW = "SPASH_SHOW";
    public static final  String TOKEN = "token";
    public static final String DOMAIN = "http://jj.zljianjie.com";
    public static final int SITE_ID = 122811;
    public static final String OPEN_ID = "zr_login_openid";
    public static String COOKIE = "";
    public static final String WEIXIN_APP_ID = "wxdb088041d2ef1e1d";//offcial

    public static final  String URL_TOKEN = "YitniN ";
    public static final String KEY = "jj.zljianjie.com";

    public static final String SUGGEST_URL = "http://jj.zljianjie.com/public/api_zsjr/prods?price=";
    public static final String CREDIT_URL = "http://xyk.zljianjie.com";
    public static final String MY_URL = "http://jj.zljianjie.com/public/api_zsjr/user_center.html?v5=1";
    public static final String REWARD_URL = "http://jj.zljianjie.com/public/api_zsjr/news?id=5&v5=1";
    public static final String REGISTER_AGREEMENT_URL = "http://jj.zljianjie.com/public/api_zsjr/pact.html?v5=1";
    public static final String TAB_2_PAGE = "http:// jj.zljianjie.com/public/api_zsjr";

    public static final String REQUEST_PARAM_USER_AGENT = "com.express.wallet.walletexpress.v1.0.0";
    public static final String WEBVIEW_PARAM_USER_AGENT = "walletexpress";
    public static final String WEIXINJSBRIDGE= "WeixinJSBridge";
    public static final String WEBACTIVITY_LINK = "web_activity_link";
    public static final String WEBACTIVITY_TITLE = "web_activity_title";
    public static final String HTTPREQUEST_COOKIE = "Cookie";
    public static String JSESSIONID = "";
    public static String SERVERID = "";
    public static long downTime = 0;


    public static int screem_width,screem_height;
    public static String PHONE_PATTERN ="^[1][3,4,7,5,8][0-9]{9}$";
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
    }

    /**
     * 校验电话号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneValid(String phone) {

        if (TextUtils.isEmpty(phone)){
            return false;
        }

        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
    public static void setCookies(retrofit2.Response response, Context context){
        for (String string:response.raw().headers().values("Set-Cookie")){
            Log.d("","headers ===>"+string);
            if (string.contains("JSESSIONID") && !JSESSIONID.equals(string)){
                JSESSIONID = string.split(";")[0];
            }else if (string.contains("SERVERID") && !SERVERID.equals(string)){
                SERVERID = string.split(";")[0];
            }
        }
    }

    public static void syncCookie(Context context,String cookie){

        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(DOMAIN, "zr_login_sn="+cookie);
        cookieManager.setCookie(DOMAIN, CommonUtil.OPEN_ID+"="+SettingUtils.get(context,CommonUtil.OPEN_ID,""));
        CookieSyncManager.getInstance().sync();

    }

    public static String getDeviceId(Context mContext) {

        String id;
        try {
            TelephonyManager telephonyManager = (TelephonyManager)
                    mContext.getSystemService(Context.TELEPHONY_SERVICE);
            id = telephonyManager.getDeviceId();

        } catch (Exception e) {
            id = "";
        }

        if (TextUtils.isEmpty(id)){
            id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        if (TextUtils.isEmpty(id)){
            id = Installation.id(mContext);
        }

        if (TextUtils.isEmpty(id)){
            id = "";
        }

        return id;
    }

}
