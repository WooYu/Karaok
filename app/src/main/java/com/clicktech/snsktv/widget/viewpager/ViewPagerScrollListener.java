package com.clicktech.snsktv.widget.viewpager;

/**
 * Created by Administrator on 2017/3/22.
 */

public interface ViewPagerScrollListener {
    void onScroll(int position, float offset, int offsetPixels);

    void left2MidSlide(float offset);

    void mid2LeftSlide(float offset);

    void mid2RightSlide(float offset);

    void right2MidSlide(float offset);
}
