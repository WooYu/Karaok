package com.clicktech.snsktv.arms.widget.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017-06-24.
 * 解决嵌套ConvenientBanner的问题
 */

public class NestedScrollView2 extends ScrollView {
    private boolean canScroll;

    private GestureDetector mGestureDetector;

    public NestedScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new YScrollDetector());
        canScroll = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP)
            canScroll = true;
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (canScroll)
                canScroll = Math.abs(distanceY) >= Math.abs(distanceX);
            return canScroll;
        }
    }
}
