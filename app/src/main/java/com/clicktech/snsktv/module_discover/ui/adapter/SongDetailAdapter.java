package com.clicktech.snsktv.module_discover.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/4/23.
 * 歌曲详情Viewpager
 */

public class SongDetailAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public SongDetailAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mFragments = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return null != mFragments ? mFragments.size() : 0;
    }
}
