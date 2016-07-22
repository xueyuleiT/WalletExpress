package com.express.wallet.walletexpress;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.express.wallet.walletexpress.model.CookieCallBack;
import com.express.wallet.walletexpress.model.RegisterOrLoginRequest;
import com.express.wallet.walletexpress.model.ResponseInfo;
import com.express.wallet.walletexpress.utils.CommonUtil;
import com.express.wallet.walletexpress.utils.OkhttpTask;
import com.express.wallet.walletexpress.utils.WalletUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zenghui on 16/7/17.
 */
public class RegisterActivity extends UmengActivity implements View.OnClickListener{

    TextView tvMsgCode;
    EditText phone;
    Button register;
    EditText inviteCode,edtMsgCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) findViewById(R.id.titleLeft);
        findViewById(R.id.split).setVisibility(View.VISIBLE);
        mToolbar.setTitle("");
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText("注册");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.agreement).setOnClickListener(this);
        register = (Button) findViewById(R.id.register);
        phone = (EditText) findViewById(R.id.phone);
        tvMsgCode = (TextView) findViewById(R.id.tvMsgCode);
        inviteCode = (EditText) findViewById(R.id.inviteCode);
        edtMsgCode = (EditText) findViewById(R.id.edtMsgCode);

        register.setOnClickListener(this);
        tvMsgCode.setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:

                if (!CommonUtil.isPhoneValid(phone.getText().toString())){
                    showToast("手机号不正确");
                    return;
                }

                if (TextUtils.isEmpty(edtMsgCode.getText().toString()) || edtMsgCode.getText().length() < 4){
                    showToast("验证码不正确");
                    return;
                }

                register();

                break;
            case R.id.agreement:
                Intent intent = new Intent(this,WebViewActivity.class);
                intent.putExtra(CommonUtil.WEBACTIVITY_LINK, CommonUtil.REGISTER_AGREEMENT_URL);
                intent.putExtra(CommonUtil.WEBACTIVITY_TITLE, "注册协议");
                startActivity(intent);
                break;
            case R.id.tvMsgCode:
                if (!CommonUtil.isPhoneValid(phone.getText().toString())){
                    showToast("请输入正确的手机号");
                    return;
                }

                tvMsgCode.setEnabled(false);
                getMsgCode(phone.getText().toString());
                break;
        }
    }

    void getMsgCode(String phone){
        OkhttpTask okhttpTask = CommonUtil.getTask(CommonUtil.DOMAIN);
        Call<ResponseInfo> call = null;
        try {
            String crypt = WalletUtil.base64Encode(WalletUtil.getSignature(CommonUtil.URL_TOKEN+phone,CommonUtil.KEY).getBytes());
            call = okhttpTask.getMsgCode(phone,java.net.URLEncoder.encode(crypt,"utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        call.enqueue(new CookieCallBack<ResponseInfo>(){
            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                super.onResponse(call, response);
                ResponseInfo responseInfo = response.body();
                if (responseInfo != null ){
                    if ( responseInfo.getMsg() == 0){
                        showToast("发送成功");
                    }else if ( responseInfo.getMsg() == 1){
                        showToast("无效的手机号");
                    }else if ( responseInfo.getMsg() == 2){
                        showToast("短信发送失败");
                    }else if ( responseInfo.getMsg() == 3){
                        showToast("重复请求，请稍后重试");
                    }else if ( responseInfo.getMsg() == 9){
                        showToast("签名错误");
                    }
                    showToast(""+responseInfo.getMsg());
                }else {
                    showToast("获取失败");
                }
            }

            @Override
            public void onFailure(Call<ResponseInfo> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    void register(){
        OkhttpTask okhttpTask = CommonUtil.getTask(CommonUtil.DOMAIN);
        Call<ResponseInfo> call = null;
        try {
            String crypt = WalletUtil.base64Encode(WalletUtil.getSignature(CommonUtil.URL_TOKEN+phone,CommonUtil.KEY).getBytes());
            RegisterOrLoginRequest registerOrLoginRequest = new RegisterOrLoginRequest();
            registerOrLoginRequest.setMobile(phone.getText().toString());
            registerOrLoginRequest.setCode(edtMsgCode.getText().toString());
            registerOrLoginRequest.setSign(java.net.URLEncoder.encode(crypt,"utf-8"));
            registerOrLoginRequest.setDevice("");
            registerOrLoginRequest.setSn("");
            call = okhttpTask.registerOrLogin(registerOrLoginRequest);
            call.enqueue(new CookieCallBack<ResponseInfo>(){
                @Override
                public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                    super.onResponse(call,response);
                }

                @Override
                public void onFailure(Call<ResponseInfo> call, Throwable t) {
                    super.onFailure(call,t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
