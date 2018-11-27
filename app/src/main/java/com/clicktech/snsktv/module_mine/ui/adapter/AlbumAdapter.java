package com.clicktech.snsktv.module_mine.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.entity.WorkAlbumBean;
import com.clicktech.snsktv.module_mine.ui.fragment.WorkAlbumFragment;

import java.util.List;

/**
 * Created by wy201 on 2017-12-20.
 */

public class AlbumAdapter extends FragmentStatePagerAdapter {

    private List<WorkAlbumBean> mAlbumList;
    private Context mContext;

    public AlbumAdapter(FragmentManager fm, List<WorkAlbumBean> mAlbumList, Context mContext) {
        super(fm);
        this.mAlbumList = mAlbumList;
        this.mContext = mContext;
    }

    public void setData(List<WorkAlbumBean> list) {
        this.mAlbumList = list;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        WorkAlbumFragment workAlbumFragment = new WorkAlbumFragment();
        workAlbumFragment.bindData(mAlbumList.get(position));
        return workAlbumFragment;
    }

    @Override
    public int getCount() {
        return EmptyUtils.isEmpty(mAlbumList) ? 0 : mAlbumList.size();
    }
}
