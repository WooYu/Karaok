package com.clicktech.snsktv.module_home.contract;

import com.clicktech.snsktv.arms.mvp.BaseView;
import com.clicktech.snsktv.arms.mvp.IModel;
import com.clicktech.snsktv.entity.PublishInforEntity;
import com.clicktech.snsktv.entity.SignCosResponse;
import com.clicktech.snsktv.entity.SoundEffectEntity;
import com.clicktech.snsktv.entity.SoundReverbEntity;
import com.clicktech.snsktv.module_home.ui.adapter.SoundEffectAdapter;

import java.util.List;

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
 * Created by Administrator on 2017-05-25.
 */

public interface ToBeAnnoWithMVContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        //设置混响
        void setReverbList(List<SoundReverbEntity> reverbEntityList);

        //设置变音的Adapter
        void setInflexionAdapter(SoundEffectAdapter adapter);

        //设置播放进度条最大值
        void setMaxValueOfPlayProgress(int max);

        //更新播放进度
        void updatePlayProgress(float progress);

        //跳转到发布作品界面
        void turn2Announce(PublishInforEntity entity);

        //跳转到登录界面
        void turn2Login();

        //跳转到主activity
        void turn2MainActivity();

        //展示请求网络对话框
        void showRequestDialog();

        //隐藏请求网络对话框
        void hideRequestDialog();

        void showNumberDialog();

        void hideNumberDialog();

        void updateNumberDialog(int progress);

        //重新录制
        void resetRecord();

    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        //获取混响数据
        List<SoundReverbEntity> getReverbData();

        //获取变音数据
        List<SoundEffectEntity> getInflexionData();

        //获取Cos上传Sign
        Observable<SignCosResponse> getSignCOSParams(String singId);

    }
}