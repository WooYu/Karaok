package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.PrivateLetterEntity;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/6.
 */
public class PrivateLetterAdapter extends DefaultAdapter<PrivateLetterEntity> {


    public PrivateLetterAdapter(List<PrivateLetterEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PrivateLetterEntity> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_privateletter;
    }


    class MHolder extends BaseHolder<PrivateLetterEntity> {

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
        public void setData(PrivateLetterEntity data, int position) {

//            KtvApplication applicationContext = (KtvApplication) context.getApplicationContext();
//            applicationContext.getAppComponent().imageLoader().loadImage(context,
//                    GlideImageConfig.builder()
//                            .errorPic(R.mipmap.def_avatar_square)
//                            .placeholder(R.mipmap.def_avatar_square)
//                            .url("")
//                            .imageView(avatar)
//            .build());

        }
    }


}
