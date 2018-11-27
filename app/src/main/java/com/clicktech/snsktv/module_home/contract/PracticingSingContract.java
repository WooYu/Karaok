package com.clicktech.snsktv.module_home.contract;


import com.clicktech.snsktv.arms.mvp.BaseView;
import com.clicktech.snsktv.arms.mvp.IModel;
import com.clicktech.snsktv.entity.SongInfoWithSongIDResponse;
import com.library.multimedia.lyricscontrols.parser.LrcRow;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import rx.Observable;

public interface PracticingSingContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        //获取权限
        RxPermissions getRxPermissions();
        //更新歌词
        void updateLrc(List<LrcRow> lrcRowList);

        //展示对话框
        void showNumberDialog();

        //隐藏对话框
        void dismissNumberDialog();

        //更新下载进度
        void updateNumberDialog(float progress);

        //调整歌词、时间进度
        void updatePlayProgress(float progress, float totalTime);

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        //获取歌曲信息
        Observable<SongInfoWithSongIDResponse> getKSingInfo(String userid, String songid, int is_practice);

        //解析歌词
        List<LrcRow> parseLrc(String lrcpath);
    }
}
