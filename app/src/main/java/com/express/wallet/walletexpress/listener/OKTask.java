package com.express.wallet.walletexpress.listener;

import com.express.wallet.walletexpress.model.ResponseInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zenghui on 2016/7/20.
 */
public interface OKTask {

    @GET("url/app_get_code")
    Call<ResponseInfo> getAuth(@Query("mobile") String mobile,@Query("sign") String sign);
}
