package com.clicktech.snsktv.module_discover.ui.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.AlbumBean;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/22.
 */

public class AlbumListHolder extends BaseHolder<AlbumBean> {
    @BindView(R.id.iv_mine_album_pic)
    ImageView iv_mine_album_pic;
    @BindView(R.id.tv_mine_album_name)
    TextView tv_mine_album_name;
    @BindView(R.id.tv_mine_album_des)
    TextView tv_mine_album_des;
    @BindView(R.id.tv_mine_album_songcount)
    TextView tv_mine_album_songcount;
    @BindView(R.id.tv_mine_album_commentcount)
    TextView tv_mine_album_commentcount;
    @BindView(R.id.tv_mine_album_giftcount)
    TextView tv_mine_album_giftcount;

    public AlbumListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(AlbumBean data, final int position) {

        if (data == null) {
            return;
        }
        KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
        ImageLoader imageLoader = ktvApplication.getAppComponent().imageLoader();
        imageLoader.loadImage(ktvApplication, GlideImageConfig
                .builder()
                .url(data.getAlbum_image())
                .errorPic(R.mipmap.def_square_large)
                .placeholder(R.mipmap.def_square_large)
                .imageView(iv_mine_album_pic)
                .build());

        tv_mine_album_name.setText(TextUtils.isEmpty(data.getAlbum_name()) ? "" : data.getAlbum_name());
        tv_mine_album_des.setText(TextUtils.isEmpty(data.getAlbum_introduce()) ? "" : data.getAlbum_introduce());
        tv_mine_album_songcount.setText(String.format(
                context.getString(R.string.format_singleofablum), String.valueOf(data.getWorks_sum())));
        tv_mine_album_commentcount.setText(String.format(
                context.getString(R.string.format_commentofalbum), String.valueOf(data.getComment_sum())));
        tv_mine_album_giftcount.setText(String.format(
                context.getString(R.string.format_giftsofalbume), String.valueOf(data.getGift_sum())));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnViewClickListener.onViewClick(v, position);
            }
        });

    }

}
