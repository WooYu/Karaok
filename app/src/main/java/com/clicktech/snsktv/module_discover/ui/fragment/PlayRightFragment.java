package com.clicktech.snsktv.module_discover.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEFragment;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_discover.contract.PlayRightContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerPlayRightComponent;
import com.clicktech.snsktv.module_discover.inject.module.PlayRightModule;
import com.clicktech.snsktv.module_discover.presenter.PlayRightPresenter;
import com.library.multimedia.lyricscontrols.parser.LrcRow;
import com.library.multimedia.lyricscontrols.view.LyricOfPlayImpl;

import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

import static com.clicktech.snsktv.arms.utils.Preconditions.checkNotNull;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Administrator on 2017/4/23.
 * 歌曲详情右边
 */

public class PlayRightFragment extends WEFragment<PlayRightPresenter> implements PlayRightContract.View {

    @BindView(R.id.lrcview)
    LyricOfPlayImpl lrcView;

    public static PlayRightFragment newInstance() {
        PlayRightFragment fragment = new PlayRightFragment();
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerPlayRightComponent
                .builder()
                .appComponent(appComponent)
                .playRightModule(new PlayRightModule(this))//请将PlayRightModule()第一个首字母改为小写
                .build()
                .inject(this);
    }


    @Override
    protected View initView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_playright, null, false);
    }

    @Override
    protected void initData() {

    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传bundle,里面存一个what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事,和message同理
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在内部onActivityCreated中
     * 初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {
        if (null != data && data instanceof Bundle) {
            Bundle bundle = (Bundle) data;
            String lyricpath = bundle.getString("lyricpath");
            mPresenter.parseLyricPath(lyricpath);
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    public void updateLrc(List<LrcRow> lrcRowList) {
        if (EmptyUtils.isNotEmpty(lrcRowList)) {
            lrcView.setLrcRows(lrcRowList);
        } else {
            lrcView.reset();
            Timber.e("解析歌词异常：lrcRowList = null");
        }
    }

    public void seekTo(int progress) {
        if (null != lrcView) {
            lrcView.seekTo(progress, false, false);
        }
    }

}