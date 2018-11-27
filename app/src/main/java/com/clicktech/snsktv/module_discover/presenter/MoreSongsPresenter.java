package com.clicktech.snsktv.module_discover.presenter;

import android.app.Application;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.ListOfWorksResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.MoreSongsContract;
import com.clicktech.snsktv.module_discover.ui.adapter.MoreSongsAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import java.util.List;

import javax.inject.Inject;


@ActivityScope
public class MoreSongsPresenter extends BasePresenter<MoreSongsContract.Model, MoreSongsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SongInfoBean> mSongList;
    private MoreSongsAdapter mSongAdapter;

    @Inject
    public MoreSongsPresenter(MoreSongsContract.Model model, MoreSongsContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

//        mSongList = new ArrayList<>();
//        mSongAdapter = new MoreSongsAdapter(mSongList);
//        mSongAdapter.setOnItemClickListener(new DefaultAdapter.
//                OnRecyclerViewItemClickListener<SongInfoBean>() {
//            @Override
//            public void onItemClick(View view, SongInfoBean data, int position) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("songinfo",data);
//                bundle.putBoolean("needmv", MediaFile.isVideoFileType(data.getWorks_url()));
//
//                Intent intent = new Intent(mApplication, SelectRoleActivity.class);
//                intent.putExtras(bundle);
//                mRootView.launchActivity(intent);
//            }
//        });
//        mRootView.setRecyclerView(mSongAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    //获取更多歌手的演唱
    public void getMoreSongs(String songid) {
        int mCurPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        int mPageSize = mApplication.getResources().getInteger(R.integer.paging_size);
        int type = mApplication.getResources().getInteger(R.integer.ranktype_popular);

        mModel.getMoreSingerOfTheSong(RequestParams_Maker.getLearnSingSongWorksList(songid,
                mCurPage, mPageSize, type))
                .compose(RxUtils.<ListOfWorksResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ListOfWorksResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ListOfWorksResponse response) {
                        mSongList = response.getSongWorksList();
                        mSongAdapter.setmInfos(mSongList);
                    }
                });
    }
}
