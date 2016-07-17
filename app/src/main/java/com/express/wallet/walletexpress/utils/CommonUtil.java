package com.express.wallet.walletexpress.utils;

import android.text.TextUtils;
import android.util.Log;

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

}
