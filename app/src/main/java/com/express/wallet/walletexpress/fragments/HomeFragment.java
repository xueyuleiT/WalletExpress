package com.express.wallet.walletexpress.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.express.wallet.walletexpress.R;
import com.express.wallet.walletexpress.RegisterActivity;
import com.express.wallet.walletexpress.WebViewActivity;
import com.express.wallet.walletexpress.adapter.AdAdapter;
import com.express.wallet.walletexpress.model.AdDomain;
import com.express.wallet.walletexpress.model.AdEntity;
import com.express.wallet.walletexpress.model.CookieCallBack;
import com.express.wallet.walletexpress.model.ImgInfo;
import com.express.wallet.walletexpress.model.LogInfo;
import com.express.wallet.walletexpress.model.MainResResponse;
import com.express.wallet.walletexpress.model.RegisterOrLoginRequest;
import com.express.wallet.walletexpress.model.ResponseInfo;
import com.express.wallet.walletexpress.utils.CommonUtil;
import com.express.wallet.walletexpress.utils.OkhttpTask;
import com.express.wallet.walletexpress.utils.SettingUtils;
import com.express.wallet.walletexpress.utils.WalletUtil;
import com.express.wallet.walletexpress.view.ADTextView;
import com.express.wallet.walletexpress.view.FindViewpagerTabView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by cashbus on 6/22/16.
 */
public class HomeFragment extends BasicFragment implements View.OnClickListener{
    FindViewpagerTabView findViewpagerTabView;
    View rootView;
    Button loan;
    private ScheduledExecutorService scheduledExecutorService;
    // 轮播banner的数据
    private List<ImgInfo> adList;

    private int currentItem = 0; // 当前图片的索引号

    private List<AdEntity> mList = new ArrayList<>();
    private TextView finishCount,finishMoney;
    private EditText loanMoney;
    ADTextView mAdTextview;
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

        finishCount = (TextView) rootView.findViewById(R.id.finishCount);
        finishMoney = (TextView) rootView.findViewById(R.id.finishMoney);

        loanMoney = (EditText) rootView.findViewById(R.id.loanMoney);
        findViewpagerTabView = (FindViewpagerTabView) rootView.findViewById(R.id.findViewpagerTabView);

        mAdTextview = (ADTextView) rootView.findViewById(R.id.ad_textview);
        mAdTextview.setFrontColor(Color.WHITE);
        mAdTextview.setBackColor(Color.WHITE);
        getMainRes();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loan:
                if (TextUtils.isEmpty(loanMoney.getText().toString().replaceAll(" ",""))){
                    Toast.makeText(getActivity(),"请输入您想要借的金额",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(CommonUtil.WEBACTIVITY_TITLE,"推荐");
                intent.putExtra(CommonUtil.WEBACTIVITY_LINK,CommonUtil.SUGGEST_URL+loanMoney.getText().toString()+"&v5=1");
                startActivity(intent);
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
                            adList = mainResResponse.getImgs();
                            findViewpagerTabView.setData(adList);

                            List<LogInfo> logInfos = mainResResponse.getLogs();
                            mList.clear();
                            for (LogInfo info:logInfos) {
                                mList.add(new AdEntity(""+info.getCreate_time(),info.getMobile().substring(0,3)+"****"+info.getMobile().substring(info.getMobile().length()-4)+
                                        "已经成功配对借款"+info.getPrice()+"元",""));
                            }
                            mAdTextview.setmTexts(mList);
                            mAdTextview.postInvalidate();

                            StringBuffer stringBuffer = new StringBuffer();
                            String finishCountStr = ""+mainResResponse.getCount();
                            int index = finishCountStr.length()%3;
                            int count = finishCountStr.length()/3;

                            stringBuffer.append(finishCountStr.substring(0,index));
                            stringBuffer.append(",");
                            for (int i= 0; i < count;i++){
                                if (i != count){
                                    stringBuffer.append(finishCountStr.substring(index+i*3,index+(i+1)*3));
                                    if (i == count - 1){
                                        break;
                                    }
                                    stringBuffer.append(",");
                                }else {
                                    stringBuffer.append(finishCountStr.length() - 3);
                                }
                            }

                            finishCount.setText("已经成功帮助"+stringBuffer.toString()+"人完成借款");
                            stringBuffer = new StringBuffer();
                            String finishMoneyStr = ""+mainResResponse.getSum();
                            index = finishMoneyStr.length()%3;
                            count = finishMoneyStr.length()/3;

                            stringBuffer.append(finishMoneyStr.substring(0,index));
                            stringBuffer.append(",");
                            for (int i= 0; i < count;i++){
                                if (i != count){
                                    stringBuffer.append(finishMoneyStr.substring(index+i*3,index+(i+1)*3));
                                    if (i == count - 1){
                                        break;
                                    }
                                    stringBuffer.append(",");
                                }else {
                                    stringBuffer.append(finishMoneyStr.length() - 3);
                                }
                            }

                            finishMoney.setText(stringBuffer.toString());
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
