package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.util.StringHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/10.
 */

public class PopuSongAdapter extends DefaultAdapter<SongInfoBean> {
    public PopuSongAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new Mholder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_popusong;
    }

    public class Mholder extends BaseHolder<SongInfoBean> {
        @BindView(R.id.discover_item_bgimg)
        ImageView discover_item_bgimg;
        @BindView(R.id.tv_song_name)
        TextView tv_song_name;
        @BindView(R.id.tv_singer_name)
        TextView singerName;

        public Mholder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(final SongInfoBean data, int position) {

            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();
            imageLoader.loadImage(ktvApplication, GlideImageConfig
                    .builder()
                    .url(data.getSong_image())
                    .errorPic(R.mipmap.def_square_large)
                    .placeholder(R.mipmap.def_square_large)
                    .imageView(discover_item_bgimg)
                    .build());

            String musicName = StringHelper.getLau_With_J_U_C(data.getSong_name_jp(),
                    data.getSong_name_us(), data.getSong_name_cn());
            tv_song_name.setText(musicName);
            singerName.setText(data.getUser_nickname());

        }
    }
}
