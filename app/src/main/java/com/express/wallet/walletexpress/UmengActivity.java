package com.express.wallet.walletexpress;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by cashbus on 6/22/16.
 */
public class UmengActivity extends BasicActivity{

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
