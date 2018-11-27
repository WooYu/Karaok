package com.clicktech.snsktv.module_discover.ui.holder;

import android.content.Intent;
import android.os.Bundle;
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
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/19.
 */

public class Discover_Home_HotSongsActivityHolder extends BaseHolder<SongInfoBean> {

    @BindView(R.id.item_singesrs_avater)
    ImageView item_singesrs_avater;
    @BindView(R.id.item_singesrs_name)
    TextView item_singesrs_name;
    @BindView(R.id.item_singesrs_songname)
    TextView item_singesrs_songname;


    public Discover_Home_HotSongsActivityHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final SongInfoBean data, int position) {
        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();

        imageLoader.loadImage(ktvApplication, GlideImageConfig
                .builder()
                .url(data.getWorks_image())
                .errorPic(R.mipmap.def_avatar_square)
                .imageView(item_singesrs_avater)
                .build());
        item_singesrs_name.setText(TextUtils.isEmpty(data.getUser_nickname()) ? "" : data.getUser_nickname());
        item_singesrs_songname.setText(TextUtils.isEmpty(data.getWorks_name()) ? "" : data.getWorks_name());
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
