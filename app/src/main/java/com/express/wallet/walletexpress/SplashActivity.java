package com.express.wallet.walletexpress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.Window;


import com.express.wallet.walletexpress.adapter.FragAdapter;
import com.express.wallet.walletexpress.fragments.FragmentOne;
import com.express.wallet.walletexpress.fragments.FragmentThree;
import com.express.wallet.walletexpress.fragments.FragmentTwo;
import com.express.wallet.walletexpress.view.CursorView;

import java.util.ArrayList;

/**
 * Created by zenghui on 15/12/1.
 */
public class SplashActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mPager;
    private ArrayList<Fragment> mListFragments = new ArrayList<Fragment>();
    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentThree fragmentThree;
    FragAdapter fragAdapter;
    CursorView cursorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.viewpager_layout);
        initViewPager();

    }

    private void initViewPager() {
        mPager = (ViewPager) findViewById(R.id.viewpager);
        cursorView = (CursorView) findViewById(R.id.cursorView);
        fragmentOne = new FragmentOne();
        fragmentTwo = new FragmentTwo();
        fragmentThree = new FragmentThree();
        mListFragments.clear();
        mListFragments.add(fragmentOne);
        mListFragments.add(fragmentTwo);
        mListFragments.add(fragmentThree);
        fragAdapter = new FragAdapter(getSupportFragmentManager(), mListFragments);

        mPager.setAdapter(fragAdapter);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(2);
        mPager.setOnPageChangeListener(this);
        setVisiable(0);

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int index) {
        if (index > cIndex) {
            cIndex = index;
            cursorView.jumpToRight(cursorView.getcIndex() + 1, false);
            setVisiable(index);
        } else if (index < cIndex) {
            cIndex = index;
            cursorView.jumpToLeft(cursorView.getcIndex() - 1, false);
            setVisiable(index);
        }
    }

    void setVisiable(int index) {
        if (index == 0) {
            fragmentOne.setUserVisibleHint(true);
            fragmentTwo.setUserVisibleHint(false);
            fragmentThree.setUserVisibleHint(false);
        } else if (index == 1) {
            fragmentOne.setUserVisibleHint(false);
            fragmentTwo.setUserVisibleHint(true);
            fragmentThree.setUserVisibleHint(false);
        } else {
            fragmentOne.setUserVisibleHint(false);
            fragmentTwo.setUserVisibleHint(false);
            fragmentThree.setUserVisibleHint(true);
        }
    }

    int cIndex = 0;
    int downX, beforeX;
    int status = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (cursorView.isAnimation) {
            return true;
        }
        boolean mSuper = super.dispatchTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (cursorView.isAnimation) {
                    return true;
                }
                status = 0;
                downX = (int) event.getX();
                beforeX = downX;
                break;
            case MotionEvent.ACTION_MOVE:


                if (event.getX() - beforeX < 0) {
                    cursorView.toRight(cursorView.getcIndex() + 1, (int) (beforeX - event.getX()));
                } else {
                    cursorView.toLeft(cursorView.getcIndex() - 1, (int) (event.getX() - beforeX));
                }
                beforeX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (cursorView.getcIndex() == cIndex) {
                    cursorView.jumpToOrginal();
                }
                break;
        }

        return mSuper;
    }

}
