package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.FunctionIntroEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class FunctionIntroAdapter extends DefaultAdapter<FunctionIntroEntity> {
    public FunctionIntroAdapter(List<FunctionIntroEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<FunctionIntroEntity> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_functionintro;
    }


    class MHolder extends BaseHolder<FunctionIntroEntity> {
        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(FunctionIntroEntity data, int position) {

        }
    }

}
