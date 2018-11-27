package com.clicktech.snsktv.module_mine.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SoundHistoryEntity;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/20.
 */

public class Mine_MySoundHistoryListHolder extends BaseHolder<SoundHistoryEntity> {

    @BindView(R.id.item_mine_mychrous_songpic)
    MLImageView item_mine_mychrous_songpic;
    @BindView(R.id.item_mine_mychrous_songname)
    TextView item_mine_mychrous_songname;
    @BindView(R.id.item_mine_mychrous_singername)
    TextView item_mine_mychrous_singername;
    @BindView(R.id.item_mine_mychroussongs_time)
    TextView item_mine_mychroussongs_time;

    public Mine_MySoundHistoryListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final SoundHistoryEntity song, int position) {

        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();
        imageLoader.loadImage(ktvApplication, GlideImageConfig
                .builder()
                .url(song.getSong_image())
                .errorPic(R.mipmap.def_square_large)
                .placeholder(R.mipmap.def_square_large)
                .imageView(item_mine_mychrous_songpic)
                .build());

        item_mine_mychrous_songname.setText(song.getSong_name());
        item_mine_mychrous_singername.setText(song.getUser_nickname());

    }


}
