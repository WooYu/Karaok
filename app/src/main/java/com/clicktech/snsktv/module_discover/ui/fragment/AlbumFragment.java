package com.clicktech.snsktv.module_discover.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.recyclerview.FullyGridLayoutManager;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.WEFragment;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.AlbumBean;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.contract.AlbumContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerAlbumComponent;
import com.clicktech.snsktv.module_discover.inject.module.AlbumModule;
import com.clicktech.snsktv.module_discover.presenter.AlbumPresenter;
import com.clicktech.snsktv.module_discover.ui.activity.SingerIntroActivity;
import com.clicktech.snsktv.module_discover.ui.adapter.AlbumListAdapter;
import com.clicktech.snsktv.module_discover.ui.adapter.SingleListAdapter;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

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
 * Created by Administrator on 2017/5/17.
 * 个人中心的fragment
 */

public class AlbumFragment extends WEFragment<AlbumPresenter> implements AlbumContract.View {


    @BindView(R.id.iv_special)
    ImageView ivSpecial;
    @BindView(R.id.tv_special)
    TextView tvSpecial;
    @BindView(R.id.tv_specialnum)
    TextView tvSpecialnum;
    @BindView(R.id.tv_allspecial)
    TextView tvAllspecial;
    @BindView(R.id.rl_special)
    RelativeLayout rlSpecial;
    @BindView(R.id.albumrecyclerview)
    RecyclerView albumrecyclerview;
    @BindView(R.id.tv_emptyspecial)
    TextView tvEmptyspecial;
    @BindView(R.id.iv_single)
    ImageView ivSingle;
    @BindView(R.id.tv_single)
    TextView tvSingle;
    @BindView(R.id.tv_singlenum)
    TextView tvSinglenum;
    @BindView(R.id.tv_playsingle)
    TextView tvPlaysingle;
    @BindView(R.id.rl_single)
    RelativeLayout rlSingle;
    @BindView(R.id.instancerecyclerview)
    RecyclerView instancerecyclerview;
    @BindView(R.id.tv_emptysingle)
    TextView tvEmptysingle;

    public static AlbumFragment newInstance() {
        AlbumFragment fragment = new AlbumFragment();
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerAlbumComponent
                .builder()
                .appComponent(appComponent)
                .albumModule(new AlbumModule(this))//请将AlbumModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_album, null, false);
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
        if (data instanceof Bundle) {
            Bundle bundle = (Bundle) data;
            int type = bundle.getInt("type");
            if (type == 1) {
                ArrayList<AlbumBean> albumList = bundle.getParcelableArrayList("albumlist");
                int albumSize = EmptyUtils.isEmpty(albumList) ? 0 : albumList.size();
                tvSpecialnum.setText(String.format(getString(R.string.format_photonum), String.valueOf(albumSize)));
                mPresenter.setAlbumList(albumList);
                showEmptyAlbum(EmptyUtils.isEmpty(albumList));
            } else if (type == 2) {
                ArrayList<SongInfoBean> workList = bundle.getParcelableArrayList("worklist");
                int workSize = EmptyUtils.isEmpty(workList) ? 0 : workList.size();
                tvSinglenum.setText(String.format(getString(R.string.format_unitofsinglenum), String.valueOf(workSize)));
                mPresenter.setWorkList(workList);
                showEmptyMusic(EmptyUtils.isEmpty(workList));
            }
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

    @Override
    public void setAlbumListRecyclerView(AlbumListAdapter adapter) {
        albumrecyclerview.setLayoutManager(new LinearLayoutManager(mWeApplication) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        albumrecyclerview.setAdapter(adapter);
    }

    @Override
    public void setSingleRecyclerView(SingleListAdapter adapter) {

        instancerecyclerview.setLayoutManager(new GridLayoutManager(mWeApplication, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        instancerecyclerview.setAdapter(adapter);
    }

    @Override
    public void showEmptyAlbum(Boolean isShow) {
        albumrecyclerview.setVisibility(isShow ? View.GONE : View.VISIBLE);
        tvEmptyspecial.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showEmptyMusic(Boolean isShow) {
        instancerecyclerview.setVisibility(isShow ? View.GONE : View.VISIBLE);
        tvEmptysingle.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.tv_allspecial)
    public void onAllAlbumClicked() {
        SingerIntroActivity singerIntroActivity = (SingerIntroActivity) getActivity();
        singerIntroActivity.turn2AllAlbum();
    }

    @OnClick(R.id.tv_playsingle)
    public void onPlayAllSingleClicked() {
        if (EmptyUtils.isEmpty(mPresenter.getWorkList())) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putParcelableArrayList("songlist", mPresenter.getWorkList());
        Message message = new Message();
        message.setData(bundle);
        EventBus.getDefault().post(message, EventBusTag.PLAYLIST_ADDSONG);

    }

}