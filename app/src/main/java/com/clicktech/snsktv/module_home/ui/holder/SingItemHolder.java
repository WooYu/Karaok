package com.clicktech.snsktv.module_home.ui.holder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.ui.activity.LearnSingActivity;
import com.clicktech.snsktv.util.StringHelper;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/26.
 */

public class SingItemHolder extends BaseHolder<SongInfoBean> {

    @BindView(R.id.tv_ranking)
    TextView tvRanking;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_songname)
    TextView tvSongname;
    @BindView(R.id.tv_singername)
    TextView tvSingername;

    public SingItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(final SongInfoBean data, int position) {
        KtvApplication.ktvApplication.getAppComponent().imageLoader()
                .loadImage(context, GlideImageConfig.builder()
                        .url(data.getSong_image())
                        .placeholder(R.mipmap.def_square_small)
                        .errorPic(R.mipmap.def_square_small)
                        .imageView(ivAvatar)
                        .build());
        tvSongname.setText(StringHelper.getLau_With_J_U_C(data.getSong_name_jp(), data.getSong_name_us(), data.getSong_name_cn()));
        tvSingername.setText(StringHelper.getLau_With_J_U_C(data.getSinger_name_jp(), data.getSinger_name_us(), data.getSinger_name_cn()));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LearnSingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("songid", data.getSong_id());
                intent.putExtras(bundle);
                UiUtils.startActivity(intent);
            }
        });

        tvRanking.setBackground(null);
        tvRanking.setText("");
        switch (position) {
            case 0:
                tvRanking.setBackgroundResource(R.mipmap.ranking_1);
                break;
            case 1:
                tvRanking.setBackgroundResource(R.mipmap.ranking_2);
                break;
            case 2:
                tvRanking.setBackgroundResource(R.mipmap.ranking_3);
                break;
            case 3:
                tvRanking.setBackgroundResource(R.mipmap.ranking_4);
                break;
            case 4:
                tvRanking.setBackgroundResource(R.mipmap.ranking_5);
                break;
            case 5:
                tvRanking.setBackgroundResource(R.mipmap.ranking_6);
                break;
            case 6:
                tvRanking.setBackgroundResource(R.mipmap.ranking_7);
                break;
            case 7:
                tvRanking.setBackgroundResource(R.mipmap.ranking_8);
                break;
            case 8:
                tvRanking.setBackgroundResource(R.mipmap.ranking_9);
                break;
            case 9:
                tvRanking.setBackgroundResource(R.mipmap.ranking_10);
                break;

            default:
                tvRanking.setText(String.valueOf(position + 1));
                break;
        }
    }

}
