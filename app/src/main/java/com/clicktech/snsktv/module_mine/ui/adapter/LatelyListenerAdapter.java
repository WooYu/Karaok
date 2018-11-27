package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.LatelyListenerResponse;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/2.
 */

public class LatelyListenerAdapter extends DefaultAdapter<LatelyListenerResponse.CurrentListBean> {


    public LatelyListenerAdapter(List<LatelyListenerResponse.CurrentListBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<LatelyListenerResponse.CurrentListBean> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_latelylistener;
    }

    class MHolder extends BaseHolder<LatelyListenerResponse.CurrentListBean> {
        @BindView(R.id.avatar)
        MLImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.desc)
        TextView desc;
        @BindView(R.id.time)
        TextView time;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(LatelyListenerResponse.CurrentListBean data, int position) {

            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            ktvApplication.getAppComponent().imageLoader()
                    .loadImage(context, GlideImageConfig.builder()
                            .url(data.getUser_photo())
                            .imageView(avatar)
                            .errorPic(R.mipmap.def_avatar_square)
                            .placeholder(R.mipmap.def_avatar_square)
                            .build());

            name.setText(data.getUser_nickname());

            if (data.getType() == 0) {
                desc.setText(context.getString(R.string.listener_visithome));

            } else if (data.getType() == 1) {
                desc.setText(context.getString(R.string.listener_visitwork));
            }

            time.setText(data.getAdd_time());

        }

    }

}
