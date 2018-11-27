package com.clicktech.snsktv.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/23.
 */

public class ViewPagerWithListener extends ViewPager {
    private float mLastXIntercept = 0;
    private float mLastYIntercept = 0;


    private int lastValue;
    private ViewPagerScrollListener mScrollerListener;

    public ViewPagerWithListener(Context context) {
        super(context);
    }

    public ViewPagerWithListener(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPagerScrollListener(ViewPagerScrollListener listener) {
        this.mScrollerListener = listener;
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
        Log.d("MyViewPager", "position = " + position + " ,offset = " + offset + ", offsetPixels = " + offsetPixels);
        if (offsetPixels == 0 || null == mScrollerListener)
            return;
        if (lastValue > offsetPixels) {
            //position = curitemposition - 1 ; offset 从1到0；offsetPixels减小
            Log.d("MyViewPager", "向右");
            if (0 == position) {//第1页向第0页滑动
                mScrollerListener.onScroll(position, 1 - offset, offsetPixels);
                mScrollerListener.mid2LeftSlide(offset);
            } else if (1 == position) {//第2页向第1页滑动
                mScrollerListener.onScroll(position, offset, offsetPixels);
                mScrollerListener.right2MidSlide(offset);
            } else if (2 == position) {

            }

        } else if (lastValue < offsetPixels) {
            //position = curitemposition ; offset从0到1；offsetPixels增加
            Log.d("MyViewPager", "向左");
            if (0 == position) {//第0页到第1页
                mScrollerListener.onScroll(position, 1 - offset, offsetPixels);
                mScrollerListener.left2MidSlide(offset);
            } else if (1 == position) {//从第1页到第2页
                mScrollerListener.onScroll(position, offset, offsetPixels);
                mScrollerListener.mid2RightSlide(offset);
            } else if (2 == position) {

            }

        } else if (lastValue == offsetPixels) {
            Log.d("MyViewPager", "未动");
        }
        lastValue = offsetPixels;
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        boolean intercepted = false;
//        float x = event.getX();
//        float y = event.getY();
//
//        int action = event.getAction() & MotionEvent.ACTION_MASK;
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                intercepted = false;
//                //初始化viewpgaer的mActivePoinerId
//                super.onInterceptTouchEvent(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float deltaX = Math.abs(x - mLastXIntercept);
//                float deltaY = Math.abs(y - mLastYIntercept);
//                intercepted = !(deltaX <= deltaY);
//                break;
//            case MotionEvent.ACTION_UP:
//                intercepted = false;
//                break;
//            default:
//                break;
//        }
//
//        mLastXIntercept = x;
//        mLastYIntercept = y;
////        System.out.println("最外层viewpager: 事件类型："+event.getAction()+"，是否拦截："+intercepted);
//        return intercepted;
//    }
}

