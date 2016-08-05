package com.express.wallet.walletexpress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.express.wallet.walletexpress.fragments.HomeFragment;
import com.express.wallet.walletexpress.fragments.PlaceholderFragment;
import com.express.wallet.walletexpress.fragments.MyFragment;
import com.express.wallet.walletexpress.fragments.CreditFragment;
import com.express.wallet.walletexpress.listener.BackHandledInterface;
import com.express.wallet.walletexpress.utils.CommonUtil;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayOutputStream;

public class MainActivity extends UmengActivity implements BackHandledInterface{
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private RadioGroup radioGroup;
    private IWXAPI iwxapi;
    TextView tvTitle;
    ImageView rightImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        initView();
    }

    void initView(){

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tvTitle = (TextView) findViewById(R.id.title);
        rightImg = (ImageView) findViewById(R.id.rightImg);

        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.mipmap.more);

        rightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share2WeiXin(iwxapi);
            }
        });

        fragmentManager = getSupportFragmentManager();
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        ((RadioButton)radioGroup.findViewById(R.id.imgOne)).setChecked(true);

        transaction = fragmentManager.beginTransaction();


        tvTitle.setText("借款");
        HomeFragment homeFragment = new HomeFragment();
        transaction.replace(R.id.contentView, homeFragment);
        transaction.commit();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.imgOne:
                        tvTitle.setText("借款");
                        transaction = fragmentManager.beginTransaction();
                        HomeFragment homeFragment = new HomeFragment();
                        transaction.replace(R.id.contentView, homeFragment);
                        transaction.commit();
                        break;
                    case R.id.imgTwo:
                        tvTitle.setText("信用卡");
                        transaction = fragmentManager.beginTransaction();
                        CreditFragment twoFragment = new CreditFragment();
                        transaction.replace(R.id.contentView, twoFragment);
                        transaction.commit();
                        break;

                    case R.id.imgThree:
                        tvTitle.setText("我的");
                        transaction = fragmentManager.beginTransaction();
                        MyFragment threeFragment = new MyFragment();
                        transaction.replace(R.id.contentView, threeFragment);
                        transaction.commit();
                        break;


                }
            }
        });

        iwxapi = WXAPIFactory.createWXAPI(this, CommonUtil.WEIXIN_APP_ID);
        iwxapi.registerApp(CommonUtil.WEIXIN_APP_ID);
    }

    @Override
    public void onBackPressed() {
        if (mPlaceholderFragment == null|| !mPlaceholderFragment.onBackPressed()) {
            //处理
            doubleClickExitApp();
        }
    }

    private void doubleClickExitApp() {
            if (CommonUtil.downTime == 0) {
                CommonUtil.downTime = System.currentTimeMillis();
                showToast(getResources().getString(R.string.double_click_exit_app));
                return;
            }
            long lastDownTime = System.currentTimeMillis();
            if ((lastDownTime - CommonUtil.downTime) > 1000) {
                CommonUtil.downTime = lastDownTime;
                showToast(getResources().getString(R.string.double_click_exit_app));
            } else {
                try {
                    MobclickAgent.onKillProcess(this);
                }catch (Exception e){
                }
                System.exit(0);
                finish();
            }

    }


    PlaceholderFragment mPlaceholderFragment;
    @Override
    public void setSelectedFragment(PlaceholderFragment selectedFragment) {
        this.mPlaceholderFragment = selectedFragment;
    }

    protected void share2WeiXin(IWXAPI iwxapi) {
        if (iwxapi.isWXAppInstalled()) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = CommonUtil.REWARD_URL;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = "急需现金,找简借";
            msg.description = "简单的借款流程,数百至数万随意借，秒到账。";
            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
            msg.thumbData = bmpToByteArray(thumb, true);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            // 调用api接口发送数据到微信
            iwxapi.sendReq(req);
        } else {
            showToast("未安装微信");
        }

    }
    public byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
