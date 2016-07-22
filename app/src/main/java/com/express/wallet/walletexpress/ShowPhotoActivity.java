package com.express.wallet.walletexpress;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.express.wallet.walletexpress.utils.CommonUtil;

import java.util.ArrayList;

/**
 * Created by cashbus on 6/2/16.
 */
public class ShowPhotoActivity extends BasicActivity implements ViewSwitcher.ViewFactory, View.OnTouchListener {

    /**
     * ImagaSwitcher 的引用
     */
    private ImageSwitcher mImageSwitcher;

    /**
     * 当前选中的图片id序号
     */
    private int currentPosition;
    /**
     * 按下点的X坐标
     */
    private float downX;
    /**
     * 装载点点的容器
     */
    private LinearLayout linearLayout;
    /**
     * 点点数组
     */
    private ImageView[] tips;
    ArrayList<Integer> imageList;
    float scaleX;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_photo_layout);


        //实例化ImageSwitcher
        mImageSwitcher  = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        //设置Factory
        mImageSwitcher.setFactory(this);
        //设置OnTouchListener，我们通过Touch事件来切换图片
        mImageSwitcher.setOnTouchListener(this);
//        mImageSwitcher.setScaleX(1.2f);

        linearLayout = (LinearLayout) findViewById(R.id.viewGroup);

        imageList = getIntent().getIntegerArrayListExtra("imageList");

        tips = new ImageView[imageList.size()];
        int margin = getResources().getDimensionPixelSize(R.dimen.public_space_value_5);
        for(int i=0; i<imageList.size(); i++){
            ImageView mImageView = new ImageView(this);
            tips[i] = mImageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.rightMargin = margin;
            layoutParams.leftMargin = margin;

            mImageView.setBackgroundResource(R.drawable.img_unfocus);
            linearLayout.addView(mImageView, layoutParams);
        }

        //这个我是从上一个界面传过来的，上一个界面是一个GridView
        currentPosition = getIntent().getIntExtra("index", 0);
//        mImageSwitcher.setImageURI(Uri.parse(imageList.get(currentPosition)));
        mImageSwitcher.setImageResource(imageList.get(currentPosition));
        setImageBackground(currentPosition);

    }

    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.img_focus);
            }else{
                tips[i].setBackgroundResource(R.drawable.img_unfocus);
            }
        }
    }

    @Override
    public View makeView() {
        final ImageView i = new ImageView(this);
        i.setBackgroundColor(getResources().getColor(R.color.T1));
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return i ;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                //手指按下的X坐标
                downX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP:{
                float lastX = event.getX();
                //抬起的时候的X坐标大于按下的时候就显示上一张图片
                if(lastX > downX){
                    if(currentPosition > 0){
                        //设置动画，这里的动画比较简单，不明白的去网上看看相关内容
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_in));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_out));
                        currentPosition --;
//                        mImageSwitcher.setImageURI(Uri.parse(imageList.get(currentPosition % imageList.size())));
                        mImageSwitcher.setImageResource(imageList.get(currentPosition % imageList.size()));
                        setImageBackground(currentPosition);
                    }
                    break;
                }

                if(lastX < downX){
                    if(currentPosition < imageList.size() - 1){
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_in));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_out));
                        currentPosition ++ ;
//                        mImageSwitcher.setImageURI(Uri.parse(imageList.get(currentPosition)));
                        mImageSwitcher.setImageResource(imageList.get(currentPosition));

                        setImageBackground(currentPosition);
                    }else{
                            SharedPreferences sp = getSharedPreferences(CommonUtil.SPASH_SHOW, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("show", true);
                            editor.commit();
                            startActivity(new Intent(ShowPhotoActivity.this, MainActivity.class));
                            ShowPhotoActivity.this.finish();
                    }
                    break;
                }
                SharedPreferences sp = getSharedPreferences(CommonUtil.SPASH_SHOW, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("show", true);
                editor.commit();
                startActivity(new Intent(ShowPhotoActivity.this, MainActivity.class));
                ShowPhotoActivity.this.finish();

            }
        }

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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

}
