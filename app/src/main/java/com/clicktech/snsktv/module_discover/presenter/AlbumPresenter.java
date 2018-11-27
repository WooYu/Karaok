package com.clicktech.snsktv.module_discover.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.entity.AlbumBean;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.AlbumContract;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_discover.ui.adapter.AlbumListAdapter;
import com.clicktech.snsktv.module_discover.ui.adapter.SingleListAdapter;
import com.clicktech.snsktv.module_mine.ui.activity.AlbumDetailActivity;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;

import java.util.ArrayList;

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
 * Created by Administrator on 2017/5/17.
 */

@ActivityScope
public class AlbumPresenter extends BasePresenter<AlbumContract.Model, AlbumContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private ArrayList<AlbumBean> mAlbumList = new ArrayList<>();
    private ArrayList<SongInfoBean> mSongList = new ArrayList<>();
    private AlbumListAdapter mAlbumAdapter;
    private SingleListAdapter mSingleAdapter;

    @Inject
    public AlbumPresenter(AlbumContract.Model model, AlbumContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mAlbumAdapter = new AlbumListAdapter(mAlbumList);
        mSingleAdapter = new SingleListAdapter(mSongList);
        mRootView.setAlbumListRecyclerView(mAlbumAdapter);
        mRootView.setSingleRecyclerView(mSingleAdapter);
        mSingleAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<SongInfoBean>() {
            @Override
            public void onItemClick(View view, SongInfoBean data, int position) {
                Intent intent = new Intent(mApplication, SongDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("songinfo", data);
                intent.putExtras(bundle);
                UiUtils.startActivity(intent);

            }
        });
        mAlbumAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<AlbumBean>() {
            @Override
            public void onItemClick(View view, AlbumBean data, int position) {
                Intent intent = new Intent(mApplication, AlbumDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid", data.getUser_id());
                bundle.putString("albumid", data.getId());
                intent.putExtras(bundle);
                UiUtils.startActivity(intent);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void setAlbumList(ArrayList<AlbumBean> albumList) {
        mAlbumAdapter.setmInfos(mAlbumList = albumList);
    }

    public ArrayList<SongInfoBean> getWorkList() {
        return mSongList;
    }

    public void setWorkList(ArrayList<SongInfoBean> workList) {
        mSingleAdapter.setmInfos(mSongList = workList);
    }
}
