package com.clicktech.snsktv.arms.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Administrator on 2017/1/10.
 * 不能竖向滚动的RecyclerView
 */

public class NoRolledLinearMananger extends LinearLayoutManager {

    private boolean isScrollEnabled = false;

    public NoRolledLinearMananger(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
