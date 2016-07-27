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

    View rootView;
    Button loan;
    private ViewPager adViewPager;
    private List<ImageView> imageViews;// 滑动的图片集合
    private ScheduledExecutorService scheduledExecutorService;
    // 轮播banner的数据
    private List<ImgInfo> adList;
    private List<View> dots; // 图片标题正文的那些点
    private List<View> dotList;

    private TextView tv_date;
    private TextView tv_title;
    private TextView tv_topic_from;
    private TextView tv_topic;
    private int currentItem = 0; // 当前图片的索引号
    // 定义的五个指示点
    private View dot0;
    private View dot1;
    private View dot2;
    private View dot3;
    private View dot4;
    private LinearLayout dotLayout;
    private List<AdEntity> mList = new ArrayList<>();
    private TextView finishCount,finishMoney;
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

        mAdTextview = (ADTextView) rootView.findViewById(R.id.ad_textview);
        mAdTextview.setFrontColor(Color.WHITE);
        mAdTextview.setBackColor(Color.WHITE);
        initImageLoader();
        // 获取图片加载实例
        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();

        initAdData();
        getMainRes();
    }
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            adViewPager.setCurrentItem(currentItem);
        };
    };
    private void initAdData() {

        imageViews = new ArrayList<ImageView>();

        // 点
        dots = new ArrayList<View>();
        dotList = new ArrayList<View>();
        dotLayout = (LinearLayout) rootView.findViewById(R.id.dotLayout);

        tv_date = (TextView) rootView.findViewById(R.id.tv_date);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_topic_from = (TextView) rootView.findViewById(R.id.tv_topic_from);
        tv_topic = (TextView) rootView.findViewById(R.id.tv_topic);

        adViewPager = (ViewPager) rootView.findViewById(R.id.vp);
        // 设置一个监听器，当ViewPager中的页面改变时调用
        adViewPager.setOnPageChangeListener(new MyPageChangeListener());
    }


    private void addDynamicView() {
        // 动态添加图片和下面指示的圆点
        // 初始化图片资源
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.public_space_value_5)
                ,getResources().getDimensionPixelSize(R.dimen.public_space_value_5));
        layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.public_space_value_5);
        layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.public_space_value_5);
        for (int i = 0; i < adList.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            // 异步加载图片
            mImageLoader.displayImage(adList.get(i).getPic(), imageView,
                    options);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);

            View dot = LayoutInflater.from(getActivity()).inflate(R.layout.dot_layout, null);
            dot.setLayoutParams(layoutParams);
            dotLayout.addView(dot);
            dots.add(dot);
            dotList.add(dot);

        }
    }

    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            synchronized (adViewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    private void startAd() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
                TimeUnit.SECONDS);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loan:
//                if (TextUtils.isEmpty(SettingUtils.get(getActivity(), CommonUtil.TOKEN,""))){
//                    startActivity(new Intent(getActivity(), RegisterActivity.class));
//                }

                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(CommonUtil.WEBACTIVITY_TITLE,"推荐");
                intent.putExtra(CommonUtil.WEBACTIVITY_LINK,CommonUtil.SUGGEST_URL);
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
                            addDynamicView();
                            adViewPager.setAdapter(new AdAdapter(getActivity(),imageViews,adList));// 设置填充ViewPager页面的适配器
                            startAd();

                            List<LogInfo> logInfos = mainResResponse.getLogs();
                            mList.clear();
                            for (LogInfo info:logInfos) {
                                mList.add(new AdEntity(""+info.getCreate_time(),info.getMobile().substring(0,3)+"****"+info.getMobile().substring(info.getMobile().length()-4)+
                                        "已经成功配对借款"+info.getPrice()+"元",""));
                            }
                            mAdTextview.setmTexts(mList);
                            mAdTextview.postInvalidate();

                            finishCount.setText("已经成功帮助"+mainResResponse.getCount()+"人完成借款");
                            finishMoney.setText(""+mainResResponse.getSum());
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
    // 异步加载图片
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径
    private void initImageLoader() {
        File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
                .getOwnCacheDirectory(getActivity(),
                        IMAGE_CACHE_PATH);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new LruMemoryCache(12 * 1024 * 1024))
                .memoryCacheSize(12 * 1024 * 1024)
                .discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
                .discCache(new UnlimitedDiscCache(cacheDir))
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        ImageLoader.getInstance().init(config);
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            ImgInfo adDomain = adList.get(position);
            tv_title.setText(adDomain.getTitle()); // 设置标题
            tv_topic.setText(adDomain.getTitle());
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }
    }

}
