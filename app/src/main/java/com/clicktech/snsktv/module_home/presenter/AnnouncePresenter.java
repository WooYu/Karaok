package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.CommonService;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.common.net.okgo.JsonCallback;
import com.clicktech.snsktv.entity.AnnounceResponse;
import com.clicktech.snsktv.entity.PublishInforEntity;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.module_home.contract.AnnounceContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.clicktech.snsktv.util.amazonaws.Util;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import java.io.File;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017/4/18.
 */

@ActivityScope
public class AnnouncePresenter extends BasePresenter<AnnounceContract.Model, AnnounceContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private PublishInforEntity mPublishInfo;
    private ScoreResultEntity mScoreResult;//打分结果
    private int idOfTransfer;//亚马逊传输的id
    private TransferUtility transferUtility;//亚马逊传输实体
    private final int REMAINPERCENT = 5;//上传服务器预留的剩余百分点
    private long mTimeOfReleasingStart;//发布开始时间
    private MaterialDialog mTipDialog;//提示弹窗

    @Inject
    public AnnouncePresenter(AnnounceContract.Model model, AnnounceContract.View rootView
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
        OkGo.getInstance().cancelTag(this);

        if (null != transferUtility) {
            transferUtility.cancelAllWithType(TransferType.UPLOAD);
        }

        if (null != mTipDialog) {
            mTipDialog.superDismiss();
        }

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

    }

    //设置打分结果
    public void setScoreResult(ScoreResultEntity scoreResult) {
        mScoreResult = scoreResult;
    }

    //亚马逊上传
    public void upload_amazonServers(PublishInforEntity entity) {
        if (null == entity) {
            return;
        }

        mPublishInfo = entity;

        //如果没有打分时，则直接上传到后台服务器
        if (null == mScoreResult) {
            upload_appServers();
            return;
        }

        //判断作品是否存在
        if (!FileUtils.isFileExists(mPublishInfo.getWorks_url())) {
            Timber.e("上传作品不存在！");
            return;
        }

        transferUtility = Util.getTransferUtility(mApplication);
        final File file = new File(mPublishInfo.getWorks_url());

        TransferObserver observer = transferUtility.upload(BuildConfig.BUCKET_NAME, file.getName(),
                file);
        idOfTransfer = observer.getId();
        mRootView.showNumberDialog();
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.d(TAG, "onStateChanged: " + id + ", " + state);
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                        id, bytesTotal, bytesCurrent));
                int percentage = (int) (bytesCurrent * 100 / bytesTotal);
                //预留5个百分点，上传后台服务器
                mRootView.updateNumberDialog(percentage > REMAINPERCENT ? (percentage - REMAINPERCENT) : 0);
                if (percentage == 100) {
                    mPublishInfo.setWorks_url(file.getName());
                    upload_appServers();
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e(TAG, "Error during upload: " + id, ex);
                mRootView.dismissNumberDialog();
            }

        });

    }

    public void onPause() {
        if (null != transferUtility) {
            transferUtility.pause(idOfTransfer);
        }
    }

    public void onResume() {
        if (null != transferUtility) {
            transferUtility.resume(idOfTransfer);
        }
    }

    // 调用服务器接口上传
    private synchronized void upload_appServers() {
        if (System.currentTimeMillis() - mTimeOfReleasingStart < 3000) {
            return;
        }
        mTimeOfReleasingStart = System.currentTimeMillis();

        mRootView.updateNumberDialog((int) (Math.random() * REMAINPERCENT + 100 - REMAINPERCENT));

        String auth = RequestParams_Maker.getAuthString();
        String sign = RequestParams_Maker.getSignString(auth);
        String info = ((KtvApplication) mApplication).getAppComponent().gson().toJson(mPublishInfo);
        Timber.d("info = " + info);

        String url = EmptyUtils.isEmpty(mScoreResult) ?
                BuildConfig.APP_DOMAIN + CommonService.EDITWORKSFORPUBLISH
                : BuildConfig.APP_DOMAIN + CommonService.PUBLISHWORKS;

        PostRequest<AnnounceResponse> postRequest = OkGo.<AnnounceResponse>post(url)
                .tag(this)
                .params("auth", auth)
                .params("sign", sign)
                .params("info", info);
        if (EmptyUtils.isNotEmpty(mPublishInfo.getPhoto())) {
            postRequest.params("photo", new File(mPublishInfo.getPhoto()));
        }

        postRequest.execute(new JsonCallback<AnnounceResponse>() {
            @Override
            public void onSuccess(Response<AnnounceResponse> response) {
                AnnounceResponse announceResponse = response.body();
                if (announceResponse.getErrCode() == 0) {
                    requestUploadScoreResult(announceResponse.getWorksId());
                } else {
                    UiUtils.SnackbarText(announceResponse.getMsg());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null != mRootView) {
                    mRootView.dismissNumberDialog();
                }
            }
        });

    }

    //上传打分结果
    void requestUploadScoreResult(long worksid) {
        if (null == mScoreResult) {
            mRootView.turn2MainActivity();
            return;
        }

        String userid = ((KtvApplication) mApplication).getUserID();
        mScoreResult.setUserId(Long.parseLong(userid));
        mScoreResult.setSong_id(Long.parseLong(mPublishInfo.getSong_id()));
        mScoreResult.setWorks_id(worksid);
        mModel.publishWorksScore(mScoreResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseResponse>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.turn2MainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }
                });
    }

    //展示提示框
    public void showTipDialog(Context context, String content) {

        mTipDialog = new MaterialDialog(context);
        mTipDialog.content(content)//
                .btnText(mApplication.getString(R.string.dialog_cancel),
                        mApplication.getString(R.string.dialog_sure))//
                .showAnim(new BounceEnter())//
                .dismissAnim(new FadeExit())//
                .show();

        mTipDialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        mTipDialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        mTipDialog.dismiss();
                        mRootView.killMyself();
                    }
                }
        );

    }

}
