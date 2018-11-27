package com.clicktech.snsktv.module_mine.ui.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.CommentEntity;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import butterknife.BindView;

import static com.clicktech.snsktv.R.id.reply_name;

/**
 * Created by Administrator on 2017/1/9.
 */

public class CommentItemHolder extends BaseHolder<CommentEntity> {


    @BindView(R.id.avatar)
    MLImageView avatar;
    @BindView(R.id.common_name)
    TextView commonName;
    @BindView(R.id.common_middle)
    TextView commonMiddle;
    @BindView(R.id.commoned_name)
    TextView commonedName;
    @BindView(R.id.reply_left)
    TextView replyLeft;
    @BindView(reply_name)
    TextView replyName;
    @BindView(R.id.reply_detail)
    TextView replyDetail;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.reply_btn)
    TextView replyBtn;
    @BindView(R.id.detail_common)
    TextView detailCommon;


    public CommentItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(CommentEntity data, final int position) {

        if (data == null) {
            return;
        }

        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();
        imageLoader.loadImage(ktvApplication, GlideImageConfig
                .builder()
                .url(data.getUser_photo())
                .errorPic(R.mipmap.def_avatar_round_light)
                .placeholder(R.mipmap.def_avatar_round_light)
                .imageView(avatar)
                .build());

        replyName.setText(TextUtils.isEmpty(data.getUser_nickname()) ? "" : data.getUser_nickname());
        commonName.setText(data.getUser_nickname());
//        commonedName.setText(context.getString(R.string.format_review, TextUtils.isEmpty(data.getWorks_name()) ? "" : data.getWorks_name()));
        commonedName.setText(TextUtils.isEmpty(data.getWorks_name()) ? "" : data.getWorks_name());

        replyDetail.setText(TextUtils.isEmpty(data.getComment_content()) ? "" : data.getComment_content());

        detailCommon.setText(TextUtils.isEmpty(data.getComment_content()) ? "" : data.getComment_content());

//        tv_comment_content.setText(TextUtils.isEmpty(data.getComment_content()) ? "" : data.getComment_content());

        time.setText(TextUtils.isEmpty(data.getAdd_time()) ? "" : data.getAdd_time());

//        detailCommon.setText();

        if (data.getReplyUserId() != null) {

            replyLeft.setVisibility(View.VISIBLE);
            replyName.setVisibility(View.VISIBLE);
            replyDetail.setVisibility(View.VISIBLE);
            detailCommon.setVisibility(View.GONE);

        } else {

            replyLeft.setVisibility(View.GONE);
            replyName.setVisibility(View.GONE);
            replyDetail.setVisibility(View.GONE);
            detailCommon.setVisibility(View.VISIBLE);

        }

        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnViewClickListener.onViewClick(v, getLayoutPosition());
            }
        });
    }

}
