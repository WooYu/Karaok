package com.clicktech.snsktv.module_mine.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/5/26.
 */

public class SwipCollectionListAdapter extends SwipeMenuAdapter<SwipCollectionListAdapter.SwipCollectionHolder> {

    private List<SongInfoBean> dataList;
    private Context mContex;
    private SwipCollectionListAdapter.OnItemClickListener mOnItemClickListener;

    public SwipCollectionListAdapter(List<SongInfoBean> list, Context context) {
        this.dataList = list;
        mContex = context;
    }

    public void updateData(List<SongInfoBean> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(SwipCollectionListAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_fragment_mine_mycollection, parent, false);
    }

    @Override
    public SwipCollectionHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        SwipCollectionHolder mHolder = new SwipCollectionHolder(realContentView);
        mHolder.mOnItemClickListener = mOnItemClickListener;
        return mHolder;
    }

    @Override
    public void onBindViewHolder(SwipCollectionHolder holder, int position) {
        SongInfoBean data = dataList.get(position);

        ImageLoader imageLoader = KtvApplication.ktvApplication.getAppComponent().imageLoader();
        int store_type = Integer.parseInt(data.getStore_type());
        if (store_type == 0) {  //作品
            holder.singerName.setText(TextUtils.isEmpty(data.getUser_nickname()) ? "" : data.getUser_nickname());
            holder.songName.setText(TextUtils.isEmpty(data.getWorks_name()) ? "" : data.getWorks_name());
            holder.comment.setText(String.format(mContex.getString(R.string.format_commentcount), String.valueOf(data.getCommentNum())));
            holder.listen.setText(String.format(mContex.getString(R.string.format_commentcount), String.valueOf(data.getListen_count())));
            holder.time.setText(data.getAdd_time());

            imageLoader.loadImage(mContex, GlideImageConfig.builder()
                    .url(data.getUser_photo())
                    .placeholder(R.mipmap.def_avatar_round)
                    .errorPic(R.mipmap.def_avatar_round)
                    .transformation(new CircleWithBorderTransformation(mContex))
                    .imageView(holder.avatar)
                    .build());
            imageLoader.loadImage(mContex, GlideImageConfig.builder()
                    .url(data.getWorks_image())
                    .placeholder(R.mipmap.def_square_large)
                    .errorPic(R.mipmap.def_square_large)
                    .imageView(holder.song_img)
                    .build());

        } else {// 专辑

            holder.box.setVisibility(View.GONE);

            holder.singerName.setText(TextUtils.isEmpty(data.getUser_nickname()) ? "" : data.getUser_nickname());
            holder.songName.setText(TextUtils.isEmpty(data.getAlbum_name()) ? "" : data.getAlbum_name());
            holder.comment.setText(String.format(mContex.getString(R.string.format_commentcount), String.valueOf(data.getCommentNum())));
            holder.listen.setText(String.format(mContex.getString(R.string.format_commentcount), String.valueOf(data.getListen_count())));
            holder.time.setText(data.getAdd_time());
            imageLoader.loadImage(mContex, GlideImageConfig.builder()
                    .url(data.getUser_photo())
                    .placeholder(R.mipmap.def_avatar_round)
                    .errorPic(R.mipmap.def_avatar_round)
                    .transformation(new CircleWithBorderTransformation(mContex))
                    .imageView(holder.avatar)
                    .build());
            imageLoader.loadImage(mContex, GlideImageConfig.builder()
                    .url(data.getAlbum_image())
                    .placeholder(R.mipmap.def_square_large)
                    .errorPic(R.mipmap.def_square_large)
                    .imageView(holder.song_img)
                    .build());
        }

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    static class SwipCollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView singerName;
        private final TextView comment;
        private final TextView songName;
        private final TextView listen;
        private final TextView time;
        private final ImageView avatar;
        private final ImageView song_img;
        private final CheckBox box;
        SwipCollectionListAdapter.OnItemClickListener mOnItemClickListener;

        public SwipCollectionHolder(View itemView) {
            super(itemView);
            singerName = itemView.findViewById(R.id.item_singer_name);
            songName = itemView.findViewById(R.id.item_song_name);
            time = itemView.findViewById(R.id.item_singer_addtime);
            comment = itemView.findViewById(R.id.item_song_listen_count);
            listen = itemView.findViewById(R.id.item_song_comment_count);
            avatar = itemView.findViewById(R.id.item_singer_avater);
            song_img = itemView.findViewById(R.id.item_songs_img);
            box = itemView.findViewById(R.id.checkbox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition(), v);
            }

        }
    }

}
