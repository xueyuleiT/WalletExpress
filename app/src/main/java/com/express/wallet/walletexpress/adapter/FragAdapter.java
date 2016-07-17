package com.express.wallet.walletexpress.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by zenghui on 15/12/1.
 */
public class FragAdapter extends FragmentPagerAdapter {
    private FragmentTransaction mCurTransaction = null;
    private List<Fragment> fragments;
    Fragment mCurrentPrimaryItem;
    FragmentManager fm;
    public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
        mCurrentPrimaryItem = fragments.get(0);
    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return fragments.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return fragments.size();
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View)object);        // 删除页卡
//    }
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return ((Fragment) object).getView() == view;
//    }
//
////    @Override
////    public void setPrimaryItem(ViewGroup container, int position, Object object) {
////        Fragment fragment = (Fragment) object;
////        if (mCurrentPrimaryItem != fragment) {
////            if (mCurrentPrimaryItem != null) {
////                mCurrentPrimaryItem.setUserVisibleHint(false);
////            }
////            if (fragment != null) {
////                fragment.setUserVisibleHint(true);
////            }
////            mCurrentPrimaryItem = fragment;
////        }
////    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        if (mCurTransaction == null) {
//            mCurTransaction = fm.beginTransaction();
//        }
//        Fragment f = getFragment(position);
//        mCurTransaction.show(f);
//        return f;
//    }
//
//    public Fragment getFragment(int position) {
//        if(fragments.size()<=position || position<0)
//            throw new IllegalArgumentException("position: " + position);
//        else
//            return fragments.get(position);
//    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        if (mCurTransaction == null) {
//            mCurTransaction = fm.beginTransaction();
//        }
//        mCurTransaction.hide((Fragment) object);
//    }
}
