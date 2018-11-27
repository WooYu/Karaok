package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.CityEntity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/25.
 */

public class SelectCityAdapter extends DefaultAdapter<CityEntity> {
    public SelectCityAdapter(List<CityEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<CityEntity> getHolder(View v) {
        return new SelectCityHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_select_city;
    }

    class SelectCityHolder extends BaseHolder<CityEntity> {
        @BindView(R.id.textview)
        TextView textView;

        private SelectCityHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(CityEntity data, int position) {
            textView.setText(data.getName());
        }
    }
}
