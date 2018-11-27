package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.util.transformations.RoundedCornersTransformation;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/23.
 */

public class MineSingleListAdapter extends DefaultAdapter<SongInfoBean> {


    public MineSingleListAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_minesinglelist;
    }

    class MHolder extends BaseHolder<SongInfoBean> {

        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_music_name)
        TextView mMusicName;
        @BindView(R.id.singer_name)
        TextView singerName;
        @BindView(R.id.common_num)
        TextView commonNum;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(SongInfoBean data, int position) {
            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            ktvApplication.getAppComponent().imageLoader().loadImage(context,
                    GlideImageConfig.builder()
                            .placeholder(R.mipmap.def_square_round)
                            .errorPic(R.mipmap.def_square_round)
                            .transformation(new RoundedCornersTransformation(context, 6))
                            .url(data.getWorks_image())
                            .imageView(ivPhoto)
                            .build());

            mMusicName.setText(data.getWorks_name());
            singerName.setText(data.getUser_nickname());
            commonNum.setText(data.getListen_count());
        }
    }
}
