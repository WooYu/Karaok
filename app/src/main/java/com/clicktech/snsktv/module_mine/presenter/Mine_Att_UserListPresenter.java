package com.clicktech.snsktv.module_mine.presenter;

import android.app.Application;
import android.os.Message;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.Att_Fans_UserEntity;
import com.clicktech.snsktv.entity.Att_Fans_UserListResponse;
import com.clicktech.snsktv.entity.ResponseAttentonCollectResponse;
import com.clicktech.snsktv.module_mine.contract.Mine_Att_UserListContract;
import com.clicktech.snsktv.module_mine.ui.adapter.Mine_AttentionUserListAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import org.simple.eventbus.EventBus;

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
 * Created by Administrator on 2017/3/21.
 */

@ActivityScope
public class Mine_Att_UserListPresenter extends BasePresenter<Mine_Att_UserListContract.Model, Mine_Att_UserListContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<Att_Fans_UserEntity> mSingers = new ArrayList<>();
    private Mine_AttentionUserListAdapter mAdapter;

    @Inject
    public Mine_Att_UserListPresenter(Mine_Att_UserListContract.Model model, final Mine_Att_UserListContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mAdapter = null;
        this.mSingers = null;
    }

    public void initAdapter(String userid, boolean isOther) {
        mAdapter = new Mine_AttentionUserListAdapter(mSingers, isOther);
        mRootView.setRecyclerView(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<Att_Fans_UserEntity>() {
            @Override
            public void onItemClick(View view, Att_Fans_UserEntity data, int position) {
                switch (view.getId()) {
                    case R.id.item_singer_attention:
                        mRootView.showAttentionDialog(data.getId());
                        break;
                }
            }
        });

        requestAttentList(userid);
    }

    //请求关注列表
    private void requestAttentList(String userid) {
        int curPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        int pagesize = mApplication.getResources().getInteger(R.integer.paging_size);

        String loginUserid = ((KtvApplication) mApplication).getUserID();
        mModel.getAttentList(curPage, pagesize, loginUserid, userid)
                .compose(RxUtils.<Att_Fans_UserListResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<Att_Fans_UserListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(Att_Fans_UserListResponse response) {
                        mSingers = response.getAttentions();
                        mAdapter.setmInfos(mSingers);
                    }
                });
    }

    //请求取消关注
    public void requestData_CancelAttent(final String attentid) {
        String loginUserID = KtvApplication.ktvApplication.getUserID();
        mModel.getCancelAttent(loginUserID, attentid)
                .compose(RxUtils.<ResponseAttentonCollectResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseAttentonCollectResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseAttentonCollectResponse singInfo) {
                        EventBus.getDefault().post(new Message(), EventBusTag.ANNOUNCESUCCESS);
                        for (int i = 0; i < mSingers.size(); i++) {
                            if (mSingers.get(i).getId().equals(attentid)) {
                                mSingers.remove(i);
                                break;
                            }
                        }
                        mAdapter.setmInfos(mSingers);
                        mRootView.cancleAttentSuccess();
                    }

                });
    }

    //搜索用户
    public void searchUser(String key) {
        if (EmptyUtils.isEmpty(mSingers)) {
            return;
        }

        ArrayList<Att_Fans_UserEntity> searchList = new ArrayList<>();
        for (Att_Fans_UserEntity attentBean : mSingers) {
            String nickname = attentBean.getUser_nickname();
            if (nickname.contains(key)) {
                searchList.add(attentBean);
            }
        }

        mAdapter.setmInfos(searchList);
    }

    //清除搜索
    public void searchUser_clearKey() {
        mAdapter.setmInfos(mSingers);
    }
}
