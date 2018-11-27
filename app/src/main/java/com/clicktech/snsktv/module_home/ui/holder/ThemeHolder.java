package com.clicktech.snsktv.module_home.ui.holder;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongTypeBeanEntity;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wy201 on 2018-01-16.
 */

public class ThemeHolder extends BaseHolder<SongTypeBeanEntity> {

    @BindView(R.id.imageview)
    MLImageView imageview;

    private ThemeClickListener mListener;

    public ThemeHolder(View itemView, ThemeClickListener listener) {
        super(itemView);
        this.mListener = listener;
    }

    @Override
    public void setData(SongTypeBeanEntity data, int position) {
        KtvApplication.ktvApplication.getAppComponent().imageLoader()
                .loadImage(context, GlideImageConfig.builder()
                        .placeholder(R.mipmap.def_square_large)
                        .errorPic(R.mipmap.def_square_large)
                        .url(data.getCategory_image())
                        .imageView(imageview)
                        .build());

    }

    @OnClick(R.id.imageview)
    public void onImageViewClicked() {
        if (null != mListener) {
            mListener.onItemClick(getLayoutPosition());
        }
    }

    public interface ThemeClickListener {
        void onItemClick(int position);
    }

}
