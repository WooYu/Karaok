package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.AlbumDetailResponse;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/27.
 */

public class AlbumCommentAdapter extends DefaultAdapter<AlbumDetailResponse.AlbumCommentListBean> {


    public AlbumCommentAdapter(List<AlbumDetailResponse.AlbumCommentListBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<AlbumDetailResponse.AlbumCommentListBean> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_album_comment;
    }

    class MHolder extends BaseHolder<AlbumDetailResponse.AlbumCommentListBean> {
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.coment)
        TextView coment;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(AlbumDetailResponse.AlbumCommentListBean data, int position) {

            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            ktvApplication.getAppComponent().imageLoader().loadImage(context, GlideImageConfig.builder()
                    .url(data.getUser_photo())
                    .errorPic(R.mipmap.def_avatar_round)
                    .placeholder(R.mipmap.def_avatar_round)
                    .transformation(new CircleWithBorderTransformation(context))
                    .imageView(avatar)
                    .build());

            name.setText(data.getUser_nickname());
            time.setText(data.getAdd_time());
            coment.setText(data.getComment_content());

        }
    }

}
