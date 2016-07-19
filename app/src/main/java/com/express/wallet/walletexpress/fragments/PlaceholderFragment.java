package com.express.wallet.walletexpress.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.express.wallet.walletexpress.listener.BackHandledInterface;

import java.util.ArrayList;

/**
 * Created by zenghui on 2016/7/19.
 */
public class PlaceholderFragment extends Fragment {

private BackHandledInterface mBackHandledInterface;
    private int i=1;
    protected ArrayList<String> loadHistoryUrls = new ArrayList<String>();
    @Override
    public void onAttach(Activity activity) {

            super.onAttach(activity);

            if (!(getActivity() instanceof BackHandledInterface)) {
            throw new ClassCastException(
            "Hosting Activity must implement BackHandledInterface");
            } else {
            this.mBackHandledInterface = (BackHandledInterface) getActivity();
            }
            }

    @Override
    public void onStart() {
            mBackHandledInterface.setSelectedFragment(this);
            super.onStart();
    }


    public boolean onBackPressed() {

            return false;
    }

}