package com.clicktech.snsktv.module_discover.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/26.
 */

public class PlayLeftTopHolder extends BaseHolder<SongInfoBean> {
    @BindView(R.id.iv_left)
    ImageView mLeftiv;
    @BindView(R.id.iv_avatar)
    MLImageView mAvatariv;
    @BindView(R.id.tv_nickname)
    TextView mNametv;

    private int[] images = new int[]{
            R.mipmap.songdetail_toprank1, R.mipmap.songdetail_toprank2, R.mipmap.songdetail_toprank3
    };
    private ImageLoader mImageLoader;

    public PlayLeftTopHolder(View itemView) {
        super(itemView);
        mImageLoader = ((KtvApplication) itemView.getContext().getApplicationContext())
                .getAppComponent().imageLoader();
    }

    @Override
    public void setData(SongInfoBean data, int position) {
        if (position < 3) {
            mLeftiv.setImageResource(images[position]);
        }

        //头像
        mImageLoader.loadImage(itemView.getContext(), GlideImageConfig.builder()
                .url(data.getUser_photo())
                .errorPic(R.mipmap.def_avatar_round)
                .placeholder(R.mipmap.def_avatar_round)
                .imageView(mAvatariv)
                .build());

        //姓名
        mNametv.setText(EmptyUtils.isEmpty(data.getUser_nickname()) ? "" : data.getUser_nickname());
    }
}
