package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SingerAlbumEntity;
import com.clicktech.snsktv.module_discover.ui.holder.PhotoAlbumHolder;

import java.util.List;

/**
 * Created by wy201 on 2017-12-05.
 */

public class PhotoAlbumAdapter extends DefaultAdapter<SingerAlbumEntity> {
    PhotoAlbumHolder.PhotoAlbumListener mListener;

    public PhotoAlbumAdapter(List<SingerAlbumEntity> infos, PhotoAlbumHolder.PhotoAlbumListener listener) {
        super(infos);
        this.mListener = listener;
    }

    @Override
    public BaseHolder<SingerAlbumEntity> getHolder(View v) {
        PhotoAlbumHolder holder = new PhotoAlbumHolder(v);
        holder.setPhotoAlbumListener(mListener);
        return holder;
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_singeralbunlist;
    }

}
