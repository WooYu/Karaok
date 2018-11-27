package com.clicktech.snsktv.module_home.ui.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.entity.SingSingnerTypeBeanEntity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/13.
 */

public class SingerCategoryHolder extends BaseHolder<SingSingnerTypeBeanEntity> {

    @BindView(R.id.item_tv_singer_name)
    TextView item_tv_singer_name;

    public SingerCategoryHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SingSingnerTypeBeanEntity data, int position) {

        item_tv_singer_name.setText(TextUtils.isEmpty(data.getCategory_name()) ? "" : data.getCategory_name());


//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//
//            }
//        });


    }

}
