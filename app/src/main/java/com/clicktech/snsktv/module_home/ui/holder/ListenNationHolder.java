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
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/19.
 */

public class ListenNationHolder extends BaseHolder<SongInfoBean> {
    @BindView(R.id.iv_ranking)
    ImageView ivRanking;
    @BindView(R.id.tv_ranking)
    TextView tvRanking;
    @BindView(R.id.iv_avatar)
    MLImageView ivAvatar;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_name)
    TextView tvName;

    public ListenNationHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SongInfoBean data, int position) {

        KtvApplication ktvApplication = KtvApplication.ktvApplication;
        //排名
        if (position < 10) {
            ivRanking.setVisibility(View.VISIBLE);
            tvRanking.setVisibility(View.GONE);
            ivRanking.setImageResource(StringHelper.getImageResource2ByRanking(position));
        } else {
            ivRanking.setVisibility(View.GONE);
            tvRanking.setVisibility(View.VISIBLE);
            tvRanking.setText(String.valueOf(position + 1));
        }

        //头像
        ktvApplication.getAppComponent().imageLoader().loadImage(context,
                GlideImageConfig.builder()
                        .url(data.getUser_photo())
                        .placeholder(R.mipmap.def_avatar_square)
                        .errorPic(R.mipmap.def_avatar_square)
                        .imageView(ivAvatar)
                        .build());

        //性别
        int sex = Integer.parseInt(data.getUser_sex());
        if (context.getResources().getInteger(R.integer.sex_female) == sex) {
            ivSex.setImageResource(R.mipmap.mine_sex_female);
            ivAvatar.setBorderColor(context.getResources().getColor(R.color.pink));
        } else {
            ivSex.setImageResource(R.mipmap.mine_sex_male);
            ivAvatar.setBorderColor(context.getResources().getColor(R.color.green));
        }

        //姓名
        tvName.setText(data.getUser_nickname());

    }

    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onViewClick(view, getAdapterPosition() - 1);
        }
    }
}
