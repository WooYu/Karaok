package com.clicktech.snsktv.module_mine.contract;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.mvp.BaseView;
import com.clicktech.snsktv.arms.mvp.IModel;
import com.clicktech.snsktv.entity.GiftListResponse;
import com.clicktech.snsktv.entity.PopularListResponse;
import com.clicktech.snsktv.entity.SongInfoBean;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by Administrator on 2017/3/20.
 */

public interface Mine_PopularHotContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        void requestCompletion();

        void updateData(List<SongInfoBean> list);

        //评论成功，刷新列表
        void doCommentSuccess();
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        //获取热门数据
        Observable<PopularListResponse> getPopularList(Map<String, String> info);

        //评论用户作品
        Observable<BaseResponse> userReviews(Map<String, String> info);

        //专辑-送礼物
        Observable<BaseResponse> sendingAlbumGifts(Map<String, String> info);

        //专辑-送花
        Observable<BaseResponse> sendingAlbumFlowers(Map<String, String> info);

        //单曲-送礼物
        Observable<BaseResponse> sendingSingleGifts(Map<String, String> info);

        //单曲-送花
        Observable<BaseResponse> sendingSingleFlowers(Map<String, String> info);

        //礼物列表
        Observable<GiftListResponse> getGiftList(Map<String, String> info);
    }
}