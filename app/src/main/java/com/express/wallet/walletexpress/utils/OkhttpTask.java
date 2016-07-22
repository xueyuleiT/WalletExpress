package com.express.wallet.walletexpress.utils;

import com.express.wallet.walletexpress.model.MainResResponse;
import com.express.wallet.walletexpress.model.RegisterOrLoginRequest;
import com.express.wallet.walletexpress.model.ResponseInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zenghui on 7/2/16.
 */
public interface OkhttpTask {

    @FormUrlEncoded
    @POST("/public/api_zsjr/app_get_code")
    Call<ResponseInfo> getMsgCode(@Field("mobile") String mobile, @Field("sign") String sign);

    @POST("/public/api_zsjr/app_user_login")
    Call<ResponseInfo> registerOrLogin(@Body RegisterOrLoginRequest registerOrLoginRequest);

    @GET("/public/api_zsjr/app_main_res")
    Call<MainResResponse> getMainRes(@Query("site_id") int site_id, @Query("package") int mpackage, @Query("sign") String sign);
}
