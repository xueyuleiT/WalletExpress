package com.express.wallet.walletexpress.utils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zenghui on 7/2/16.
 */
public interface OkhttpTask {

    @GET("/app_get_code")
    Call<String> getPlanStatus(@Query("mobile") String mobile,@Query("sign") String sign);

}
