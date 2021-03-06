package com.clicktech.snsktv.module_home.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.transformations.RoundedCornersTransformation;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/13.
 */

public class SongSelectHistoryHolder extends BaseHolder<SongInfoBean> {

    @BindView(R.id.item_tv_singer_name)
    TextView item_tv_singer_name;
    @BindView(R.id.im_song_atatar)
    ImageView im_song_atatar;
    @BindView(R.id.item_tv_music_name)
    TextView item_tv_music_name;

    public SongSelectHistoryHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SongInfoBean data, int position) {
        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();

        item_tv_singer_name.setText(StringHelper.getLau_With_J_U_C(data.getSinger_name_jp(),
                data.getSinger_name_us(), data.getSinger_name_cn()));
        item_tv_music_name.setText(StringHelper.getLau_With_J_U_C(data.getSong_name_jp(),
                data.getSong_name_us(), data.getSong_name_cn()));

        ktvApplication.getAppComponent().imageLoader().loadImage(context, GlideImageConfig
                .builder()
                .url(data.getSong_image())
                .transformation(new RoundedCornersTransformation(context, 5))
                .errorPic(R.mipmap.def_avatar_square)
                .placeholder(R.mipmap.def_avatar_square)
                .imageView(im_song_atatar)
                .build());
    }

}
