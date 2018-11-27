package com.clicktech.snsktv.module_discover.contract;

import com.clicktech.snsktv.arms.mvp.BaseView;
import com.clicktech.snsktv.arms.mvp.IModel;
import com.clicktech.snsktv.entity.ListOfWorksResponse;
import com.clicktech.snsktv.module_discover.ui.adapter.MoreSongsAdapter;

import java.util.Map;

import rx.Observable;


public interface MoreSongsContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        //设置Adapter
        void setRecyclerView(MoreSongsAdapter adapter);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        //获取演唱歌曲的其它歌手
        Observable<ListOfWorksResponse> getMoreSingerOfTheSong(Map<String, String> info);
    }
}
