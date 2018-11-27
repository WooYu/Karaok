package com.clicktech.snsktv.module_discover.presenter;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.arms.utils.PermissionUtil;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.MvPreviewFirstContract;
import com.clicktech.snsktv.module_discover.ui.adapter.TemplateAndFilterAdapter;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;

import java.io.File;

import javax.inject.Inject;

import timber.log.Timber;

import static com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType.MOSCOW;
import static com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType.NARA;
import static com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType.ORIGINAL;
import static com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType.SEOUL;
import static com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType.SMMCL;
import static com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType.SUNDAE;
import static com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType.SUNSHINE;
import static com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType.SWEET;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017-05-26.
 */

@ActivityScope
public class MvPreviewFirstPresenter extends BasePresenter<MvPreviewFirstContract.Model, MvPreviewFirstContract.View> {
    private final TemplateAndFilterType[] mFilterTypes = new TemplateAndFilterType[]{
            ORIGINAL,
            SUNDAE,
            SWEET,
            MOSCOW,
            SMMCL,
            SUNSHINE,
            NARA,
            SEOUL
    };
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private TemplateAndFilterAdapter mFilterAdapter;
    private MaterialDialog mTipDialog;
    private TemplateAndFilterAdapter.onFilterChangeListener onFilterChangeListener =
            new TemplateAndFilterAdapter.onFilterChangeListener() {

                @Override
                public void onFilterChanged(int position) {
                    mRootView.setFilterEffect(mFilterTypes[position]);
                }
            };
    private boolean bDialogShowing;


    @Inject
    public MvPreviewFirstPresenter(MvPreviewFirstContract.Model model, MvPreviewFirstContract.View rootView
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

        OkGo.getInstance().cancelTag(this);

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

        if (null != mTipDialog) {
            mTipDialog.superDismiss();
        }

        super.onDestroy();
    }

    public void requestData(final SongInfoBean songinfo) {
        PermissionUtil.recordmv(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                request_downloadLyric(songinfo.getLyric_url());
            }

            @Override
            public void onRequestPermissionRefuse() {
                mRootView.showMessage(mApplication.getString(R.string.permiss_recordmv));
            }
        }, mRootView.getRxPermissions(), mErrorHandler);

        mFilterAdapter = new TemplateAndFilterAdapter(mApplication, mFilterTypes, 0);
        mFilterAdapter.setOnFilterChangeListener(onFilterChangeListener);
        mRootView.setTemplateFilterAdapter(mFilterAdapter);
    }

    //下载歌词
    private void request_downloadLyric(final String lyricpath) {
        if (EmptyUtils.isEmpty(lyricpath)) {
            mRootView.showMessage(mApplication.getString(R.string.error_lyricexception));
            return;
        }

        //歌词保存路径
        final String lyricsavepath = FileUtils.getCacheDirectory(mApplication,
                Environment.DIRECTORY_MUSIC).getAbsolutePath() + ConstantConfig.Dirs_Lyric +
                lyricpath;
        if (FileUtils.isFileExists(lyricsavepath)) {
            mRootView.setLyricSavePath(lyricsavepath);
            return;
        }

        FileUtils.createOrExistsFile(lyricsavepath);
        OkGo.<File>get(BuildConfig.APP_DOMAIN_File + lyricpath)
                .tag(this)
                .execute(new FileCallback(FileUtils.getDirName(lyricsavepath), lyricpath) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Timber.d("歌词下载完成");
                        mRootView.setLyricSavePath(lyricsavepath);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Timber.e("歌词下载失败！");
                        FileUtils.deleteFile(lyricsavepath);
                        if (null != mRootView) {
                            mRootView.showMessage(mApplication.getString(R.string.error_lyricdownfail));
                        }
                    }
                });

    }

    //展示提示对话框
    public void showTipDialog(Context context, String content) {
        if (bDialogShowing) {
            return;
        }


        bDialogShowing = true;

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
                        bDialogShowing = false;
                        mTipDialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        bDialogShowing = false;
                        mTipDialog.dismiss();
                        mRootView.hideLoading();
                        mRootView.killMyself();
                    }
                }
        );

    }

}
