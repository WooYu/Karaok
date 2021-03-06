package com.clicktech.snsktv.module_home.contract;

import android.util.SparseArray;

import com.clicktech.snsktv.arms.mvp.BaseView;
import com.clicktech.snsktv.arms.mvp.IModel;
import com.clicktech.snsktv.entity.AverageScoreResponse;
import com.clicktech.snsktv.entity.SongInfoWithSongIDResponse;
import com.library.multimedia.lyricscontrols.parser.LrcRow;
import com.library.multimedia.soundercontrols.GradeBean;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Administrator on 2017/3/9.
 */

public interface KSingContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        //获取权限
        RxPermissions getRxPermissions();

        //开始倒计时
        void startCountDown();

        //更新mid音量图片
        void updatePictureOfMidVoice();

        //更新歌词
        void updateLrc(List<LrcRow> lrcRowList);

        //调整歌词、时间进度
        void updatePlayProgress(float progress, float totalTime);

        //展示打分结果
        void showScoreResult();

        //跳转到发布界面
        void turn2Publish();

        //更新下载进度
        void updateNumberDialog(float progress);

        //展示对话框
        void showNumberDialog();

        //隐藏对话框
        void dismissNumberDialog();

        //初始化整首歌的mid
        void initIntonationIs(SparseArray<List<GradeBean>> hashMap);

        //更新打分
        void updateIntonationIs(GradeBean gradeBean, float positionSecX);

        //准备获取mid数据的操作
        void prepareDataOfGrade();

        //跳转到下一句歌词的mid
        void turn2NextLyric();

    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        //解析歌词
        List<LrcRow> parseLrc(String lrcpath);

        //获取歌曲信息
        Observable<SongInfoWithSongIDResponse> getKSingInfo(String userid, String songid, int is_practice);

        //下载歌曲或者歌词
        Observable<ResponseBody> downFileWithDynamicUrlSync(String url);

        //查询歌曲所有作品平均得分接口
        Observable<AverageScoreResponse> getSongWorksAverageScore(String userid, String songid);

    }
}