package com.clicktech.snsktv.module_home.ui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEFragment;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_home.contract.ChorusStarContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerChorusStarComponent;
import com.clicktech.snsktv.module_home.inject.module.ChorusStarModule;
import com.clicktech.snsktv.module_home.presenter.ChorusStarPresenter;
import com.clicktech.snsktv.module_home.ui.adapter.ChorusStarAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;

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
 * Created by Administrator on 2017/1/16.
 */

public class ChorusStarFragment extends WEFragment<ChorusStarPresenter> implements ChorusStarContract.View {

    @BindView(R.id.xrv_ever)
    XRecyclerView xRecyclerView;


    public static ChorusStarFragment newInstance() {
        ChorusStarFragment fragment = new ChorusStarFragment();
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerChorusStarComponent
                .builder()
                .appComponent(appComponent)
                .chorusStarModule(new ChorusStarModule(this))//请将ChorusStarModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ever, null, false);
    }

    @Override
    protected void initData() {
        mPresenter.requestData();
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

    @Override
    public void setStarRecyclerView(ChorusStarAdapter adapter) {
        xRecyclerView.setLayoutManager(new LinearLayoutManager(mWeApplication));
        xRecyclerView.setAdapter(adapter);
    }
}