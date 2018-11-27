package com.clicktech.snsktv.module_home.ui.adapter;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SelectRoleActivity;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.util.HistoryUtils;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import java.util.List;

import butterknife.BindView;

public class ChorusSingerAdapter extends DefaultAdapter<SongInfoBean> {

    public ChorusSingerAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new ChorusSingerHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_chorussinger;
    }

    class ChorusSingerHolder extends BaseHolder<SongInfoBean> {
        @BindView(R.id.tv_indexes)
        TextView tvIndexes;
        @BindView(R.id.iv_photo)
        MLImageView ivPhoto;
        @BindView(R.id.tv_nickname)
        TextView tvNickName;
        @BindView(R.id.tv_chorus_num)
        TextView tvChorusNum;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.tv_chorus)
        TextView tvChorus;

        public ChorusSingerHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(final SongInfoBean data, int position) {
            if (position == 0) {
                tvIndexes.setVisibility(View.VISIBLE);
            } else {
                SongInfoBean preSongInfo = mInfos.get(position - 1);
                if (preSongInfo.getFirst_letter().equals(data.getFirst_letter())) {
                    tvIndexes.setVisibility(View.GONE);
                } else {
                    tvIndexes.setVisibility(View.VISIBLE);
                }
            }
            tvIndexes.setText(data.getFirst_letter());

            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            ktvApplication.getAppComponent().imageLoader().loadImage(context,
                    GlideImageConfig.builder()
                            .url(data.getUser_photo())
                            .placeholder(R.mipmap.def_square_round)
                            .errorPic(R.mipmap.def_square_round)
                            .imageView(ivPhoto).build());
            tvNickName.setText(data.getUser_nickname());
            String chorusCount = data.getChorus_count();
            if (EmptyUtils.isNotEmpty(chorusCount)) {
                tvChorusNum.setText(String.format(context.getString(R.string.format_chorusnumber), chorusCount));
            } else {
                tvChorusNum.setText(String.format(context.getString(R.string.format_chorusnumber), String.valueOf(0)));
            }
            tvComment.setText(data.getWorks_desc());

            tvChorus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chorusIntent = new Intent(context, SelectRoleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("songinfo", data);
                    bundle.putBoolean("needmv", false);
                    chorusIntent.putExtras(bundle);
                    HistoryUtils.putMusicHistory(context, data);
                    UiUtils.startActivity(chorusIntent);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(context, SongDetailsActivity.class);
                    Bundle songDetailBundle = new Bundle();
                    songDetailBundle.putParcelable("songinfo", data);
                    detailIntent.putExtras(songDetailBundle);
                    UiUtils.startActivity(detailIntent);
                }
            });
        }
    }
}
