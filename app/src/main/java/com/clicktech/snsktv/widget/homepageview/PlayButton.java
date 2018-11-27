package com.clicktech.snsktv.widget.homepageview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/4/20.
 */

public class PlayButton extends android.support.v7.widget.AppCompatImageView {
    private Context context;

    public PlayButton(Context context) {
        super(context);
    }

    public PlayButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }
}
