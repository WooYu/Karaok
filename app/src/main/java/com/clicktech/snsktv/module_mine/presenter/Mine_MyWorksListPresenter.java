package com.clicktech.snsktv.module_mine.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.WorkAlbumBean;
import com.clicktech.snsktv.entity.WorksListResponse;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_mine.contract.Mine_MyWorksListContract;
import com.clicktech.snsktv.module_mine.ui.adapter.MineSingleListAdapter;
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
 * Created by Administrator on 2017/3/21.
 */

@ActivityScope
public class Mine_MyWorksListPresenter extends BasePresenter<Mine_MyWorksListContract.Model, Mine_MyWorksListContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<WorkAlbumBean> mAlbumList = new ArrayList<>();
    private List<SongInfoBean> mSongList = new ArrayList<>();
    private MineSingleListAdapter mSingleAdapter;
    private int mIndexOfAlbum;//当前专辑下标

    @Inject
    public Mine_MyWorksListPresenter(Mine_MyWorksListContract.Model model, Mine_MyWorksListContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        initializeViewPager();
        initializeSongList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mAlbumList = null;
        this.mSongList = null;
        this.mSingleAdapter = null;
    }

    //获取作品
    public void requestWorks(String userid) {
        int mCurPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        int mPageSize = mApplication.getResources().getInteger(R.integer.paging_size);

        mModel.getWorksList(mCurPage, mPageSize, userid,
                mApplication.getResources().getInteger(R.integer.ispublish_open))
                .compose(RxUtils.<WorksListResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<WorksListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(WorksListResponse worksListResponse) {
                        if (null == worksListResponse) {
                            return;
                        }

                        mAlbumList = worksListResponse.getAlbums();
                        mSongList = worksListResponse.getWorks();
                        mSingleAdapter.setmInfos(mSongList);
                        mRootView.updateAlbumView(mAlbumList);
                        mRootView.scrollAlbum(mAlbumList, 0);
                        mRootView.updateSingleView(EmptyUtils.isEmpty(mSongList)
                                ? 0 : mSongList.size());

                    }

                });
    }

    //初始化专辑
    private void initializeViewPager() {

        ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mIndexOfAlbum = position;
                mRootView.scrollAlbum(mAlbumList, mIndexOfAlbum);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };

        mRootView.initAlbumView(mAlbumList, changeListener);

    }

    //初始化单曲
    private void initializeSongList() {
        mSingleAdapter = new MineSingleListAdapter(mSongList);
        mSingleAdapter.setOnItemClickListener(new DefaultAdapter.
                OnRecyclerViewItemClickListener<SongInfoBean>() {
            @Override
            public void onItemClick(View view, SongInfoBean data, int position) {

                Intent intent = new Intent(mApplication, SongDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("songinfo", data);
                intent.putExtras(bundle);
                UiUtils.startActivity(intent);

            }
        });

        mRootView.initSingleView(mSingleAdapter);
    }

}
