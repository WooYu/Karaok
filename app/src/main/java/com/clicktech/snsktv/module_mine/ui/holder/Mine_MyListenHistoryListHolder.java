package com.clicktech.snsktv.module_mine.ui.holder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/20.
 */

public class Mine_MyListenHistoryListHolder extends BaseHolder<SongInfoBean> {


    @BindView(R.id.item_mine_mychrous_songpic)
    MLImageView item_mine_mychrous_songpic;
    @BindView(R.id.item_mine_mychrous_songname)
    TextView item_mine_mychrous_songname;
    @BindView(R.id.item_mine_mychrous_singername)
    TextView item_mine_mychrous_singername;
    @BindView(R.id.item_mine_mychroussongs_time)
    TextView item_mine_mychroussongs_time;

    public Mine_MyListenHistoryListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final SongInfoBean song, int position) {

        if (song == null) {
            return;
        }

        final KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();
        imageLoader.loadImage(ktvApplication, GlideImageConfig
                .builder()
                .url(song.getWorks_image())
                .errorPic(R.mipmap.def_avatar_square)
                .placeholder(R.mipmap.def_avatar_square)
                .imageView(item_mine_mychrous_songpic)
                .build());

        item_mine_mychrous_songname.setText(TextUtils.isEmpty(song.getWorks_name()) ? "" : song.getWorks_name());
        item_mine_mychrous_singername.setText(TextUtils.isEmpty(song.getUser_nickname()) ? "" : song.getUser_nickname());
        item_mine_mychroussongs_time.setText(TextUtils.isEmpty(song.getAdd_time()) ? "" : song.getAdd_time());


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ktvApplication, SongDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("songinfo", song);
                intent.putExtras(bundle);
                UiUtils.startActivity(intent);

            }
        });

    }

}
