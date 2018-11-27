package com.clicktech.snsktv.module_discover.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/22.
 */

public class MoreSongsHolder extends BaseHolder<SongInfoBean> {
    @BindView(R.id.tv_ranking)
    TextView tvRanking;
    @BindView(R.id.imageview)
    ImageView imageView;
    @BindView(R.id.tv_name)
    TextView tvName;

    public MoreSongsHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SongInfoBean data, final int position) {

        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();
        imageLoader.loadImage(ktvApplication, GlideImageConfig
                .builder()
                .url(data.getUser_photo())
                .errorPic(R.mipmap.def_square_large)
                .placeholder(R.mipmap.def_square_large)
                .imageView(imageView)
                .build());

        tvRanking.setText(String.valueOf(position + 1));
        tvName.setText(data.getUser_nickname());
    }

}
