package com.clicktech.snsktv.module_home.ui.holder;


import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.entity.SingSingnerTypeBeanEntity;

import butterknife.BindView;

public class ChorusTypeHolder extends BaseHolder<SingSingnerTypeBeanEntity> {

    @BindView(R.id.item_tv_singer_name)
    TextView item_tv_singer_name;

    public ChorusTypeHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SingSingnerTypeBeanEntity data, int position) {
        item_tv_singer_name.setText(data.getCategory_name());

    }
}
