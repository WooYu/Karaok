package com.clicktech.snsktv.module_discover.presenter;

import android.app.Application;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.PermissionUtil;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.module_discover.contract.ReportContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import java.util.Map;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017/6/26.
 */

@ActivityScope
public class ReportPresenter extends BasePresenter<ReportContract.Model, ReportContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public ReportPresenter(ReportContract.Model model, ReportContract.View rootView
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
    }

    //  举报
    public void report(Map<String, RequestBody> info, MultipartBody.Part part) {
        mRootView.showLoading();
        mModel.report(info, part)
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.hideLoading();
                        mRootView.showMessage(mApplication.getString(R.string.tip_report));
                        mRootView.killMyself();
                    }
                });

    }

    /**
     * 请求权限
     */
    public void requestPermissions(final int type) {
        PermissionUtil.selectPhoto(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                if (type == 1) {
                    mRootView.takePictureFormCamera();
                } else {
                    mRootView.turnToImagePicker();
                }

            }

            @Override
            public void onRequestPermissionRefuse() {
                mRootView.showMessage(mApplication.getString(R.string.permiss_changephoto));
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
    }

}
