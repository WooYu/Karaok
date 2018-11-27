package com.clicktech.snsktv.module_discover.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.util.StringHelper;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/19.
 */

public class Discover_Home_HotSongsHolder extends BaseHolder<SongInfoBean> {
    @BindView(R.id.discover_item_bgimg)
    ImageView discover_item_bgimg;
    @BindView(R.id.tv_song_name)
    TextView tv_song_name;
    @BindView(R.id.tv_singer_name)
    TextView tv_singer_name;

    public Discover_Home_HotSongsHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SongInfoBean data, int position) {
        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ktvApplication.getAppComponent().imageLoader().loadImage(context, GlideImageConfig.builder()
                .imageView(discover_item_bgimg)
                .url(data.getSong_image())
                .placeholder(R.mipmap.def_square_large)
                .errorPic(R.mipmap.def_square_large)
                .build());

        tv_song_name.setText(StringHelper.getLau_With_J_U_C(data.getSong_name_jp(),
                data.getSong_name_us(), data.getSong_name_cn()));
        tv_singer_name.setText(data.getUser_nickname());

    }

}
