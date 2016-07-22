package com.express.wallet.walletexpress.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by zenghui on 15/11/18.
 */
public class MyScrollView extends ScrollView{

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int actionMasked = ev.getActionMasked() & MotionEvent.ACTION_MASK;

        if (actionMasked == MotionEvent.ACTION_MOVE || actionMasked == MotionEvent.ACTION_DOWN) {
            // 最关键的地方，忽略MOVE DOWN事件
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
