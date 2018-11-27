package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.SystemMsgResponse;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/22.
 */

public class SystemMsgAdapter extends DefaultAdapter<SystemMsgResponse.MessageListBean> {


    public SystemMsgAdapter(List<SystemMsgResponse.MessageListBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SystemMsgResponse.MessageListBean> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_systemmsg;
    }


    class MHolder extends BaseHolder<SystemMsgResponse.MessageListBean> {

        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.msg_detail)
        TextView msgDetail;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(SystemMsgResponse.MessageListBean data, int position) {

            if (data.getUpdate_time() != null) {
                time.setText(data.getUpdate_time());
            } else {
                time.setVisibility(View.INVISIBLE);
            }

            if (data.getMessage_content() != null) {
                msgDetail.setText(data.getMessage_content());
            } else {
                msgDetail.setVisibility(View.INVISIBLE);
            }


        }
    }

}
