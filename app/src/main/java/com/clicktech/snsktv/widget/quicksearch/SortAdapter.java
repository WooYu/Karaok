package com.clicktech.snsktv.widget.quicksearch;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SingerInfoEntity;
import com.clicktech.snsktv.util.StringHelper;

import java.util.List;

import butterknife.BindView;

public class SortAdapter extends DefaultAdapter<SingerInfoEntity> {


    public SortAdapter(List<SingerInfoEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SingerInfoEntity> getHolder(View v) {
        return new SortHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_sort_item;
    }

    class SortHolder extends BaseHolder<SingerInfoEntity> {
        @BindView(R.id.tv_indexes)
        TextView tvIndexes;
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_name)
        TextView tvName;

        public SortHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(SingerInfoEntity data, int position) {
            if (position == 0) {
                tvIndexes.setVisibility(View.VISIBLE);
            } else {
                SingerInfoEntity preSongInfo = mInfos.get(position - 1);
                if (preSongInfo.getFirst_letter().equals(data.getFirst_letter())) {
                    tvIndexes.setVisibility(View.GONE);
                } else {
                    tvIndexes.setVisibility(View.VISIBLE);
                }
            }
            tvIndexes.setText(data.getFirst_letter());

            KtvApplication.ktvApplication.getAppComponent().imageLoader().loadImage(context,
                    GlideImageConfig.builder()
                            .url(data.getSinger_photo())
                            .placeholder(R.mipmap.def_avatar_round)
                            .errorPic(R.mipmap.def_avatar_round)
                            .imageView(ivPhoto)
                            .build());
            tvName.setText(StringHelper.getLau_With_J_U_C(
                    data.getSinger_name_jp(), data.getSinger_name_us(), data.getSinger_name_cn()
            ));
        }
    }
}