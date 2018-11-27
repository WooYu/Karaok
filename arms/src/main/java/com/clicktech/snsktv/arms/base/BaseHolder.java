package com.clicktech.snsktv.arms.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.clicktech.snsktv.arms.utils.KnifeUtil;
import com.zhy.autolayout.utils.AutoUtils;


public abstract class BaseHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected final String TAG = this.getClass().getSimpleName();
    public Context context;
    protected OnViewClickListener mOnViewClickListener = null;

    public BaseHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        itemView.setOnClickListener(this);//点击事件
        AutoUtils.autoSize(itemView);//适配
        KnifeUtil.bindTarget(this, itemView);//绑定
    }

    /**
     * 设置数据
     * 刷新界面
     *
     * @param
     * @param position
     */
    public abstract void setData(T data, int position);

    /**
     * 释放资源
     */
    protected void onRelease() {

    }

    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onViewClick(view, this.getLayoutPosition());
        }
    }

    public void setOnItemClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }

    public interface OnViewClickListener {
        void onViewClick(View view, int position);
    }
}
