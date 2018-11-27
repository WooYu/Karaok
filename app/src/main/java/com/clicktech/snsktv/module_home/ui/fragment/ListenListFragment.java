package com.clicktech.snsktv.module_home.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.headerviewpager.HeaderScrollHelper;
import com.clicktech.snsktv.arms.widget.scrollview.NestedScrollView;
import com.clicktech.snsktv.common.WEFragment;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.module_home.contract.ListenListContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerListenListComponent;
import com.clicktech.snsktv.module_home.inject.module.ListenListModule;
import com.clicktech.snsktv.module_home.presenter.ListenListPresenter;
import com.clicktech.snsktv.module_home.ui.adapter.ListenNationAdapter;
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
 * Created by Administrator on 2017/1/19.
 * 收听榜
 */

public class ListenListFragment extends WEFragment<ListenListPresenter> implements
        ListenListContract.View, HeaderScrollHelper.ScrollableContainer, XRecyclerView.LoadingListener {

    private static final String ARG_PAGE = "ARG_PAGE";
    @BindView(R.id.ll_empty)
    LinearLayout mEmptyView;
    @BindView(R.id.ll_data)
    LinearLayout mDataLL;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollview;
    @BindView(R.id.ll_friendrank)
    LinearLayout mFriendRankLL;
    @BindView(R.id.rv_friendrank)
    RecyclerView mFriendRankRv;
    @BindView(R.id.rv_nationrank)
    XRecyclerView mNationRankRv;
    boolean canLoadmore = true;
    String song_songId;
    private int index = 1;

    public static ListenListFragment newInstance(String song_songId) {
        ListenListFragment fragment = new ListenListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, song_songId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        song_songId = getArguments().getString(ARG_PAGE);
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerListenListComponent
                .builder()
                .appComponent(appComponent)
                .listenListModule(new ListenListModule(this))//请将ListenListModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_listenlist, null, false);
    }

    @Override
    protected void initData() {
        //type	是	String	1收听榜，2人气榜，（未添加 3评委榜，4合唱推荐）
        mPresenter.requestData(RequestParams_Maker.getLearnSingSongWorksList(song_songId,
                index, 10, getResources().getInteger(R.integer.ranktype_listen)), index);
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一方法,
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
    public View getScrollableView() {
        return mScrollview;
    }

    @Override
    public void setListentRankRecyclerView(ListenNationAdapter adapter) {
        mNationRankRv.setLayoutManager(new LinearLayoutManager(mWeApplication));
        mNationRankRv.setLoadingListener(this);
        mNationRankRv.setPullRefreshEnabled(false);
        mNationRankRv.setAdapter(adapter);
    }

    @Override
    public void setEmptyView(boolean show) {
        mDataLL.setVisibility(show ? View.GONE : View.VISIBLE);
        mEmptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void canLoadmore(boolean can, int index) {
        canLoadmore = can;
        if (can) {
            this.index += 1;
        }
        if (index != 1) {
            mNationRankRv.loadMoreComplete();
        } else {
            mNationRankRv.refreshComplete();
        }
    }

    @Override
    public void onRefresh() {
        index = 1;
        mPresenter.requestData(RequestParams_Maker.getLearnSingSongWorksList(song_songId,
                index, 10, getResources().getInteger(R.integer.ranktype_listen)), 1);
    }

    @Override
    public void onLoadMore() {
        mPresenter.requestData(RequestParams_Maker.getLearnSingSongWorksList(song_songId,
                index, 10, getResources().getInteger(R.integer.ranktype_listen)), 2);
    }
}