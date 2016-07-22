package com.express.wallet.walletexpress.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.express.wallet.walletexpress.R;
import com.express.wallet.walletexpress.RegisterActivity;
import com.express.wallet.walletexpress.model.CookieCallBack;
import com.express.wallet.walletexpress.model.MainResResponse;
import com.express.wallet.walletexpress.model.RegisterOrLoginRequest;
import com.express.wallet.walletexpress.model.ResponseInfo;
import com.express.wallet.walletexpress.utils.CommonUtil;
import com.express.wallet.walletexpress.utils.OkhttpTask;
import com.express.wallet.walletexpress.utils.SettingUtils;
import com.express.wallet.walletexpress.utils.WalletUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by cashbus on 6/22/16.
 */
public class HomeFragment extends BasicFragment implements View.OnClickListener{

    View rootView;
    Button loan;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home,null);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loan = (Button) rootView.findViewById(R.id.loan);
        loan.setOnClickListener(this);
        getMainRes();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loan:
                if (TextUtils.isEmpty(SettingUtils.get(getActivity(), CommonUtil.TOKEN,""))){
                    startActivity(new Intent(getActivity(), RegisterActivity.class));
                }
                break;
        }
    }

    void getMainRes(){
        OkhttpTask okhttpTask = CommonUtil.getTask(CommonUtil.DOMAIN);
        Call<MainResResponse> call = null;
        try {
            String crypt = WalletUtil.base64Encode(WalletUtil.getSignature(CommonUtil.URL_TOKEN+CommonUtil.SITE_ID,CommonUtil.KEY).getBytes());

            call = okhttpTask.getMainRes(CommonUtil.SITE_ID,0,java.net.URLEncoder.encode(crypt,"utf-8"));
            call.enqueue(new CookieCallBack<MainResResponse>(){
                @Override
                public void onResponse(Call<MainResResponse> call, Response<MainResResponse> response) {
                    super.onResponse(call,response);
                    MainResResponse mainResResponse = response.body();
                    if (mainResResponse != null){
                        if (mainResResponse.getMsg() == 0){

                        }else if (mainResResponse.getMsg() == 1){
                            Toast.makeText(getActivity(),"无效的站点编号",Toast.LENGTH_SHORT).show();
                        }else if (mainResResponse.getMsg() == 9){
                            Toast.makeText(getActivity(),"签名错误",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(),"返回异常",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MainResResponse> call, Throwable t) {
                    super.onFailure(call,t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
