package com.clicktech.snsktv.module_discover.ui.holder;

import android.view.View;
import android.widget.ImageView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SingerAlbumEntity;

import butterknife.BindView;

/**
 * Created by wy201 on 2017-12-05.
 */

public class PhotoAlbumHolder extends BaseHolder<SingerAlbumEntity> {
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.iv_pitchon)
    ImageView ivPitchon;

    private PhotoAlbumListener mItemListener;

    public PhotoAlbumHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final SingerAlbumEntity data, final int position) {
        KtvApplication application = KtvApplication.ktvApplication;
        if (EmptyUtils.isEmpty(data.getPhoto())) {
            ivPhoto.setImageResource(R.mipmap.album_add);
        } else {
            application.getAppComponent().imageLoader()
                    .loadImage(context, GlideImageConfig.builder()
                            .imageView(ivPhoto)
                            .url(data.getPhoto())
                            .errorPic(R.mipmap.def_square_large)
                            .placeholder(R.mipmap.def_square_large)
                            .build());
        }

        if (data.isSelect()) {
            ivPitchon.setVisibility(View.VISIBLE);
        } else {
            ivPitchon.setVisibility(View.GONE);
        }

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mItemListener) {
                    return;
                }

                if (0 == position && EmptyUtils.isEmpty(data.getPhoto())) {
                    mItemListener.addAlbum();
                } else {
                    mItemListener.itemClick(getLayoutPosition());
                }
            }
        });

    }

    public void setPhotoAlbumListener(PhotoAlbumListener listener) {
        this.mItemListener = listener;
    }

    public interface PhotoAlbumListener {
        void itemClick(int position);

        void addAlbum();
    }
}
