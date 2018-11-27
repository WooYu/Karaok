package com.clicktech.snsktv.module_discover.ui.holder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.PopularSingerEntity;
import com.clicktech.snsktv.module_discover.ui.activity.SingerIntroActivity;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/20.
 */

public class RichListHolder extends BaseHolder<PopularSingerEntity> {

    @BindView(R.id.iv_ranking)
    ImageView ivRanking;
    @BindView(R.id.tv_ranking)
    TextView tvRanking;
    @BindView(R.id.imageview)
    MLImageView imageview;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.item_song_name)
    TextView songName;
    @BindView(R.id.tv_num)
    TextView tvNum;

    public RichListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final PopularSingerEntity data, int position) {
        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();
        imageLoader.loadImage(ktvApplication, GlideImageConfig
                .builder()
                .url(data.getUser_photo())
                .errorPic(R.mipmap.def_avatar_round)
                .placeholder(R.mipmap.def_avatar_round)
                .imageView(imageview)
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
        tvName.setText(data.getUser_nickname());
        tvNum.setText(data.getLevel());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingerIntroActivity.class);
                intent.putExtra("userid", data.getId());
                UiUtils.startActivity(intent);
            }
        });

    }

}
