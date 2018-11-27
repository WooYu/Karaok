package com.clicktech.snsktv.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.module_home.ui.holder.EmptyHolder;

import java.util.List;

public abstract class WEAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> {
    private final int Type_Empty = 1;
    private final int Type_Item = 2;

    protected List<T> mInfos;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private BaseHolder<T> mHolder;

    public WEAdapter(List<T> infos) {
        super();
        this.mInfos = infos;
    }

    @Override
    public int getItemViewType(int position) {
        if (null == mInfos || mInfos.isEmpty()) {
            return Type_Empty;
        } else {
            return Type_Item;
        }
    }

    /**
     * 创建Hodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseHolder<T> onCreateViewHolder(ViewGroup parent, final int viewType) {

        if (viewType == Type_Empty) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(getEmptyLayoutId(), parent, false);
            mHolder = (BaseHolder<T>) new EmptyHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
            mHolder = getHolder(view);
        }
        mHolder.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
            @Override
            public void onViewClick(View view, int position) {
                if (mOnItemClickListener != null) {
                    if (viewType == Type_Empty)
                        mOnItemClickListener.onEmptyClick();
                    else
                        mOnItemClickListener.onItemClick(view, mInfos.get(position), position);
                }
            }
        });
        return mHolder;
    }


    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseHolder<T> holder, int position) {
        if (getItemViewType(position) == Type_Item)
            holder.setData(mInfos.get(position), position);
    }

    /**
     * 数据的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (null == mInfos || mInfos.isEmpty()) {
            return 1;
        }
        return mInfos.size();
    }


    public List<T> getInfos() {
        return mInfos;
    }

    /**
     * 获得item的数据
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return mInfos == null ? null : mInfos.get(position);
    }

    public abstract int getEmptyLayoutId();

    /**
     * 子类实现提供holder
     *
     * @param v
     * @return
     */
    public abstract BaseHolder<T> getHolder(View v);

    /**
     * 提供Item的布局
     *
     * @return
     */
    public abstract int getLayoutId();

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener<T> {
        void onItemClick(View view, T data, int position);

        void onEmptyClick();
    }
}
