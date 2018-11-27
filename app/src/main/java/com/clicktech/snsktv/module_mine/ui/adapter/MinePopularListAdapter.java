package com.clicktech.snsktv.module_mine.ui.adapter;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.MediaFile;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;
import com.clicktech.snsktv.widget.videoplayer.ListPlayer;
import com.zhy.autolayout.utils.AutoUtils;

import org.simple.eventbus.EventBus;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;

/**
 * Created by Administrator on 2017/1/20.
 * 我的-热门Adapter
 */

public class MinePopularListAdapter extends RecyclerView.Adapter<MinePopularListAdapter.PopularViewHolder> {
    int mDescribeWidth = -1;
    int mDescribeHeight = -1;
    private List<SongInfoBean> mPopularList;
    private ItemViewClickListener mListener;
    private int mCurClickPosition = -1;

    public MinePopularListAdapter(List<SongInfoBean> mPopularList) {
        this.mPopularList = mPopularList;
    }

    public void updateData(List<SongInfoBean> list) {
        this.mPopularList = list;
        notifyDataSetChanged();
    }

    @Override
    public PopularViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attent_works,
                parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularViewHolder workViewHolder, final int position) {

        SongInfoBean workInfoEntity = mPopularList.get(position);

        final KtvApplication context = KtvApplication.ktvApplication;
        ImageLoader imageLoader = context.getAppComponent().imageLoader();

        //歌手名称
        workViewHolder.tvSingername.setText(workInfoEntity.getUser_nickname());
        //作品名称
        workViewHolder.tvNameofsong.setText(workInfoEntity.getWorks_name());
        //作品名称
        workViewHolder.tvSongname.setText(workInfoEntity.getWorks_name());
        //性别
        int sex = Integer.parseInt(workInfoEntity.getUser_sex());
        if (sex == context.getResources().getInteger(R.integer.sex_man)) {
            workViewHolder.ivSex.setImageResource(R.mipmap.mine_sex_male);
            workViewHolder.ivAvatar.setBorderColor(context.getResources().getColor(R.color.green));
        } else {
            workViewHolder.ivSex.setImageResource(R.mipmap.mine_sex_female);
            workViewHolder.ivAvatar.setBorderColor(context.getResources().getColor(R.color.pink));
        }
        //头像
        imageLoader.loadImage(context, GlideImageConfig.builder()
                .url(workInfoEntity.getUser_photo())
                .placeholder(R.mipmap.def_avatar_square)
                .errorPic(R.mipmap.def_avatar_square)
                .imageView(workViewHolder.ivAvatar)
                .build());
        //评论、收藏、收听
        String commentnum = EmptyUtils.isEmpty(workInfoEntity.getCommentNum()) ?
                "0" : workInfoEntity.getCommentNum();
        workViewHolder.tvListencomment.setText(String.format(
                context.getString(R.string.format_listencomment), workInfoEntity.getListen_count(),
                commentnum));
        //作品描述
        workViewHolder.tvDescribe.setText(workInfoEntity.getWorks_desc());
        //作品图片
        imageLoader.loadImage(context, GlideImageConfig.builder()
                .url(workInfoEntity.getWorks_image())
                .placeholder(R.mipmap.def_square_large)
                .errorPic(R.mipmap.def_square_large)
                .imageView(workViewHolder.ivCover)
                .build());
        //发布时间
        workViewHolder.tvTime.setText(workInfoEntity.getAdd_time());

        //播放作品
        String worksurl = BuildConfig.APP_DOMAIN_File + workInfoEntity.getWorks_url();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) workViewHolder.listPlayer.getLayoutParams();
        if (MediaFile.isVideoFileType(worksurl)) {
            workViewHolder.rlMusicalbum.setVisibility(View.GONE);
            layoutParams.width = mDescribeWidth;
            layoutParams.height = mDescribeHeight;
        } else {
            workViewHolder.rlMusicalbum.setVisibility(View.VISIBLE);
            layoutParams.width = 1;
            layoutParams.height = 1;
        }
        workViewHolder.listPlayer.setLayoutParams(layoutParams);

        setMultiMediaView(workViewHolder, position, workInfoEntity);

    }

    @Override
    public int getItemCount() {
        return EmptyUtils.isEmpty(mPopularList) ? 0 : mPopularList.size();
    }


    //设置音视频播放
    private void setMultiMediaView(final PopularViewHolder holder, final int position, final SongInfoBean workInfoEntity) {
        LinkedHashMap hashMap = new LinkedHashMap();
        hashMap.put(JZVideoPlayer.URL_KEY_DEFAULT, BuildConfig.APP_DOMAIN_File + workInfoEntity.getWorks_url());
        Object[] objects = new Object[2];
        objects[0] = hashMap;
        objects[1] = true;
        holder.listPlayer.setUp(objects, 0, JZVideoPlayer.SCREEN_WINDOW_LIST, "");
        KtvApplication.ktvApplication.getAppComponent().imageLoader().loadImage(
                KtvApplication.ktvApplication, GlideImageConfig.builder()
                        .url(workInfoEntity.getWorks_image())
                        .placeholder(R.mipmap.def_square_large)
                        .errorPic(R.mipmap.def_square_large)
                        .imageView(holder.listPlayer.thumbImageView)
                        .build());
        holder.listPlayer.widthRatio = 1;
        holder.listPlayer.heightRatio = 1;

        holder.listPlayer.setmListener(new ListPlayer.ClickStartBtnCallBack() {

            @Override
            public void onStatePreparing() {
                changePlayStatus(holder, true, position);
                addToPlayList(position);
            }

            @Override
            public void onStatePlaying() {
                changePlayStatus(holder, true, position);
            }

            @Override
            public void onStatePause() {
                changePlayStatus(holder, false, position);
            }

            @Override
            public void onStateAutoComplete() {
                changePlayStatus(holder, false, position);
            }

            @Override
            public void onStateError() {
                changePlayStatus(holder, false, position);
            }

            @Override
            public void onCompletion() {
                changePlayStatus(holder, false, position);
            }
        });

        holder.ivPlaypause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.listPlayer.startButton.performClick();
            }
        });

        if (workInfoEntity.isbPlayStatus()) {
            holder.ivPlaypause.setImageResource(R.mipmap.music_play);
        } else {
            holder.ivPlaypause.setImageResource(R.mipmap.music_pause);
        }
    }

    //改变播放状态
    private void changePlayStatus(PopularViewHolder holder, boolean state, int changePosition) {
        if (state) {
            holder.ivPlaypause.setImageResource(R.mipmap.music_play);
        } else {
            holder.ivPlaypause.setImageResource(R.mipmap.music_pause);
        }
        SongInfoBean changeWorkInfo = mPopularList.get(changePosition);
        changeWorkInfo.setbPlayStatus(state);
        if (mCurClickPosition != -1 && mCurClickPosition != changePosition) {
            SongInfoBean lastWorkSingleBean = mPopularList.get(mCurClickPosition);
            lastWorkSingleBean.setbPlayStatus(false);
            notifyItemChanged(mCurClickPosition);
        }

        mCurClickPosition = changePosition;

        if (null != mListener) {
            mListener.onClickPlayPause(mCurClickPosition);
        }
    }

    //添加到播放列表
    private void addToPlayList(int playPosition) {
        SongInfoBean songInfoBean = mPopularList.get(playPosition);
        Bundle addSongBundle = new Bundle();
        addSongBundle.putParcelable("songinfo", songInfoBean);
        addSongBundle.putInt("type", 1);
        Message message = new Message();
        message.setData(addSongBundle);
        EventBus.getDefault().post(message, EventBusTag.PLAYLIST_ADDSONG);
    }

    public void setItemEventListener(ItemViewClickListener listener) {
        this.mListener = listener;
    }

    public interface ItemViewClickListener {
        //点击itemview
        void onClickItem(int position);

        //点击头像
        void onClickSingerIntro(int position);

        //点击礼物
        void onClickPresent(int position);

        //点击评论
        void onClickComment(int position);

        //点击分享
        void onClickShare(int position);

        //点击播放暂停
        void onClickPlayPause(int position);
    }

    class PopularViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        MLImageView ivAvatar;
        @BindView(R.id.iv_sex)
        ImageView ivSex;
        @BindView(R.id.tv_singername)
        TextView tvSingername;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_songname)
        TextView tvSongname;
        @BindView(R.id.rl_workinfo)
        RelativeLayout rlWorkinfo;
        @BindView(R.id.tv_describe)
        TextView tvDescribe;
        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.iv_playpause)
        ImageView ivPlaypause;
        @BindView(R.id.tv_nameofsong)
        TextView tvNameofsong;
        @BindView(R.id.tv_listencomment)
        TextView tvListencomment;
        @BindView(R.id.rl_musicalbum)
        RelativeLayout rlMusicalbum;
        @BindView(R.id.listplayer)
        ListPlayer listPlayer;
        @BindView(R.id.iv_gift)
        ImageView ivGift;
        @BindView(R.id.iv_comment)
        ImageView ivComment;
        @BindView(R.id.iv_share)
        ImageView ivShare;
        @BindView(R.id.itemview)
        LinearLayout itemview;
        @BindView(R.id.ll_singerintro)
        LinearLayout llSingerIntro;


        public PopularViewHolder(View view) {
            super(view);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, view);

            if (mDescribeHeight == -1 || mDescribeWidth == -1) {
                tvDescribe.post(new Runnable() {
                    @Override
                    public void run() {
                        mDescribeWidth = tvDescribe.getMeasuredWidth();
                        mDescribeHeight = tvDescribe.getMeasuredHeight();
                    }
                });
            }

        }

        @OnClick(R.id.itemview)
        public void onClickedItemView() {
            if (null != mListener) {
                if (listPlayer != JZVideoPlayerManager.getCurrentJzvd()) {
                    listPlayer.startVideo();
                }
                mListener.onClickItem(getLayoutPosition());
            }
        }

        @OnClick({R.id.ll_singerintro, R.id.iv_avatar})
        public void OnClickedSingerIntro() {
            if (null != mListener) {
                mListener.onClickSingerIntro(getLayoutPosition());
            }
        }

        @OnClick(R.id.iv_gift)
        void onClickedGift() {
            if (null != mListener) {
                mListener.onClickPresent(getLayoutPosition());
            }
        }

        @OnClick(R.id.iv_comment)
        void onClickedComment() {
            if (null != mListener) {
                mListener.onClickComment(getLayoutPosition());
            }
        }

        @OnClick(R.id.iv_share)
        void onClickedShare() {
            if (null != mListener) {
                mListener.onClickShare(getLayoutPosition());
            }
        }
    }
}
