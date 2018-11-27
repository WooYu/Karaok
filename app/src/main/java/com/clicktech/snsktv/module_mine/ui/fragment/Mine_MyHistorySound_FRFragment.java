package com.clicktech.snsktv.module_mine.ui.fragment;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEFragment;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.SoundHistoryEntity;
import com.clicktech.snsktv.module_home.ui.activity.LearnSingActivity;
import com.clicktech.snsktv.module_mine.contract.Mine_MyHistorySound_FRContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMine_MyHistorySound_FRComponent;
import com.clicktech.snsktv.module_mine.inject.module.Mine_MyHistorySound_FRModule;
import com.clicktech.snsktv.module_mine.presenter.Mine_MyHistorySound_FRPresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.AccompanimentAdapter;
import com.clicktech.snsktv.module_mine.ui.adapter.SwipLocalRecordingAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

import static com.clicktech.snsktv.arms.utils.Preconditions.checkNotNull;

/**
 * Created by Administrator on 2017/3/24.
 * 已点伴奏
 */

public class Mine_MyHistorySound_FRFragment extends WEFragment<Mine_MyHistorySound_FRPresenter> implements Mine_MyHistorySound_FRContract.View
        , XRecyclerView.LoadingListener {

    public static final String ARGUMENTS = "arguments";
    @BindView(R.id.swiprecyclerview)
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    private int type = 0;//0已点伴奏，1练唱列表
    private int index = 1;
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            int width = getResources().getDimensionPixelSize(R.dimen.mine_collect_height);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mWeApplication)
                        .setBackgroundDrawable(R.drawable.shape_collection_deltete)
                        .setImage(R.mipmap.unpublished_delete)
//                       .setText("删除") // 文字，还可以设置文字颜色，大小等。。
//                       .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
            }
        }
    };

    public static Mine_MyHistorySound_FRFragment newInstance(int position) {
        Mine_MyHistorySound_FRFragment fragment = new Mine_MyHistorySound_FRFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGUMENTS, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerMine_MyHistorySound_FRComponent
                .builder()
                .appComponent(appComponent)
                .mine_MyHistorySound_FRModule(new Mine_MyHistorySound_FRModule(this))//请将Mine_MyHistorySound_FRModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_myhistorysounds, null, false);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (null != bundle)
            type = bundle.getInt(ARGUMENTS);
        Timber.d(String.valueOf(type));


        switch (type) {
            case 0:
                mPresenter.requestData_history(RequestParams_Maker.getHadAccompanied(mWeApplication.getUserID(), index + "", "10"), index);
                break;
            case 1:
                mPresenter.requestData_history(RequestParams_Maker.getHadAccompanied(mWeApplication.getUserID(), index + "", "10"), index);
                break;
        }
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
    public void setRichListRecyclerView(final AccompanimentAdapter adapter, final List<SoundHistoryEntity> hotSingers) {
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(mWeApplication));
        swipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                closeable.smoothCloseMenu();// 关闭被点击的菜单。
                hotSingers.remove(adapterPosition);
                adapter.notifyItemRemoved(adapterPosition);
            }
        };
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
        swipeMenuRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SwipLocalRecordingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = null;
                switch (v.getId()) {
                    case R.id.item_mine_mychroussongs_time:
                        break;

                    case R.id.ll:
                        intent = new Intent(mWeApplication, LearnSingActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("songid", hotSingers.get(position).getSong_id());
                        intent.putExtras(bundle);
                        UiUtils.startActivity(intent);
                        break;
                }
            }
        });
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
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }


}