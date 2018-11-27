package com.clicktech.snsktv.module_home.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.ShareBean;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wy201 on 2018-03-09.
 */

public class ShareWorkAdapter extends DefaultAdapter<ShareBean> {

    public ShareWorkAdapter(List<ShareBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<ShareBean> getHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_share;
    }

    class ViewHolder extends BaseHolder<ShareBean> {
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(ShareBean data, int position) {
            ivPhoto.setImageResource(data.getPlatformImg());
            tvName.setText(data.getPlatformName());
        }
    }
}
