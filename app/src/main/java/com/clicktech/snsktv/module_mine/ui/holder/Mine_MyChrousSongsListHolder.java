package com.clicktech.snsktv.module_mine.ui.holder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_mine.ui.activity.Mine_Chrous_SingedListActivity;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/20.
 */

public class Mine_MyChrousSongsListHolder extends BaseHolder<SongInfoBean> {


    @BindView(R.id.item_mine_mychrous_songpic)
    MLImageView item_mine_mychrous_songpic;
    @BindView(R.id.item_mine_mychrous_songname)
    TextView item_mine_mychrous_songname;
    @BindView(R.id.item_mine_mychroussongs_canyu)
    TextView item_mine_mychroussongs_canyu;
    @BindView(R.id.chorus_num)
    TextView chorusNum;

    public Mine_MyChrousSongsListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final SongInfoBean song, int position) {

        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ktvApplication.getAppComponent().imageLoader().loadImage(context,
                GlideImageConfig.builder()
                        .url(song.getWorks_image())
                        .imageView(item_mine_mychrous_songpic)
                        .placeholder(R.mipmap.def_square_round)
                        .errorPic(R.mipmap.def_square_round)
                        .build());
        if (song.getChorus_count() != null) {
            chorusNum.setText(String.format(ktvApplication.getString(R.string.format_chorusnumber), song.getChorus_count()));
        } else {
            chorusNum.setText("");
        }
        item_mine_mychrous_songname.setText(TextUtils.isEmpty(song.getWorks_name()) ? "" : song.getWorks_name());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SongDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("songinfo", song);
                intent.putExtras(bundle);
                UiUtils.startActivity(intent);

            }
        });

        item_mine_mychroussongs_canyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, Mine_Chrous_SingedListActivity.class);
                it.putExtra("worksid", song.getId());
                UiUtils.startActivity(it);
            }
        });
    }


}
