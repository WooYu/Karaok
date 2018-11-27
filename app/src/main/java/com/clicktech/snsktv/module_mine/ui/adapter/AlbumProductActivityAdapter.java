package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/22.
 */

public class AlbumProductActivityAdapter extends DefaultAdapter<SongInfoBean> {


    public AlbumProductActivityAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_albumproduct;
    }


    class MHolder extends BaseHolder<SongInfoBean> {
        @BindView(R.id.mi_avatar)
        MLImageView miAvatar;
        @BindView(R.id.tv_music_name)
        TextView tvMusicName;
        @BindView(R.id.im_listen)
        ImageView imListen;
        @BindView(R.id.tv_listen_num)
        TextView tvListenNum;
        @BindView(R.id.im_check)
        ImageView imCheck;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(SongInfoBean data, int position) {
            tvMusicName.setText(data.getWorks_name());
            tvListenNum.setText(data.getListen_count());

            KtvApplication.ktvApplication.getAppComponent().imageLoader()
                    .loadImage(context, GlideImageConfig.builder()
                            .url(data.getWorks_image())
                            .errorPic(R.mipmap.def_square_large)
                            .placeholder(R.mipmap.def_square_large)
                            .imageView(miAvatar)
                            .build());
        }
    }


}
