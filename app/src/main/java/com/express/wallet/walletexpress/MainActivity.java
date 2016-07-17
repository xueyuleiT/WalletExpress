package com.express.wallet.walletexpress;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.express.wallet.walletexpress.fragments.OneFragment;
import com.express.wallet.walletexpress.fragments.ThreeFragment;
import com.express.wallet.walletexpress.fragments.TwoFragment;
import com.express.wallet.walletexpress.utils.CommonUtil;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends UmengActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private RadioGroup radioGroup;

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


        fragmentManager = getSupportFragmentManager();
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        ((RadioButton)radioGroup.findViewById(R.id.imgOne)).setChecked(true);

        transaction = fragmentManager.beginTransaction();


        tvTitle.setText("简贷");
        OneFragment oneFragment = new OneFragment();
        transaction.replace(R.id.contentView, oneFragment);
        transaction.commit();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.imgOne:
                        transaction = fragmentManager.beginTransaction();
                        OneFragment oneFragment = new OneFragment();
                        transaction.replace(R.id.contentView, oneFragment);
                        transaction.commit();
                        break;
                    case R.id.imgTwo:
                        transaction = fragmentManager.beginTransaction();
                        TwoFragment twoFragment = new TwoFragment();
                        transaction.replace(R.id.contentView, twoFragment);
                        transaction.commit();
                        break;
                    case R.id.imgThree:
                        transaction = fragmentManager.beginTransaction();
                        ThreeFragment threeFragment = new ThreeFragment();
                        transaction.replace(R.id.contentView, threeFragment);
                        transaction.commit();
                        break;

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        doubleClickExitApp();
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
            MobclickAgent.onKillProcess(this);
            System.exit(0);
            finish();
        }
    }
}
