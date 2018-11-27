package com.clicktech.snsktv.module_discover.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.CommentInfoEntity;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/25.
 */

public class PlayMiddleHolder extends BaseHolder<CommentInfoEntity> {
    @BindView(R.id.iv_comment_avatar)
    MLImageView ivAvatar;
    @BindView(R.id.tv_comment_name)
    TextView tvName;
    @BindView(R.id.tv_comment_time)
    TextView tvTime;
    @BindView(R.id.tv_comment_content)
    TextView tvContent;

    private ImageLoader imageLoader;

    public PlayMiddleHolder(View itemView) {
        super(itemView);
        KtvApplication ktvApplication = (KtvApplication) itemView.getContext().getApplicationContext();
        imageLoader = ktvApplication.getAppComponent().imageLoader();
    }

    @Override
    public void setData(CommentInfoEntity data, int position) {
        //头像
        imageLoader.loadImage(context,
                GlideImageConfig.builder()
                        .url(data.getUser_photo())
                        .placeholder(R.mipmap.def_avatar_round)
                        .errorPic(R.mipmap.def_avatar_round)
                        .imageView(ivAvatar)
                        .build());
        //姓名
        tvName.setText(EmptyUtils.isEmpty(data.getUser_nickname()) ? "" : data.getUser_nickname());
        //时间
        tvTime.setText(EmptyUtils.isEmpty(data.getAdd_time()) ? "" : data.getAdd_time());
        //评论内容
        tvContent.setText(EmptyUtils.isEmpty(data.getComment_content()) ? "" : data.getComment_content());
    }
}
