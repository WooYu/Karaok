package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SingerAlbumEntity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/26.
 */

public class OtherSingersAlbumAdapter extends DefaultAdapter<SingerAlbumEntity> {

    public OtherSingersAlbumAdapter(List<SingerAlbumEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SingerAlbumEntity> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_singeralbunlist;
    }


    class MHolder extends BaseHolder<SingerAlbumEntity> {

        @BindView(R.id.iv_photo)
        ImageView photo;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(SingerAlbumEntity data, int position) {
            KtvApplication.ktvApplication.getAppComponent().imageLoader().loadImage(context,
                    GlideImageConfig.builder()
                            .url(data.getPhoto())
                            .errorPic(R.mipmap.def_square_large)
                            .placeholder(R.mipmap.def_square_large)
                            .imageView(photo)
                            .build());
        }
    }

}
