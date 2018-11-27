package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.entity.ChorusTypeResponse;
import com.clicktech.snsktv.entity.SingSingnerTypeBeanEntity;
import com.clicktech.snsktv.entity.SingSingnerTypeResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_home.contract.ChorusTypeContract;
import com.clicktech.snsktv.module_home.ui.activity.ChorusWorksActivity;
import com.clicktech.snsktv.module_home.ui.adapter.ChorusTypeAdapter;
import com.clicktech.snsktv.module_home.ui.adapter.PopularChorusAdapter;
import com.clicktech.snsktv.module_home.ui.listener.JoinInChorusListener;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

@ActivityScope
public class ChorusTypePresenter extends BasePresenter<ChorusTypeContract.Model, ChorusTypeContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SingSingnerTypeBeanEntity> mCategoryList = new ArrayList<>();
    private List<SongInfoBean> popularWorks = new ArrayList<SongInfoBean>();
    private ChorusTypeAdapter categoryAdapter;
    private PopularChorusAdapter popularChorusAdapter;

    @Inject
    public ChorusTypePresenter(ChorusTypeContract.Model model, ChorusTypeContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
        categoryAdapter = new ChorusTypeAdapter(mCategoryList);
        popularChorusAdapter = new PopularChorusAdapter(popularWorks, new JoinInChorusListener() {
            @Override
            public void joininchorus(int position) {
                mRootView.turn2SelectRole(popularWorks.get(position));
            }
        });
        mRootView.setRecyclerView(popularChorusAdapter, categoryAdapter);

        popularChorusAdapter.setOnItemClickListener(new DefaultAdapter
                .OnRecyclerViewItemClickListener<SongInfoBean>() {
            @Override
            public void onItemClick(View view, SongInfoBean data, int position) {
                Intent intent = new Intent(mApplication, SongDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("songinfo", data);
                intent.putExtras(bundle);
                UiUtils.startActivity(intent);

            }
        });

        categoryAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<SingSingnerTypeBeanEntity>() {
            @Override
            public void onItemClick(View view, SingSingnerTypeBeanEntity data, int position) {
                Intent intent = new Intent(mApplication, ChorusWorksActivity.class);
                intent.putExtra("singercategoryid", data.getCategory_Type());
                intent.putExtra("categoryname", data.getCategory_name());
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

    public void requestData(Map<String, String> info) {
        mModel.getSingerList(info)
                .compose(RxUtils.<SingSingnerTypeResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SingSingnerTypeResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SingSingnerTypeResponse singInfo) {
                        mCategoryList = singInfo.getSingerCategoryList();
                        categoryAdapter.setmInfos(mCategoryList);
                    }
                });
    }

    public void requestChorusList(Map<String, String> info) {
        mModel.getChorusTypeList(info)
                .compose(RxUtils.<ChorusTypeResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ChorusTypeResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ChorusTypeResponse chorusTypeEntity) {
                        popularWorks = chorusTypeEntity.getList();
                        popularChorusAdapter.setmInfos(popularWorks);
                    }
                });
    }

}