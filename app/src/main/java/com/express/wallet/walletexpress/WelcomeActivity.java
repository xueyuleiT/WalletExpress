package com.express.wallet.walletexpress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.express.wallet.walletexpress.utils.CommonUtil;
import com.express.wallet.walletexpress.view.MyScrollView;
import com.google.gson.Gson;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by zenghui on 15/11/18.
 */
public class WelcomeActivity extends UmengActivity {
    ImageView imageView;
    Animation welcomeAnimation;
    MyScrollView scrollView;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        imageView = (ImageView) findViewById(R.id.welcomeImg);
        scrollView = (MyScrollView) findViewById(R.id.scrollView);

        WindowManager wm = this.getWindowManager();
        CommonUtil.screem_width = wm.getDefaultDisplay().getWidth();
        CommonUtil.screem_height = wm.getDefaultDisplay().getHeight();

        welcomeAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        scrollView.post(new Runnable() {
//            @Override
//            public void run() {
//                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//            }
//        });
        imageView.setAnimation(welcomeAnimation);
        welcomeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences sp = getSharedPreferences(CommonUtil.SPASH_SHOW, Context.MODE_PRIVATE);
                if (!sp.getBoolean("show", false)) {
                    Intent intent = new Intent(WelcomeActivity.this, ShowPhotoActivity.class);
                    ArrayList<Integer> arrayList = new ArrayList<Integer>();
                    arrayList.add(R.mipmap.convenient);
                    arrayList.add(R.mipmap.fast);
                    arrayList.add(R.mipmap.safe);
                    arrayList.add(R.mipmap.accurate);
                    arrayList.add(R.mipmap.gonext);
                    intent.putIntegerArrayListExtra("imageList",arrayList);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                } else {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    WelcomeActivity.this.finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }


}
