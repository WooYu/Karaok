package com.clicktech.snsktv.module_home.ui.adapter;

import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.entity.ShareBean;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wy201 on 2018-03-09.
 */

public class OtherShareAdapter extends DefaultAdapter<ShareBean> {

    public OtherShareAdapter(List<ShareBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<ShareBean> getHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_share_other;
    }

    class ViewHolder extends BaseHolder<ShareBean> {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.textView)
        TextView textView;
        @BindView(R.id.switchView)
        Switch switchView;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(final ShareBean data, int position) {
            imageView.setImageResource(data.getPlatformImg());
            textView.setText(data.getPlatformName());
            switchView.setChecked(DataHelper.getBooleanSF(context, data.getPlatformName()));
            switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    DataHelper.setBooleanSF(context, data.getPlatformName(), isChecked);
                    EventBus.getDefault().post(new Message(), EventBusTag.SHARE_MODECHANGE);
                }
            });
        }
    }

}
