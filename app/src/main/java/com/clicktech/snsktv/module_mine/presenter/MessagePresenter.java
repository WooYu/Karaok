package com.clicktech.snsktv.module_mine.presenter;

import android.app.Application;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.CommentEntity;
import com.clicktech.snsktv.entity.CommentListResponse;
import com.clicktech.snsktv.module_mine.contract.MessageContract;
import com.clicktech.snsktv.module_mine.ui.adapter.CommentAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


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

@ActivityScope
public class MessagePresenter extends BasePresenter<MessageContract.Model, MessageContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private CommentAdapter mCommentAdapter;
    private List<CommentEntity> mComments = new ArrayList<CommentEntity>();
    private int mCurIndex;
    private int mPageSize;
    private String mUserID;

    @Inject
    public MessagePresenter(MessageContract.Model model, MessageContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mUserID = ((KtvApplication) mApplication).getUserID();
        mCurIndex = mApplication.getResources().getInteger(R.integer.paging_startindex);
        mPageSize = mApplication.getResources().getInteger(R.integer.paging_size);

        mCommentAdapter = new CommentAdapter(mComments);
        mRootView.setCommentList(mCommentAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

        this.mComments = null;
    }

    // 切换评论列表
    public void switchCommentsList(int checkid) {
        if (null != mCommentAdapter) {
            mComments = new ArrayList<>();
            mCommentAdapter.setmInfos(mComments);
        }

        switch (checkid) {
            case 0:
                switchCommentsList_att();
                break;
            case 1:
                switchCommentsList_noatt();
                break;
        }
    }

    //已关注人评论
    public void switchCommentsList_att() {
        mModel.getWorksCommentByConcernedList(RequestParams_Maker.getCommentsOfInterested(mUserID, mCurIndex, mPageSize))
                .compose(RxUtils.<CommentListResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommentListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(CommentListResponse response) {
                        mComments = response.getList();
                        mCommentAdapter.setmInfos(mComments);
                    }
                });
    }

    //未关注人评论
    public void switchCommentsList_noatt() {
        mModel.getWorksCommentByUnConcernedList(RequestParams_Maker.getCommentsOfInterested(mUserID, mCurIndex, mPageSize))
                .compose(RxUtils.<CommentListResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommentListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(CommentListResponse response) {
                        mComments = response.getList();
                        mCommentAdapter.setmInfos(mComments);
                    }
                });
    }

    //回复
    public void reply(CommentEntity bean, String comment) {
        mModel.comment(RequestParams_Maker.getUserReviews(mUserID, bean.getWorks_id(),
                bean.getId(), comment, bean.getComment_uid()))
                .compose(RxUtils.<BaseResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse singInfo) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_comment));
                    }

                });
    }


}