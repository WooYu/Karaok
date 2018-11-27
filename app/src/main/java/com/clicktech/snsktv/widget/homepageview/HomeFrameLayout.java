package com.clicktech.snsktv.widget.homepageview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.clicktech.snsktv.arms.utils.DeviceUtils;

import timber.log.Timber;

/**
 * Created by Administrator on 2017/4/7.
 * 首页自定义FrameLayout，处理左右点击，长按事件
 */

public class HomeFrameLayout extends FrameLayout implements View.OnLongClickListener {

    private float screenWidth;
    private float screenHeight;
    private float coboundary;//上边界
    private float lowerboundary;//下边界

    private float downX;
    private float downY;
    private float maprange = 30;//判断手指是否移动的可接受的抖动距离
    private boolean isLongClick = false;//是否为长按
    private boolean hasLiftFinger = false;//是否抬起手指了
    private IHomeLayoutClickListener mLayoutListener;

    public HomeFrameLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HomeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        //获取屏幕的宽，以此确定可点击的区域
        screenWidth = DeviceUtils.getScreenWidth(context);
        screenHeight = DeviceUtils.getScreenHeight(context);
        coboundary = screenHeight / 2 - screenWidth / 2;
        lowerboundary = screenHeight / 2 + screenWidth / 2;
        setOnLongClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Timber.d("onTouchEvent():" + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isLongClick = false;
                hasLiftFinger = false;
                downX = event.getX();
                downY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                hasLiftFinger = true;
                float upX = event.getX();
                float upY = event.getY();

                double distance = Math.sqrt(Math.abs(
                        (downX - upX) * (downX - upX) + (downY - upY) * (downY - upY)));
                if (!isLongClick  //不是长按
                        && distance < maprange //手指没移动
                        && downY > coboundary && downY < lowerboundary) {//并且在可点击范围内

                    if (0 < upX && upX < screenWidth / 2) {//点击左边
                        Timber.d("点击左边");
                        if (null != mLayoutListener)
                            mLayoutListener.clickLeft();
                        return true;
                    } else if (screenWidth / 2 < upX && upX < screenWidth) {//点击右边
                        Timber.d("点击右边");
                        if (null != mLayoutListener)
                            mLayoutListener.clickRight();
                        return true;
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public void setClickLayoutListener(IHomeLayoutClickListener clickLayoutListener) {
        this.mLayoutListener = clickLayoutListener;
    }

    @Override
    public boolean onLongClick(View v) {
        Timber.d("onLongclick()");
        isLongClick = true;
        if (null != mLayoutListener && !hasLiftFinger)
            mLayoutListener.longClick();
        return true;
    }
}
