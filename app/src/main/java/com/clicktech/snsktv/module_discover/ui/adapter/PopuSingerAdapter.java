package com.clicktech.snsktv.module_discover.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.PopularSingerResponse;
import com.clicktech.snsktv.module_discover.ui.activity.SingerIntroActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/10.
 */

public class PopuSingerAdapter extends DefaultAdapter<PopularSingerResponse.PopularSingersBean> {
    public PopuSingerAdapter(List<PopularSingerResponse.PopularSingersBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PopularSingerResponse.PopularSingersBean> getHolder(View v) {
        return new Mholder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_popusinger;
    }

    public class Mholder extends BaseHolder<PopularSingerResponse.PopularSingersBean> {

        @BindView(R.id.discover_listen_num)
        TextView listernNum;
        @BindView(R.id.tv_singer_name)
        TextView tv_singer_name;
        @BindView(R.id.tv_trophy)
        TextView tv_trophy;
        @BindView(R.id.discover_item_bgimg)
        ImageView mimg;

        public Mholder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(final PopularSingerResponse.PopularSingersBean data, int position) {
            if (data == null)
                return;

            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();
            imageLoader.loadImage(context, GlideImageConfig.builder()
                    .errorPic(R.mipmap.def_square_large)
                    .placeholder(R.mipmap.def_square_large)
                    .imageView(mimg)
                    .url(data.getUser_photo())
                    .build());


            tv_singer_name.setText(data.getUser_nickname());
            listernNum.setText(String.valueOf(data.getListenCount()));
            tv_trophy.setText(String.valueOf(data.getGiftNum()));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SingerIntroActivity.class);
                    intent.putExtra("userid", data.getId());
                    UiUtils.startActivity(intent);
                }
            });

        }
    }
}
