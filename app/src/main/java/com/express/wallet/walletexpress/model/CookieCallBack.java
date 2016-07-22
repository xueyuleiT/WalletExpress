package com.express.wallet.walletexpress.model;

import com.express.wallet.walletexpress.utils.CommonUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zenghui on 2016/7/17.
 */
public class CookieCallBack<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        CommonUtil.setCookies(response,null);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }
}
