package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.AlbumGiftListEntity;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/9.
 */

public class GIftRankAdapter extends DefaultAdapter<AlbumGiftListEntity> {


    public GIftRankAdapter(List<AlbumGiftListEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<AlbumGiftListEntity> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_giftrank;
    }


    class MHolder extends BaseHolder<AlbumGiftListEntity> {
        @BindView(R.id.rank)
        TextView rank;
        @BindView(R.id.avatar)
        MLImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.contribution)
        TextView contribution;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(AlbumGiftListEntity data, int position) {
            KtvApplication applicationContext = (KtvApplication) context.getApplicationContext();

            if (data != null) {
                applicationContext.getAppComponent().imageLoader().loadImage(context,
                        GlideImageConfig.builder()
                                .placeholder(R.mipmap.def_avatar_square)
                                .errorPic(R.mipmap.def_avatar_square)
                                .url(data.getUser_photo())
                                .imageView(avatar)
                                .build());

                name.setText(data.getUser_nickname());
                contribution.setText(String.format(context.getString(R.string.format_contribution),
                        data.getCoin_num(), data.getFlower_num()));

                if (position == 0) {
                    rank.setText("");
                    rank.setBackgroundResource(R.mipmap.nation_golde_medal);
                } else if (position == 1) {
                    rank.setText("");
                    rank.setBackgroundResource(R.mipmap.nation_silver_medal);
                } else if (position == 2) {
                    rank.setText("");
                    rank.setBackgroundResource(R.mipmap.nation_bronze_medal);
                } else {

                    rank.setText(String.valueOf(position + 1));
                    rank.setBackground(null);
                }
            }


        }
    }
}
