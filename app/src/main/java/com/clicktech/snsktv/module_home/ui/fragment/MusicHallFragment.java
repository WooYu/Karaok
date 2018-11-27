package com.clicktech.snsktv.module_home.ui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.autolayout.AutoRadioGroup;
import com.clicktech.snsktv.common.WEFragment;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.BannersEntity;
import com.clicktech.snsktv.module_discover.ui.activity.WebActivity;
import com.clicktech.snsktv.module_enter.ui.activity.LoginActivity;
import com.clicktech.snsktv.module_home.contract.MusicHallContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerMusicHallComponent;
import com.clicktech.snsktv.module_home.inject.module.MusicHallModule;
import com.clicktech.snsktv.module_home.presenter.MusicHallPresenter;
import com.clicktech.snsktv.module_home.ui.activity.CantataActivity;
import com.clicktech.snsktv.module_home.ui.activity.CategoryActivity;
import com.clicktech.snsktv.module_home.ui.activity.ChorusTypeActivity;
import com.clicktech.snsktv.module_home.ui.activity.SingerSelectActivity;
import com.clicktech.snsktv.module_home.ui.activity.ThemeActivity;
import com.clicktech.snsktv.module_home.ui.activity.YearsBetweenSetActivity;
import com.clicktech.snsktv.module_home.ui.holder.NetworkImageHolderView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bigkoo.convenientbanner.ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL;
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
 * Created by Administrator on 2017/2/3.
 * 音乐馆（K2）
 */

public class MusicHallFragment extends WEFragment<MusicHallPresenter> implements
        CompoundButton.OnCheckedChangeListener, MusicHallContract.View {

    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @BindView(R.id.rb_popular)
    RadioButton rbPopular;
    @BindView(R.id.rb_recommend)
    RadioButton rbRecommend;
    @BindView(R.id.rb_newly)
    RadioButton rbNewly;
    @BindView(R.id.rg_musichall)
    AutoRadioGroup rgMusichall;

    private int spot[] = {R.mipmap.spot_normal, R.mipmap.spot_selcet};

    public static MusicHallFragment newInstance() {
        MusicHallFragment fragment = new MusicHallFragment();
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerMusicHallComponent
                .builder()
                .appComponent(appComponent)
                .musicHallModule(new MusicHallModule(this)) //请将MusicHallModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_musichall, null, false);
    }

    @Override
    protected void initData() {
        mPresenter.requestBannerData(mWeApplication.getUserID());
        rbPopular.setOnCheckedChangeListener(this);
        rbNewly.setOnCheckedChangeListener(this);
        rbRecommend.setOnCheckedChangeListener(this);
        rbPopular.performClick();
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
        UiUtils.startActivity(getActivity(), intent);
    }

    @Override
    public void killMyself() {
    }

    @Override
    public void setTopBanners(final List<BannersEntity> list) {
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, list);
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mWeApplication, WebActivity.class);
                intent.putExtra("title", list.get(position).getCarousel_title());
                intent.putExtra("url", list.get(position).getCarousel_link());
                UiUtils.startActivity(intent);

            }
        });

        convenientBanner.setPageIndicator(spot);
        convenientBanner.setPointViewVisible(true);
        convenientBanner.setPageIndicatorAlign(CENTER_HORIZONTAL);
        convenientBanner.isTurning();
        convenientBanner.startTurning(2000);
    }

    @OnClick({R.id.view_artistname, R.id.view_songname, R.id.view_chorus, R.id.view_time,
            R.id.view_cantata, R.id.view_practice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_artistname://歌手名
                UiUtils.startActivity(SingerSelectActivity.class);
                break;
            case R.id.view_songname://曲名
                UiUtils.startActivity(CategoryActivity.class);
                break;
            case R.id.view_chorus://合唱
                UiUtils.startActivity(ChorusTypeActivity.class);
                break;
            case R.id.view_time://年代
                UiUtils.startActivity(YearsBetweenSetActivity.class);
                break;
            case R.id.view_cantata://清唱
                if (mWeApplication.loggingStatus()) {
                    launchActivity(new Intent(mWeApplication, CantataActivity.class));
                } else {
                    launchActivity(new Intent(mWeApplication, LoginActivity.class));
                }
                break;
            case R.id.view_practice://主题
                launchActivity(new Intent(mWeApplication, ThemeActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //用户当前浏览的选项卡
            int checkedWidgetId = buttonView.getId();
            rbRecommend.setChecked(checkedWidgetId == R.id.rb_recommend);
            rbNewly.setChecked(checkedWidgetId == R.id.rb_newly);
            rbPopular.setChecked(checkedWidgetId == R.id.rb_popular);
            showFragment(checkedWidgetId);
        } else {
            //此处记录了用户上次浏览的选项卡
            String unCheckFragmentTag = getTagById(buttonView.getId());
            SongListFragment unCheckFragment = (SongListFragment) getChildFragmentManager().findFragmentByTag(unCheckFragmentTag);
            if (unCheckFragment != null) {
                //隐藏上次显示到fragment,确保fragment不会重叠
                getChildFragmentManager()
                        .beginTransaction()
                        .hide(unCheckFragment)
                        .commit();
            }
        }
    }

    /**
     * 显示对应的fragment
     *
     * @param checkedRadioBtnId
     */
    private void showFragment(int checkedRadioBtnId) {
        String tag = getTagById(checkedRadioBtnId);
        SongListFragment fragment = (SongListFragment) getChildFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            //如果没有找到对应的fragment则生成一个新的fragment，并添加到容器中
            SongListFragment newFragment = SongListFragment.newInstance(tag);
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.layout_songlist, newFragment, tag)
                    .commit();
        } else {
            //如果找到了fragment则显示它
            getChildFragmentManager()
                    .beginTransaction()
                    .show(fragment)
                    .commit();
        }
    }

    /**
     * 为三个fragment分别取三个不同到tag名
     *
     * @param widgetId
     * @return
     */
    private String getTagById(int widgetId) {
        if (widgetId == R.id.rb_popular) {
            return "1";
        } else if (widgetId == R.id.rb_recommend) {
            return "2";
        } else {
            return "3";
        }
    }

}
