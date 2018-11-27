package com.clicktech.snsktv.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/7/4.
 */

public class MViewPager extends ViewPager {
    // 滑动距离及坐标 归还父控件焦点
    private float xDistance, yDistance, xLast, yLast, mLeft, line;

    public MViewPager(Context context) {
        super(context);
    }

    public MViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("touch", "ACTION_DOWN");
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                mLeft = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);

                xLast = curX;
                yLast = curY;
//                if (mLeft<100||xDistance < yDistance) {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                } else {
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }

                line = Math.abs(curX - xLast);
                if (line > 10) {
                    getParent().requestDisallowInterceptTouchEvent(true);

                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);

                }


                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
