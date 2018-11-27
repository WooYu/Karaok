package com.clicktech.snsktv.arms.widget.scrollview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/1/22.
 */

public class ScrollViewListener extends NestedScrollView {
    private OnScrollListener listener;

    public ScrollViewListener(Context context) {
        super(context);
    }

    public ScrollViewListener(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewListener(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置滑动距离监听器
     */
    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (listener != null) {
            listener.onScroll(getScrollY());
        }
    }

    // 滑动距离监听器
    public interface OnScrollListener {

        /**
         * 在滑动的时候调用，scrollY为已滑动的距离
         */
        void onScroll(int scrollY);
    }
}
