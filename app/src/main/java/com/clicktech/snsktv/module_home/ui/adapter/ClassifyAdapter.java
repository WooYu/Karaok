package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.ConstUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/5.
 */

public class ClassifyAdapter extends DefaultAdapter<SongInfoBean> {
    public ClassifyAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new ClassifyHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_classify;
    }

    class ClassifyHolder extends BaseHolder<SongInfoBean> {

        @BindView(R.id.tv_indexes)
        TextView tvIndexes;
        @BindView(R.id.iv_photo)
        MLImageView ivPhoto;
        @BindView(R.id.tv_autor)
        TextView tvAutor;
        @BindView(R.id.tv_workname)
        TextView tvWorkName;
        @BindView(R.id.tv_worksize)
        TextView tvWorkSize;
        @BindView(R.id.tv_ksing)
        TextView tvKSing;

        public ClassifyHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(final SongInfoBean data, final int position) {

            if (EmptyUtils.isNotEmpty(data.getFirst_letter())) {
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
            } else {
                tvIndexes.setVisibility(View.GONE);
            }

            tvIndexes.setText(data.getFirst_letter());

            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            ktvApplication.getAppComponent().imageLoader().loadImage(context,
                    GlideImageConfig.builder()
                            .url(data.getSong_image())
                            .placeholder(R.mipmap.def_square_round)
                            .errorPic(R.mipmap.def_square_round)
                            .imageView(ivPhoto).build());
            tvWorkName.setText(StringHelper.getLau_With_J_U_C(data.getSong_name_jp(), data.getSong_name_us(), data.getSong_name_cn()));
            tvAutor.setText(StringHelper.getLau_With_J_U_C(data.getSinger_name_jp(), data.getSinger_name_us(), data.getSinger_name_cn()));
            tvWorkSize.setText(StringHelper.getFileSize(context,data.getAccompany_size(),
                    ConstUtils.MemoryUnit.KB));

            tvKSing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, data, position);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, data, position);
                }
            });


        }
    }
}
