package com.clicktech.snsktv.module_discover.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.Preconditions;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_discover.contract.SelectCityContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerSelectCityComponent;
import com.clicktech.snsktv.module_discover.inject.module.SelectCityModule;
import com.clicktech.snsktv.module_discover.presenter.SelectCityPresenter;
import com.clicktech.snsktv.module_discover.ui.adapter.SelectCityAdapter;
import com.clicktech.snsktv.module_discover.ui.fragment.SelectNationFragment;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

public class SelectCityActivity extends WEActivity<SelectCityPresenter> implements
        SelectCityContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView mHeaderView;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search_clear)
    ImageView ivSearchClear;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSelectCityComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .selectCityModule(new SelectCityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_select_city,
                null, false);
    }

    @Override
    protected void initData() {
        mHeaderView.setTitleClickListener(this);
        initEditText();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        Preconditions.checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        Preconditions.checkNotNull(intent);
        UiUtils.startActivity(this, intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void setRecyclerView(SelectCityAdapter adapter) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void returnSelectCit(String city) {
        getIntent().putExtra("city", city);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void setTitleLeftClick() {
        hideInputBox();
    }

    @Override
    public void setTitleRightClick() {
        SelectNationFragment fragment = new SelectNationFragment();
        fragment.setmCallBack(new SelectNationFragment.SwitchNationCallBack() {
            @Override
            public void switch2Japan() {
                mPresenter.getJapanCities();
            }

            @Override
            public void switch2UnJapan() {
                mPresenter.getUnJapanCities();
            }
        });
        fragment.show(getSupportFragmentManager(), getClass().getSimpleName());
    }

    @OnClick(R.id.iv_search_clear)
    public void onSearchClearClicked() {
        etSearch.setText("");
        ivSearchClear.setVisibility(View.GONE);
        mPresenter.clearSearch();
    }

    private void initEditText() {
        RxTextView.textChangeEvents(etSearch)
                .debounce(300, TimeUnit.MILLISECONDS)  //debounce:每次文本更改后有300毫秒的缓冲时间，默认在computation调度器
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<TextViewTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        boolean filter = TextUtils.isEmpty(textViewTextChangeEvent.text());
                        return !filter;
                    }
                })
                .subscribe(new Action1<TextViewTextChangeEvent>() {
                    @Override
                    public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        ivSearchClear.setVisibility(View.VISIBLE);
                        mPresenter.searchCity(textViewTextChangeEvent.text().toString());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e("异常：" + throwable.getMessage());
                    }
                });
    }

    //隐藏输入框
    private void hideInputBox() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0); //强制隐藏键盘

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                killMyself();
            }
        }, 500);
    }


}
