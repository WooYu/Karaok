package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;

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
 * Created by Administrator on 2017/5/20.
 * 添加单曲
 */

public class AddSingleMusicHeadAdapter extends DefaultAdapter<SongInfoBean> {
    public AddSingleMusicHeadAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_addsinglemusichead;
    }

    class MHolder extends BaseHolder<SongInfoBean> {

        @BindView(R.id.avatar)
        MLImageView avatar;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(SongInfoBean data, int position) {

            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            ktvApplication.getAppComponent().imageLoader().loadImage(context,
                    GlideImageConfig.builder()
                            .placeholder(R.mipmap.def_square_large)
                            .errorPic(R.mipmap.def_square_large)
                            .url(data.getWorks_image())
                            .imageView(avatar)
                            .build());

        }
    }
}
