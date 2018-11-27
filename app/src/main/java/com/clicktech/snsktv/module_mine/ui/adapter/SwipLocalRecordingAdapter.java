package com.clicktech.snsktv.module_mine.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.ConstUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.transformations.RoundedCornersTransformation;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/26.
 */

public class SwipLocalRecordingAdapter extends SwipeMenuAdapter<SwipLocalRecordingAdapter.MHolder> {

    private List<SongInfoBean> dataList;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public SwipLocalRecordingAdapter(List<SongInfoBean> list, Context context) {
        this.dataList = list;
        mContext = context;
    }

    public void setData(List<SongInfoBean> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_localrecording, parent, false);
    }

    @Override
    public MHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        MHolder mHolder = new MHolder(realContentView);
        mHolder.mOnItemClickListener = mOnItemClickListener;
        return mHolder;
    }

    @Override
    public void onBindViewHolder(MHolder holder, int position) {
        MHolder mHolder = holder;
        SongInfoBean data = dataList.get(position);

        KtvApplication.ktvApplication.getAppComponent().imageLoader().loadImage(mContext, GlideImageConfig
                .builder()
                .url(data.getWorks_image())
                .transformation(new RoundedCornersTransformation(mContext, 5))
                .errorPic(R.mipmap.def_square_round)
                .placeholder(R.mipmap.def_square_round)
                .imageView(mHolder.avatar)
                .build());
        mHolder.name.setText(data.getWorks_name());
        mHolder.comment.setText(StringHelper.getWorkTimeBySecond(mContext, data.getWorks_second()));
        mHolder.size.setText(StringHelper.getFileSize(mContext, data.getWorks_size(),
                ConstUtils.MemoryUnit.KB));
        mHolder.time.setText(data.getAdd_time());
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    static class MHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnItemClickListener mOnItemClickListener;
        ImageView avatar;
        TextView name;
        TextView comment;
        TextView size;
        TextView time;
        TextView release;
        LinearLayout rootView;

        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            avatar = itemView.findViewById(R.id.chours_avatar);
            name = itemView.findViewById(R.id.tv_music_name);
            comment = itemView.findViewById(R.id.tv_comment);
            size = itemView.findViewById(R.id.tv_size);
            time = itemView.findViewById(R.id.tv_time);
            release = itemView.findViewById(R.id.tv_release);
            rootView = itemView.findViewById(R.id.ll_item);

        }

        @OnClick({R.id.ll_item, R.id.tv_release})
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition(), v);
            }
        }
    }

}
