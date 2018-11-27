package com.clicktech.snsktv.module_discover.presenter;

import android.app.Application;
import android.os.Environment;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.FileUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.SelectRoleContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;

import java.io.File;

import javax.inject.Inject;

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
 * Created by Administrator on 2017/4/26.
 */

@ActivityScope
public class SelectRolePresenter extends BasePresenter<SelectRoleContract.Model, SelectRoleContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public SelectRolePresenter(SelectRoleContract.Model model, SelectRoleContract.View rootView
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

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

    }

    public void requestData(SongInfoBean songinfo) {
        if (null != songinfo) {
            downloadLrcData(songinfo.getLyric_url());
        }
    }

    /**
     * 下载歌词
     *
     * @param lyric_url
     */
    private void downloadLrcData(String lyric_url) {
        if (EmptyUtils.isEmpty(lyric_url)) {
            Timber.e("歌词下载路径异常");
            return;
        }

        //如果本地存在文件就不下载
        final String mLyricPath = FileUtils.getCacheDirectory(mApplication,
                Environment.DIRECTORY_MUSIC).getAbsolutePath() + ConstantConfig.Dirs_Lyric + lyric_url;
        if (FileUtils.isFileExists(mLyricPath)) {
            mRootView.updateLrc(mModel.parseLrc(mLyricPath));
            return;
        }

        //歌词不存在就下载
        OkGo.<File>get(BuildConfig.APP_DOMAIN_File + lyric_url)
                .tag(this)
                .execute(new FileCallback(FileUtils.getDirName(mLyricPath), lyric_url) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Timber.d("歌词下载完成");
                        mRootView.updateLrc(mModel.parseLrc(mLyricPath));
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Timber.e("歌词下载失败！");
                        FileUtils.deleteFile(mLyricPath);
                        if (null != mRootView) {
                            mRootView.showMessage(mApplication.getString(R.string.error_lyricdownfail));
                        }
                    }
                });
    }

}
