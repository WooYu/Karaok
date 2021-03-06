package com.clicktech.snsktv.module_home.ui.adapter;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.ConstUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.ui.activity.KSingActivity;
import com.clicktech.snsktv.util.HistoryUtils;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SongsortAdapter extends DefaultAdapter<SongInfoBean> {

    public SongsortAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new SongSortHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_songsort;
    }

    class SongSortHolder extends BaseHolder<SongInfoBean> {
        @BindView(R.id.tv_indexes)
        TextView tvIndexes;
        @BindView(R.id.iv_photo)
        MLImageView ivPhoto;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_size)
        TextView tvSize;

        private SongSortHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.tv_ksong)
        void OnTvKSongClicked() {
            Intent kSongIntent = new Intent(context, KSingActivity.class);
            SongInfoBean songInfoBean = mInfos.get(getLayoutPosition());
            HistoryUtils.putMusicHistory(context, songInfoBean);
            Bundle bundle = new Bundle();
            bundle.putParcelable("songinfo", songInfoBean);
            bundle.putInt("workstype", context.getResources().getInteger(R.integer.workstype_common));
            kSongIntent.putExtras(bundle);
            UiUtils.startActivity(kSongIntent);
        }

        @Override
        public void setData(SongInfoBean data, int position) {
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

            KtvApplication.ktvApplication.getAppComponent().imageLoader().loadImage(
                    context, GlideImageConfig.builder()
                            .placeholder(R.mipmap.def_square_round)
                            .errorPic(R.mipmap.def_square_round)
                            .url(data.getSong_image())
                            .imageView(ivPhoto)
                            .build()
            );

            tvName.setText(StringHelper.getLau_With_J_U_C(
                    data.getSong_name_jp(), data.getSong_name_us(), data.getSong_name_cn()));
            tvSize.setText(StringHelper.getFileSize(context,data.getAccompany_size(),
                    ConstUtils.MemoryUnit.KB));

        }
    }
}
