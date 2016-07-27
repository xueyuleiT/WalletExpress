package com.express.wallet.walletexpress.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.express.wallet.walletexpress.WebViewActivity;
import com.express.wallet.walletexpress.model.AdDomain;
import com.express.wallet.walletexpress.model.ImgInfo;
import com.express.wallet.walletexpress.utils.CommonUtil;

import java.util.List;

/**
 * Created by zenghui on 2016/7/24.
 */
public class AdAdapter extends PagerAdapter {

    List<ImgInfo> adList;
    private List<ImageView> imageViews;// 滑动的图片集合
    Context context;
    public AdAdapter(Context context, List<ImageView> imageViews, List<ImgInfo> adList){
        this.adList = adList;
        this.imageViews = imageViews;
        this.context = context;
    }

    @Override
    public int getCount() {
        return adList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = imageViews.get(position);
        ((ViewPager) container).addView(iv);
        final ImgInfo adDomain = adList.get(position);
        // 在这个方法里面设置图片的点击事件
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 处理跳转逻辑
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(CommonUtil.WEBACTIVITY_TITLE,adDomain.getTitle());
                intent.putExtra(CommonUtil.WEBACTIVITY_LINK,String.format(CommonUtil.DOMAIN+"%s",adDomain.getLink()));
                context.startActivity(intent);
            }
        });
        return iv;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public void finishUpdate(View arg0) {

    }

}
