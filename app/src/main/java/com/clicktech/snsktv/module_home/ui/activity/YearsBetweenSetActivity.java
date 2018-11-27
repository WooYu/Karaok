package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_home.contract.YearsBetweenSetContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerYearsBetweenSetComponent;
import com.clicktech.snsktv.module_home.inject.module.YearsBetweenSetModule;
import com.clicktech.snsktv.module_home.presenter.YearsBetweenSetPresenter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.widget.WheelView;

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
 * Created by Administrator on 2017/3/20.
 * 年代选择
 */

public class YearsBetweenSetActivity extends WEActivity<YearsBetweenSetPresenter> implements
        YearsBetweenSetContract.View, View.OnClickListener, HeaderView.OnCustomTileListener {

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.wheelview)
    WheelView wheelView;
    @BindView(R.id.end_wheelview)
    WheelView endWheelView;
    private List<String> beginList = new ArrayList<>();
    private List<String> endList = new ArrayList<>();
    private String beginYear;
    private String endYear;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerYearsBetweenSetComponent
                .builder()
                .appComponent(appComponent)
                .yearsBetweenSetModule(new YearsBetweenSetModule(this)) //请将YearsBetweenSetModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_yearsbetween_set, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        initList();
        initWheeleView();
    }

    private void initList() {

    }

    private void initWheeleView() {
        int startYear = 1970;
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = startYear; i < curYear; i++) {
            beginList.add(String.valueOf(i));
        }
        for (int i = curYear; i > startYear; i--) {
            endList.add(String.valueOf(i));
        }

        wheelView.setOffset(1);
        wheelView.setItems(beginList);
        wheelView.setTextColor(getResources().getColor(R.color.text_medium), getResources().getColor(R.color.text_medium));
//        wheelView.setCacheColorHint(getResources().getColor(R.color.text_medium));
        WheelView.LineConfig config = new WheelView.LineConfig();
        config.setRatio(0);
        config.setColor(getResources().getColor(R.color.gainsboro));
        wheelView.setLineConfig(config);
//        wheelView.setTextColor(getResources().getColor(R.color.black),getResources().getColor(R.color.black));

        wheelView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                beginYear = item;
            }
        });

        endWheelView.setOffset(1);
        int endindex = endList.indexOf(curYear);
        endWheelView.setItems(endList, endindex);
//        endWheelView.setBackgroundColor(getResources().getColor(R.color.red_dark));
        endWheelView.setTextColor(getResources().getColor(R.color.text_medium), getResources().getColor(R.color.text_medium));
//        wheelView.setCacheColorHint(getResources().getColor(R.color.text_medium));
        WheelView.LineConfig endconfig = new WheelView.LineConfig();
        endconfig.setRatio(0);
        endconfig.setColor(getResources().getColor(R.color.gainsboro));
        endWheelView.setLineConfig(endconfig);
        endWheelView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                endYear = item;
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
        UiUtils.startActivity(this, intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @OnClick({R.id.tv_ok})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                turnToList();
                break;
        }
    }

    private void turnToList() {
        if (TextUtils.isEmpty(beginYear)) {
            showMessage(getString(R.string.year_between_year_beg_hint));
            return;
        }

        if (TextUtils.isEmpty(endYear)) {
            showMessage(getString(R.string.year_between_year_end_hint));
            return;
        }

        if (Integer.parseInt(beginYear) >= Integer.parseInt(endYear)) {
            showMessage(getString(R.string.error_year_notlegal));
            return;
        }
        Intent intent = new Intent(this, ClassifyListActivity.class);
        intent.putExtra("beginyear", beginYear);
        intent.putExtra("endyear", endYear);
        intent.putExtra("incometype", 2);
        intent.putExtra("title", beginYear + "-" + endYear);
        launchActivity(intent);
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("classtype", "song");
        UiUtils.startActivity(intent);
    }

}
