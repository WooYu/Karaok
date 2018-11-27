package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ChooseProinceAdapter extends DefaultAdapter<String> {
    public ChooseProinceAdapter(List<String> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<String> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_chooseproince;
    }

    class MHolder extends BaseHolder<String> {
        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(String data, int position) {

        }
    }
}
