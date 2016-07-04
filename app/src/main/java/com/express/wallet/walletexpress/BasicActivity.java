package com.express.wallet.walletexpress;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by cashbus on 6/22/16.
 */
public class BasicActivity extends AppCompatActivity {

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int msgResId) {
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show();
    }

}
