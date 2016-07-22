package com.express.wallet.walletexpress;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ricky on 15-10-9.
 */
public class WalletApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG){
            JPushInterface.setDebugMode(true);
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }else{
            JPushInterface.setDebugMode(false);
        }
        context = getApplicationContext();
    }

    public static Context getInstance(){
        return context;

    }



}
