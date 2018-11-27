package com.clicktech.snsktv.widget.pagetransformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by zhy on 16/5/7.
 */
public class NonPageTransformer implements ViewPager.PageTransformer {
    public static final ViewPager.PageTransformer INSTANCE = new NonPageTransformer();

    @Override
    public void transformPage(View page, float position) {
        page.setScaleX(0.999f);//hack
    }
}