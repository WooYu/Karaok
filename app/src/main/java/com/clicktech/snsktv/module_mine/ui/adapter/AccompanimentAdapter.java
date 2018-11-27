package com.clicktech.snsktv.module_mine.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SoundHistoryEntity;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/5/26.
 */

public class AccompanimentAdapter extends SwipeMenuAdapter<AccompanimentAdapter.SwipCollectionHolder> {

    private List<SoundHistoryEntity> dataList = new ArrayList<>();
    private Context mContext;
    private SwipLocalRecordingAdapter.OnItemClickListener mOnItemClickListener;

    public AccompanimentAdapter(List<SoundHistoryEntity> list, Context context) {
        this.dataList = list;
        mContext = context;
    }

    public void setOnItemClickListener(SwipLocalRecordingAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activity_mine_mysound_historylist, parent, false);
    }

    @Override
    public SwipCollectionHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        SwipCollectionHolder mHolder = new SwipCollectionHolder(realContentView);
        mHolder.mOnItemClickListener = mOnItemClickListener;
        return mHolder;
    }

    @Override
    public void onBindViewHolder(SwipCollectionHolder holder, final int position) {
        SwipCollectionHolder mholder = holder;
        SoundHistoryEntity data = dataList.get(position);

        KtvApplication.ktvApplication.getAppComponent().imageLoader()
                .loadImage(mContext, GlideImageConfig.builder()
                        .url(data.getSong_image())
                        .placeholder(R.mipmap.def_avatar_square)
                        .errorPic(R.mipmap.def_avatar_square)
                        .imageView(mholder.item_mine_mychrous_songpic)
                        .build());

        mholder.item_mine_mychrous_songname.setText(TextUtils.isEmpty(data.getSong_name()) ? "" : data.getSong_name());
        mholder.item_mine_mychrous_singername.setText(TextUtils.isEmpty(data.getUser_nickname()) ? "" : data.getUser_nickname());

        mholder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position, v);
            }
        });
        mholder.item_mine_mychroussongs_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position, v);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    static class SwipCollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SwipLocalRecordingAdapter.OnItemClickListener mOnItemClickListener;
        MLImageView item_mine_mychrous_songpic;
        TextView item_mine_mychrous_songname;
        TextView item_mine_mychrous_singername;
        TextView item_mine_mychroussongs_time;
        LinearLayout ll;

        public SwipCollectionHolder(View itemView) {
            super(itemView);
            item_mine_mychrous_songname = itemView.findViewById(R.id.item_mine_mychrous_songname);
            item_mine_mychrous_singername = itemView.findViewById(R.id.item_mine_mychrous_singername);
            item_mine_mychroussongs_time = itemView.findViewById(R.id.item_mine_mychroussongs_time);
            item_mine_mychrous_songpic = itemView.findViewById(R.id.item_mine_mychrous_songpic);
            ll = itemView.findViewById(R.id.ll);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition(), v);
            }
        }
    }

}
