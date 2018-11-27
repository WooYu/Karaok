package com.clicktech.snsktv.module_mine.ui.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.util.transformations.RoundedCornersTransformation;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/27.
 */

public class AlbumMusicAdapter extends DefaultAdapter<SongInfoBean> {


    public AlbumMusicAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_album_music;
    }

    class MHolder extends BaseHolder<SongInfoBean> {
        @BindView(R.id.chours_avatar)
        ImageView choursAvatar;
        @BindView(R.id.tv_music_name)
        TextView tvMusicName;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.enter)
        ImageView enter;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(final SongInfoBean data, int position) {

            KtvApplication ktvApplication = KtvApplication.ktvApplication;
            ktvApplication.getAppComponent().imageLoader().loadImage(context,
                    GlideImageConfig.builder()
                            .url(data.getWorks_image())
                            .transformation(new RoundedCornersTransformation(context, 6))
                            .errorPic(R.mipmap.def_square_round)
                            .placeholder(R.mipmap.def_square_round)
                            .imageView(choursAvatar)
                            .build());

            tvMusicName.setText(data.getWorks_name());
            tvComment.setText(String.valueOf(data.getListen_count()));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SongDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("songinfo", data);
                    intent.putExtras(bundle);
                    UiUtils.startActivity(intent);
                }
            });


        }
    }

}
