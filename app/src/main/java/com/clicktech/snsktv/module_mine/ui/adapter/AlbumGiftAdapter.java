package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.AlbumGiftListEntity;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/27.
 */

public class AlbumGiftAdapter extends DefaultAdapter<AlbumGiftListEntity> {


    public AlbumGiftAdapter(List<AlbumGiftListEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<AlbumGiftListEntity> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_album_gift;
    }

    class MHolder extends BaseHolder<AlbumGiftListEntity> {
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.m_list_num)
        TextView mListNum;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(AlbumGiftListEntity data, int position) {
            if (null == data)
                return;

            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            ktvApplication.getAppComponent().imageLoader().loadImage(context,
                    GlideImageConfig.builder()
                            .url(data.getUser_photo())
                            .placeholder(R.mipmap.def_avatar_round)
                            .errorPic(R.mipmap.def_avatar_round)
                            .transformation(new CircleWithBorderTransformation(context))
                            .imageView(avatar)
                            .build());

            mListNum.setText(String.valueOf(data.getFlower_or_gift()));


        }
    }
}
