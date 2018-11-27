package com.clicktech.snsktv.arms.utils;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/12/28.
 * 重新计算ViewPager高度
 */

public class ViewPagerUtils {
    public static void resetViewPagerHeight(ViewPager viewPager, int pos) {
        View child = viewPager.getChildAt(pos);
        if (child != null) {
            child.measure(0, 0);
            int h = child.getMeasuredHeight();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewPager
                    .getLayoutParams();
            params.height = h;
            viewPager.setLayoutParams(params);
        }
    }
}
