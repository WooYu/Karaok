package com.clicktech.snsktv.module_mine.ui.holder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.Att_Fans_UserEntity;
import com.clicktech.snsktv.module_discover.ui.activity.SingerIntroActivity;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/20.
 */

public class Mine_AttentionUserListHolder extends BaseHolder<Att_Fans_UserEntity> {

    @BindView(R.id.item_singer_avater)
    ImageView item_singer_avater;
    @BindView(R.id.item_singer_name)
    TextView item_singer_name;
    @BindView(R.id.item_singer_attention)
    TextView item_singer_attention;

    boolean hideAttent;

    public Mine_AttentionUserListHolder(View itemView, boolean hideAttent) {
        super(itemView);
        this.hideAttent = hideAttent;
    }

    @Override
    public void setData(final Att_Fans_UserEntity song, int position) {

        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();
        imageLoader.loadImage(ktvApplication, GlideImageConfig
                .builder()
                .url(song.getUser_photo())
                .placeholder(R.mipmap.def_avatar_round)
                .errorPic(R.mipmap.def_avatar_round)
                .transformation(new CircleWithBorderTransformation(context))
                .imageView(item_singer_avater)
                .build());

        item_singer_name.setText(TextUtils.isEmpty(song.getUser_nickname()) ? "" : song.getUser_nickname());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingerIntroActivity.class);
                intent.putExtra("userid", song.getId());
                UiUtils.startActivity(intent);
            }
        });

        if (hideAttent) {
            item_singer_attention.setVisibility(View.GONE);
        } else {
            item_singer_attention.setVisibility(View.VISIBLE);
        }
        item_singer_attention.setOnClickListener(this);
    }

}
