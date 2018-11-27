package com.clicktech.snsktv.module_discover.ui.holder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/19.
 */

public class RankListenHolder extends BaseHolder<SongInfoBean> {

    @BindView(R.id.iv_ranking)
    ImageView ivRanking;
    @BindView(R.id.tv_ranking)
    TextView tvRanking;
    @BindView(R.id.item_singesrs_avater)
    MLImageView ivAvatar;
    @BindView(R.id.item_singesrs_name)
    TextView tvSingerName;
    @BindView(R.id.item_singesrs_songname)
    TextView tvSongName;


    public RankListenHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final SongInfoBean data, int position) {

        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();
        imageLoader.loadImage(ktvApplication, GlideImageConfig
                .builder()
                .url(data.getWorks_image())
                .errorPic(R.mipmap.def_avatar_round)
                .placeholder(R.mipmap.def_avatar_round)
                .imageView(ivAvatar)
                .build());

        if (position < 10) {
            ivRanking.setVisibility(View.VISIBLE);
            tvRanking.setVisibility(View.GONE);
            ivRanking.setImageResource(StringHelper.getImageResourceByRanking(position));
        } else {
            ivRanking.setVisibility(View.GONE);
            tvRanking.setVisibility(View.VISIBLE);
            tvRanking.setText(String.valueOf(position + 1));
        }
        tvSingerName.setText(data.getUser_nickname());
        tvSongName.setText(data.getListen_count());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SongDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("songinfo", data);
                intent.putExtras(bundle);
                UiUtils.startActivity(intent);

            }
        });
    }

}
