package com.clicktech.snsktv.module_home.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SingerInfoEntity;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/13.
 */

public class SingerSelectHolder extends BaseHolder<SingerInfoEntity> {

    @BindView(R.id.item_tv_singer_name)
    TextView item_tv_singer_name;
    @BindView(R.id.im_singer_avatar)
    ImageView im_singer_avatar;

    public SingerSelectHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SingerInfoEntity data, int position) {
        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        item_tv_singer_name.setText(StringHelper.getLau_With_J_U_C(data.getSinger_name_jp(),
                data.getSinger_name_us(), data.getSinger_name_cn()));
        ktvApplication.getAppComponent().imageLoader().loadImage(context, GlideImageConfig.builder()
                .url(data.getSinger_photo())
                .placeholder(R.mipmap.def_avatar_round)
                .errorPic(R.mipmap.def_avatar_round)
                .transformation(new CircleWithBorderTransformation(context))
                .imageView(im_singer_avatar)
                .build());

    }

}
