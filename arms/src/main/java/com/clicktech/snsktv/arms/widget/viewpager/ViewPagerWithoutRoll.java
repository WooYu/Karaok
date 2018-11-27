package com.clicktech.snsktv.arms.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/12/28.
 * 不能左右滚动的Viewpager
 */

public class ViewPagerWithoutRoll extends ViewPager {
    private boolean noRoll = true;

    public ViewPagerWithoutRoll(Context context) {
        super(context);
        init(context);
    }

    public ViewPagerWithoutRoll(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
    }

    public void setNoRoll(boolean noRoll) {
        this.noRoll = noRoll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (noRoll)
            return false;
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (noRoll)
            return false;
        return super.onInterceptTouchEvent(ev);
    }

}
