package com.clicktech.snsktv.module_discover.contract;

import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.mvp.BaseView;
import com.clicktech.snsktv.arms.mvp.IModel;
import com.clicktech.snsktv.entity.ArtworksDetailResponse;
import com.clicktech.snsktv.entity.GiftListResponse;
import com.clicktech.snsktv.entity.ResponseAttentonCollectResponse;

import java.util.Map;

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
 * Created by Administrator on 2017/4/21.
 */

public interface SongDetailsContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        //初始化界面UI
        void initUIInterface(ArtworksDetailResponse bean);

        //更新播放的进度条
        void updatePlayProgress(boolean status, int progress, int totalprogress);

        //歌词下载完成
        void downloadTheLyrics(String lyricpath);

        //更新收藏状态
        void updateStatusOfCollect(boolean status);

        //更新关注状态
        void updateStatusOfAttention(boolean status);

    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        //收听发布的作品
        Observable<ArtworksDetailResponse> listenToReleaseWorks(Map<String, String> info);

        //评论用户作品
        Observable<BaseResponse> addAComment(Map<String, String> info);

        //添加关注
        Observable<ResponseAttentonCollectResponse> addAttention(Map<String, String> info);

        //取消关注
        Observable<ResponseAttentonCollectResponse> delAttention(Map<String, String> info);

        //添加收藏
        Observable<ResponseAttentonCollectResponse> addWorksStore(Map<String, String> info);

        //取消收藏
        Observable<ResponseAttentonCollectResponse> cancelWorksStore(Map<String, String> info);

        //评论用户作品
        Observable<BaseResponse> userReviews(Map<String, String> info);

        //送礼物
        Observable<BaseResponse> givingGift(Map<String, String> info);

        //送花
        Observable<BaseResponse> givingFlowers(Map<String, String> info);

        //礼物列表
        Observable<GiftListResponse> getGiftList(Map<String, String> info);


    }
}