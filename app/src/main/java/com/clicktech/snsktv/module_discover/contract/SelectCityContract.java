package com.clicktech.snsktv.module_discover.contract;


import com.clicktech.snsktv.arms.mvp.BaseView;
import com.clicktech.snsktv.arms.mvp.IModel;
import com.clicktech.snsktv.entity.CityEntity;
import com.clicktech.snsktv.module_discover.ui.adapter.SelectCityAdapter;

import java.util.List;

public interface SelectCityContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        void setRecyclerView(SelectCityAdapter adapter);

        //返回城市
        void returnSelectCit(String city);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        //获取日本区域的城市
        List<CityEntity> getJapanCities();

        //获取非日本区域的城市
        List<CityEntity> getUnJapanCities();
    }
}
