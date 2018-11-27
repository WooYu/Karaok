package com.clicktech.snsktv.module_home.ui.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.entity.SoundEffectEntity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/14.
 * 变音的Holder
 */

public class SoundEffectHolder extends BaseHolder<SoundEffectEntity> {

    @BindView(R.id.checkbox)
    CheckBox checkBox;
    @BindView(R.id.textView)
    TextView textView;

    public SoundEffectHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SoundEffectEntity data, int position) {
        textView.setText(data.getEffectname());
        checkBox.setBackgroundResource(data.getResid());
        checkBox.setChecked(data.isSelected());
    }
}
