package com.clicktech.snsktv.module_mine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SingerIntroActivity;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_mine.contract.Mine_MyCollectionContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMine_MyCollectionComponent;
import com.clicktech.snsktv.module_mine.inject.module.Mine_MyCollectionModule;
import com.clicktech.snsktv.module_mine.presenter.Mine_MyCollectionPresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.SwipCollectionListAdapter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

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
 * Created by Administrator on 2017/3/24.
 * 我的收藏---我的
 */

public class Mine_MyCollectionActivity extends WEActivity<Mine_MyCollectionPresenter> implements Mine_MyCollectionContract.View
        , HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.swiprecyclerview)
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    @BindView(R.id.ll_no_collection)
    LinearLayout noCollection;

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            int width = getResources().getDimensionPixelSize(R.dimen.mine_collect_height);
            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            SwipeMenuItem deleteItem = new SwipeMenuItem(mWeApplication)
                    .setBackgroundDrawable(R.drawable.shape_collection_deltete)
                    .setImage(R.mipmap.unpublished_delete)
//                       .setText("删除") // 文字，还可以设置文字颜色，大小等。。
//                       .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

        }
    };
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。
        }
    };

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMine_MyCollectionComponent
                .builder()
                .appComponent(appComponent)
                .mine_MyCollectionModule(new Mine_MyCollectionModule(this)) //请将Mine_MyCollectionModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_mine_mycollection, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        mPresenter.requestData();
    }

    @Override
    public void setSwipRecyclerview(SwipCollectionListAdapter adapter, List<SongInfoBean> hotSingers,
                                    OnSwipeMenuItemClickListener listener) {
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(mWeApplication));
        swipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(listener);
        swipeMenuRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showNoCollection(boolean isCollection) {
        if (isCollection) {
            noCollection.setVisibility(View.VISIBLE);
        } else {
            noCollection.setVisibility(View.GONE);
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
        UiUtils.startActivity(this, intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void setTitleLeftClick() {
        finish();
    }

    @Override
    public void setTitleRightClick() {

    }
}
