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
import com.clicktech.snsktv.entity.MineAttentEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.WorkAlbumBean;
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
 * Created by Administrator on 2017/1/20.al
 * 我的关注中的歌曲信息Adapter
 */

public class MineAttentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_MUSIC = 1;
    private final int TYPE_VIDEO = 2;
    private final int TYPE_ALBUM = 3;

    private List<MineAttentEntity> mAttentList;
    private ItemViewClickListener mListener;
    private int mCurClickPosition = -1;

    public MineAttentListAdapter(List<MineAttentEntity> list) {
        this.mAttentList = list;
    }

    public void updateData(List<MineAttentEntity> list) {
        this.mAttentList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (null != mAttentList.get(position).getWorkAlbum()) {
            return TYPE_ALBUM;
        } else if (null != mAttentList.get(position).getWorkInfo()) {
            SongInfoBean workInfo = mAttentList.get(position).getWorkInfo();
            if (MediaFile.isAudioFileType(workInfo.getWorks_url())) {
                return TYPE_MUSIC;
            } else if (MediaFile.isVideoFileType(workInfo.getWorks_url())) {
                return TYPE_VIDEO;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public WorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.adapter_attent_works, parent, false);
        return new WorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!(holder instanceof WorkViewHolder)) {
            return;
        }

        final WorkViewHolder workViewHolder = (WorkViewHolder) holder;
        MineAttentEntity mineAttentEntity = mAttentList.get(position);
        SongInfoBean workInfoEntity = mineAttentEntity.getWorkInfo();
        WorkAlbumBean workAlbumEntity = mineAttentEntity.getWorkAlbum();

        int worktype = getItemViewType(position);
        final KtvApplication context = KtvApplication.ktvApplication;
        ImageLoader imageLoader = context.getAppComponent().imageLoader();

        if (worktype == TYPE_VIDEO) {
            workViewHolder.rlMusciAlbum.setVisibility(View.GONE);
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) workViewHolder.listPlayer.getLayoutParams();
            layoutParams.width = 1;
            layoutParams.height = 1;
            workViewHolder.listPlayer.setLayoutParams(layoutParams);

            workViewHolder.rlMusciAlbum.setVisibility(View.VISIBLE);
            if (worktype == TYPE_ALBUM) {
                workViewHolder.ivPlaypause.setVisibility(View.GONE);
            } else {
                workViewHolder.ivPlaypause.setVisibility(View.VISIBLE);
            }

        }

        String urlOfAvatar;
        String nickname;
        String songname;
        String collectnum = "0";
        String listennum = "0";
        String commentnum;
        String describe;
        String urlofcover;
        String releasetime;
        int sex;
        if (worktype == TYPE_ALBUM && null != workAlbumEntity) {
            urlOfAvatar = workAlbumEntity.getUser_photo();
            nickname = workAlbumEntity.getUser_nickname();
            songname = workAlbumEntity.getAlbum_name();
            collectnum = workAlbumEntity.getCollect_sum();
            commentnum = workAlbumEntity.getComment_sum();
            describe = workAlbumEntity.getAlbum_introduce();
            urlofcover = workAlbumEntity.getAlbum_image();
            releasetime = workAlbumEntity.getAdd_time();
            sex = workAlbumEntity.getUser_sex();
        } else if (null != workInfoEntity) {
            urlOfAvatar = workInfoEntity.getUser_photo();
            nickname = workInfoEntity.getUser_nickname();
            songname = workInfoEntity.getWorks_name();
            listennum = workInfoEntity.getListen_count();
            commentnum = workInfoEntity.getCommentNum();
            describe = workInfoEntity.getWorks_desc();
            urlofcover = workInfoEntity.getWorks_image();
            releasetime = workInfoEntity.getAdd_time();
            sex = Integer.parseInt(workInfoEntity.getUser_sex());
        } else {
            return;
        }

        //歌手名称
        workViewHolder.tvSingername.setText(nickname);
        //作品名称
        workViewHolder.tvNameofsong.setText(songname);
        //作品名称
        workViewHolder.tvSongname.setText(songname);
        //性别
        if (sex == context.getResources().getInteger(R.integer.sex_man)) {
            workViewHolder.ivSex.setImageResource(R.mipmap.mine_sex_male);
            workViewHolder.ivAvatar.setBorderColor(context.getResources().getColor(R.color.green));
        } else {
            workViewHolder.ivSex.setImageResource(R.mipmap.mine_sex_female);
            workViewHolder.ivAvatar.setBorderColor(context.getResources().getColor(R.color.pink));
        }
        //头像
        imageLoader.loadImage(context, GlideImageConfig.builder()
                .url(urlOfAvatar)
                .placeholder(R.mipmap.def_avatar_square)
                .errorPic(R.mipmap.def_avatar_square)
                .imageView(workViewHolder.ivAvatar)
                .build());
        //评论、收藏、收听
        if (worktype == TYPE_ALBUM) {
            workViewHolder.tvListencomment.setText(String.format(
                    context.getString(R.string.format_collectcomment), collectnum,
                    commentnum));
        } else {
            workViewHolder.tvListencomment.setText(String.format(
                    context.getString(R.string.format_listencomment), listennum,
                    commentnum));
        }
        //作品描述
        workViewHolder.tvDescribe.setText(describe);
        //作品图片
        imageLoader.loadImage(context, GlideImageConfig.builder()
                .url(urlofcover)
                .placeholder(R.mipmap.def_square_large)
                .errorPic(R.mipmap.def_square_large)
                .imageView(workViewHolder.ivCover)
                .build());
        //发布时间
        workViewHolder.tvTime.setText(releasetime);

        //播放作品
        if (worktype != TYPE_ALBUM) {
            setMultiMediaView(workViewHolder, position, workInfoEntity);
        }

    }

    @Override
    public int getItemCount() {
        return EmptyUtils.isEmpty(mAttentList) ? 0 : mAttentList.size();
    }

    //设置音视频播放
    private void setMultiMediaView(final WorkViewHolder holder, final int position, final SongInfoBean workInfoEntity) {
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
    private void changePlayStatus(WorkViewHolder holder, boolean state, int changePosition) {
        if (state) {
            holder.ivPlaypause.setImageResource(R.mipmap.music_play);
        } else {
            holder.ivPlaypause.setImageResource(R.mipmap.music_pause);
        }
        SongInfoBean changeWorkInfo = mAttentList.get(changePosition).getWorkInfo();
        changeWorkInfo.setbPlayStatus(state);
        if (mCurClickPosition != -1 && mCurClickPosition != changePosition) {
            SongInfoBean lastWorkSingleBean = mAttentList.get(mCurClickPosition).getWorkInfo();
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
        SongInfoBean songInfoBean = mAttentList.get(playPosition).getWorkInfo();
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

        //点击播放按钮
        void onClickPlayPause(int position);
    }

    class WorkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        MLImageView ivAvatar;
        @BindView(R.id.iv_sex)
        ImageView ivSex;
        @BindView(R.id.tv_singername)
        TextView tvSingername;
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
        @BindView(R.id.iv_gift)
        ImageView ivGift;
        @BindView(R.id.iv_comment)
        ImageView ivComment;
        @BindView(R.id.iv_share)
        ImageView ivShare;
        @BindView(R.id.itemview)
        LinearLayout itemview;
        @BindView(R.id.listplayer)
        ListPlayer listPlayer;
        @BindView(R.id.rl_musicalbum)
        RelativeLayout rlMusciAlbum;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.ll_singerintro)
        LinearLayout llSingerIntro;

        WorkViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.itemview)
        void onClickedItemView() {
            if (null != mListener) {
                if (getItemViewType() != TYPE_ALBUM
                        && listPlayer != JZVideoPlayerManager.getCurrentJzvd()) {
                    listPlayer.startVideo();
                }
                mListener.onClickItem(getLayoutPosition());
            }
        }

        @OnClick({R.id.iv_avatar, R.id.ll_singerintro})
        void onClickedAvatar() {
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
