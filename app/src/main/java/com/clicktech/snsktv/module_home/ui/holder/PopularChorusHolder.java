package com.clicktech.snsktv.module_home.ui.holder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.ui.listener.JoinInChorusListener;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.transformations.RoundedCornersTransformation;

import butterknife.BindView;

public class PopularChorusHolder extends BaseHolder<SongInfoBean> {

    @BindView(R.id.chours_avatar)
    ImageView chours_avatar;
    @BindView(R.id.tv_music_name)
    TextView tv_music_name;   //曲名
    @BindView(R.id.tv_singer_name)
    TextView tv_singer_name; //昵称
    @BindView(R.id.tv_chours_num)
    TextView tv_chours_num;  //合唱曲数
    @BindView(R.id.tv_comment)
    TextView tv_comment;   //评论
    @BindView(R.id.tv_chorus)
    TextView tv_chorus;

    private JoinInChorusListener mListener;

    public PopularChorusHolder(View itemView, JoinInChorusListener listener) {
        super(itemView);
        mListener = listener;
    }

    @Override
    public void setData(SongInfoBean data, final int position) {
        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        tv_music_name.setText(StringHelper.getLau_With_J_U_C(data.getSong_name_jp(), data.getSong_name_us(), data.getSong_name_cn()));
        ktvApplication.getAppComponent().imageLoader().loadImage(ktvApplication, GlideImageConfig
                .builder()
                .url(data.getSong_image())
                .transformation(new RoundedCornersTransformation(ktvApplication, 6))
                .errorPic(R.mipmap.def_square_round)
                .placeholder(R.mipmap.def_square_round)
                .imageView(chours_avatar)
                .build());
        tv_singer_name.setText(data.getUser_nickname());
        tv_comment.setText(data.getWorks_desc());
        tv_chours_num.setText(String.format(ktvApplication.getString(R.string.format_chorusnumber),
                String.valueOf(data.getChorus_count())));
        tv_chorus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener)
                    mListener.joininchorus(position);
            }
        });

    }
}
