package com.clicktech.snsktv.widget.pagetransformer;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by wy201 on 2017-12-20.
 */

public class UltraDepthScaleTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.70f;
    private int maxTranslateOffsetX;
    private ViewPager viewPager;


    public UltraDepthScaleTransformer(Context context) {
        this.maxTranslateOffsetX = dp2px(context, 180);
    }

    public void transformPage(View page, float position) {
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        if (position < -1 || position > 1) {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            page.setRotationY(-30 * scaleFactor);
        } else if (position <= 1) { // [-1,1]

            if (position < 0) {
                float scaleX = 1 + 0.3f * position;
                page.setScaleX(scaleX);
                page.setScaleY(scaleX);
                page.setRotationY(30 * scaleFactor);
            } else {
                float scaleX = 1 - 0.3f * position;
                page.setScaleX(scaleX);
                page.setScaleY(scaleX);
                page.setRotationY(0);
            }
        }


//        if (viewPager == null) {
//            viewPager = (ViewPager) view.getParent();
//        }
//
//        int leftInScreen = view.getLeft() - viewPager.getScrollX();
//        int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;
//        int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
//        float offsetRate = (float) offsetX * 0.38f / viewPager.getMeasuredWidth();
//
//
//        float scaleFactor = 1 - Math.abs(offsetRate);
//        if (scaleFactor > 0) {
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);
//            view.setTranslationX(-maxTranslateOffsetX * offsetRate);
//        }
//
//        if (position < -1 || position > 1) {
//            view.setRotationY(-30*scaleFactor);
//        } else {
//            if (position < 0) {
//                view.setRotationY(30*scaleFactor);
//            } else {
//                view.setRotationY(0);
//            }
//        }
    }

    /**
     * dp和像素转换
     */
    private int dp2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }
}
