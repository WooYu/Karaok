package com.clicktech.snsktv.module_discover.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.PopularSingerEntity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/19.
 */

public class Discover_Home_HotSingersHolder extends BaseHolder<PopularSingerEntity> {

    @BindView(R.id.discover_item_bgimg)
    ImageView discover_item_bgimg;
    @BindView(R.id.tv_singer_name)
    TextView tv_singer_name;
    @BindView(R.id.discover_listen_num)
    TextView listernNum;
    @BindView(R.id.tv_trophy)
    TextView tv_trophy;

    public Discover_Home_HotSingersHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PopularSingerEntity data, int position) {

        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();
        imageLoader.loadImage(context, GlideImageConfig.builder()
                .errorPic(R.mipmap.def_square_large)
                .placeholder(R.mipmap.def_square_large)
                .url(data.getUser_photo())
                .imageView(discover_item_bgimg)
                .build());

        tv_singer_name.setText(data.getUser_nickname());
        tv_trophy.setText(data.getGiftNum());
        listernNum.setText(data.getListenCount());

    }

}
