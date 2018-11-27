package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/23.
 * 本地录音
 */

public class LocalRecordingAdapter extends DefaultAdapter<SongInfoBean> {


    public LocalRecordingAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_localrecording;
    }

    class MHolder extends BaseHolder<SongInfoBean> {
        @BindView(R.id.chours_avatar)
        MLImageView choursAvatar;
        @BindView(R.id.tv_music_name)
        TextView tvMusicName;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_time)
        TextView time;
        @BindView(R.id.tv_release)
        TextView tvRelease;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(SongInfoBean data, int position) {

            if (data == null) {
                return;
            }

            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            ktvApplication.getAppComponent().imageLoader().loadImage(context,
                    GlideImageConfig.builder()
                            .placeholder(R.mipmap.def_avatar_square)
                            .errorPic(R.mipmap.def_avatar_square)
                            .url(data.getWorks_image())
                            .imageView(choursAvatar)
                            .build());

            tvMusicName.setText(data.getWorks_name());
            tvComment.setText(data.getCommentNum());
            time.setText(data.getAdd_time());

        }
    }
}
