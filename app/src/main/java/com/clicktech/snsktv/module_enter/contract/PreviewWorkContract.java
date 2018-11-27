package com.clicktech.snsktv.module_enter.contract;


import com.clicktech.snsktv.arms.mvp.BaseView;
import com.clicktech.snsktv.arms.mvp.IModel;
import com.clicktech.snsktv.entity.HomeShowResponse;
import com.clicktech.snsktv.entity.SongInfoBean;

import java.util.List;

import rx.Observable;

public interface PreviewWorkContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        //设置播放列表
        void setPlayList(List<SongInfoBean> list);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        //获取首页歌曲信息
        Observable<HomeShowResponse> getHomeShowList(String userid);
    }
}
