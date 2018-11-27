package com.clicktech.snsktv.module_mine.model;

import android.app.Application;

import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.mvp.BaseModel;
import com.clicktech.snsktv.common.cache.CacheManager;
import com.clicktech.snsktv.common.net.ServiceManager;
import com.clicktech.snsktv.entity.CommentEntity;
import com.clicktech.snsktv.entity.CommentListResponse;
import com.clicktech.snsktv.module_mine.contract.MessageContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
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
 * Created by Administrator on 2016/12/29.
 */

public class MessageModel extends BaseModel<ServiceManager, CacheManager> implements MessageContract.Model {
    private Gson mGson;
    private Application mApplication;

    public MessageModel(ServiceManager serviceManager, CacheManager cacheManager, Gson gson, Application application) {
        super(serviceManager, cacheManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestory() {
        super.onDestory();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public List<CommentEntity> getConcernComments() {
        List<CommentEntity> commentList = new ArrayList<CommentEntity>();
        for (int i = 0; i < 15; i++) {
            commentList.add(new CommentEntity());
        }
        return commentList;
    }

    @Override
    public List<CommentEntity> getIndifferentComments() {
        return new ArrayList<CommentEntity>();
    }

    @Override
    public Observable<BaseResponse> comment(Map<String, String> info) {
        Observable<BaseResponse> reviews = mServiceManager.getCommonService()
                .getUserReviews(info);
        return reviews;
    }


    @Override
    public Observable<CommentListResponse> getWorksCommentByConcernedList(Map<String, String> info) {

        Observable<CommentListResponse> singSinggerList = mServiceManager.getCommonService()
                .getWorksCommentByConcernedList(info);
        return singSinggerList;
    }

    @Override
    public Observable<CommentListResponse> getWorksCommentByUnConcernedList(Map<String, String> info) {

        Observable<CommentListResponse> singSinggerList = mServiceManager.getCommonService()
                .getWorksCommentByUnConcernedList(info);
        return singSinggerList;
    }

}