package com.clicktech.snsktv.widget.dialog;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.common.EventBusTag;
import com.flyco.dialog.widget.base.BottomBaseDialog;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwitchModeDialog extends BottomBaseDialog<SwitchModeDialog> {

    @BindView(R.id.tv_colose)
    TextView tvColose;

    public SwitchModeDialog(Context context, View animateView) {
        super(context, animateView);
    }

    public SwitchModeDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        View inflate = View.inflate(mContext, R.layout.dialog_switchmode, null);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        tvColose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @OnClick({R.id.ll_practice, R.id.ll_mv, R.id.ll_ksong, R.id.ll_chorus, R.id.ll_videochorus, R.id.tv_colose})
    public void onViewClicked(View view) {
        Message message = new Message();

        switch (view.getId()) {
            case R.id.ll_practice:
                message.what = 0;
                break;
            case R.id.ll_mv:
                message.what = 1;
                break;
            case R.id.ll_ksong:
                message.what = 2;
                break;
            case R.id.ll_chorus:
                message.what = 3;
                break;
            case R.id.ll_videochorus:
                message.what = 4;
                break;
        }
        dismiss();
        EventBus.getDefault().post(message, EventBusTag.SWITCHMODE);
    }

}
