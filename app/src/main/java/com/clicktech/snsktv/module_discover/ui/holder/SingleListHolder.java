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

public class SingleListHolder extends BaseHolder<SongInfoBean> {

    @BindView(R.id.iv_mine_album_pic)
    ImageView iv_mine_album_pic;
    @BindView(R.id.tv_music_name)
    TextView tv_mine_music_name;
    @BindView(R.id.tv_listen_num)
    TextView listenNum;
    @BindView(R.id.tv_gift_num)
    TextView giftNum;

    public SingleListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final SongInfoBean data, int position) {
        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();

        imageLoader.loadImage(ktvApplication, GlideImageConfig
                .builder()
                .url(data.getWorks_image())
                .errorPic(R.mipmap.def_square_large)
                .placeholder(R.mipmap.def_square_large)
                .imageView(iv_mine_album_pic)
                .build());

        listenNum.setText(data.getListen_count());
        giftNum.setText(data.getFlowerNum());
        tv_mine_music_name.setText(data.getWorks_name());
    }

}
