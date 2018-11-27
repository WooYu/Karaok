package com.clicktech.snsktv.module_mine.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.DeviceUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.PublishInforEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.WorksListResponse;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_home.ui.activity.AnnounceActivity;
import com.clicktech.snsktv.module_mine.contract.LocalRecordingContract;
import com.clicktech.snsktv.module_mine.ui.adapter.SwipLocalRecordingAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.clicktech.snsktv.common.KtvApplication.ktvApplication;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017/5/23.
 */

@ActivityScope
public class LocalRecordingPresenter extends BasePresenter<LocalRecordingContract.Model, LocalRecordingContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SongInfoBean> mSongList = new ArrayList<>();
    private SwipLocalRecordingAdapter swipAdapter;

    @Inject
    public LocalRecordingPresenter(LocalRecordingContract.Model model, LocalRecordingContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        swipAdapter = new SwipLocalRecordingAdapter(mSongList, mApplication);
        OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                requestDeleteItemData(adapterPosition);
                closeable.smoothCloseMenu();

            }
        };

        swipAdapter.setOnItemClickListener(new SwipLocalRecordingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = null;
                switch (v.getId()) {
                    case R.id.tv_release:
                        intent = new Intent(ktvApplication, AnnounceActivity.class);
                        Bundle bundle = new Bundle();
                        PublishInforEntity publishInfo = obtainPublishInfoEntity(position);
                        bundle.putParcelable("publishinfo", publishInfo);
                        intent.putExtras(bundle);
                        UiUtils.startActivity(intent);
                        break;

                    case R.id.ll_item:
                        intent = new Intent(ktvApplication, SongDetailsActivity.class);
                        Bundle detailBundle = new Bundle();
                        detailBundle.putParcelable("songinfo", mSongList.get(position));
                        intent.putExtras(detailBundle);
                        UiUtils.startActivity(intent);
                        break;
                }
            }
        });
        mRootView.setSwipRecyclerView(swipAdapter, mSongList, menuItemClickListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    //请求本地录音
    public void requestData() {
        int mCurPage = ktvApplication.getResources().getInteger(R.integer.paging_startindex);
        int mPageSize = ktvApplication.getResources().getInteger(R.integer.paging_size);
        String userid = ktvApplication.getUserID();
        int isPublish = ktvApplication.getResources().getInteger(R.integer.ispublish_secret);
        mModel.getSingerList(RequestParams_Maker.getWorks(mCurPage, mPageSize, userid, isPublish))
                .compose(RxUtils.<WorksListResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<WorksListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(WorksListResponse singInfo) {
                        mSongList = singInfo.getWorks();
                        swipAdapter.setData(mSongList);
                    }
                });
    }

    //本地录音删除
    public void requestDeleteItemData(final int position) {
        if (EmptyUtils.isEmpty(mSongList)) {
            return;
        }

        String userid = ktvApplication.getUserID();
        String worksid = mSongList.get(position).getId();

        mModel.deleteAccompany(RequestParams_Maker.getDeleteLocalWorks(userid, worksid))
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse singInfo) {
                        swipAdapter.notifyItemRemoved(position);
                        mSongList.remove(position);
                        mRootView.showMessage(mApplication.getString(R.string.tip_delete_success));
                    }
                });
    }

    //获取发布的实体
    private PublishInforEntity obtainPublishInfoEntity(int position) {
        SongInfoBean bean = mSongList.get(position);
        PublishInforEntity publishInforEntity = new PublishInforEntity();
        publishInforEntity.setSong_id(bean.getSong_id());
        publishInforEntity.setUserId(bean.getUser_id());
        publishInforEntity.setAlbum_id(bean.getAlbum_id());
        publishInforEntity.setWorks_id(bean.getId());
        publishInforEntity.setPhone_type(DeviceUtils.getPhoneType());
        publishInforEntity.setWorks_second(bean.getWorks_second());
        publishInforEntity.setWorks_size(bean.getWorks_size());
        publishInforEntity.setWorks_score(bean.getWorks_score());
        publishInforEntity.setWorks_level(bean.getWorks_level());
        publishInforEntity.setWorks_name(bean.getWorks_name());
        publishInforEntity.setWorks_desc(bean.getWorks_desc());
        publishInforEntity.setWorks_type(Integer.parseInt(bean.getWorks_type()));
        publishInforEntity.setWorks_url(bean.getWorks_url());
        publishInforEntity.setWorks_photo(bean.getWorks_image());
        return publishInforEntity;
    }
}
