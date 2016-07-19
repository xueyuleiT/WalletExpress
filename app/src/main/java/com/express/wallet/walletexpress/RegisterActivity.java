package com.express.wallet.walletexpress;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.express.wallet.walletexpress.utils.CommonUtil;

/**
 * Created by zenghui on 16/7/17.
 */
public class RegisterActivity extends UmengActivity implements View.OnClickListener{

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
            case R.id.agreement:
                Intent intent = new Intent(this,WebViewActivity.class);
                intent.putExtra(CommonUtil.WEBACTIVITY_LINK, CommonUtil.REGISTER_AGREEMENT_URL);
                intent.putExtra(CommonUtil.WEBACTIVITY_TITLE, "注册协议");
                startActivity(intent);
                break;
        }
    }
}
