package com.clicktech.snsktv.module_mine.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.autolayout.AutoRadioGroup;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.CommentEntity;
import com.clicktech.snsktv.module_home.ui.fragment.CommentFragment;
import com.clicktech.snsktv.module_mine.contract.MessageContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerMessageComponent;
import com.clicktech.snsktv.module_mine.inject.module.MessageModule;
import com.clicktech.snsktv.module_mine.presenter.MessagePresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.CommentAdapter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeRadioButton;
import cn.bingoogolapple.badgeview.BGABadgeTextView;

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
 * Created by Administrator on 2017/2/4.
 * 通知
 */

public class MessageActivity extends WEActivity<MessagePresenter> implements
        MessageContract.View, RadioGroup.OnCheckedChangeListener, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.autorg_comment)
    AutoRadioGroup autorgComment;
    @BindView(R.id.xrv_review)
    RecyclerView xrvReview;

    //BGA开头的控件只能findViewById
    BGABadgeTextView tvAudience;
    BGABadgeTextView tvGift;
    BGABadgeTextView tvSysmsg;
    BGABadgeRadioButton rbConcern;
    BGABadgeRadioButton rbIndifferent;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMessageComponent
                .builder()
                .appComponent(appComponent)
                .messageModule(new MessageModule(this)) //请将MessageModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_message, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);
        tvAudience = findViewById(R.id.tv_audience);
        tvGift = findViewById(R.id.tv_gift);
        tvSysmsg = findViewById(R.id.tv_sysmsg);
        rbConcern = findViewById(R.id.rb_concern);
        rbIndifferent = findViewById(R.id.rb_indifferent);

        autorgComment.setOnCheckedChangeListener(this);
        mPresenter.switchCommentsList(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init_msgtip();
    }

    @Override
    protected void onPause() {
        super.onPause();
        int totalNum = DataHelper.getIntergerSF(this, ConstantConfig.PUSHMESSAGE_RECENTLYAUDIENCE)
                + DataHelper.getIntergerSF(this, ConstantConfig.PUSHMESSAGE_GIFT)
                + DataHelper.getIntergerSF(this, ConstantConfig.PUSHMESSAGE_SYSTEM)
                + DataHelper.getIntergerSF(this, ConstantConfig.PUSHMESSAGE_COMMENT_NOTFOLLOWED)
                + DataHelper.getIntergerSF(this, ConstantConfig.PUSHMESSAGE_COMMENT_FOLLOWED);
        DataHelper.setIntergerSF(this, ConstantConfig.PUSHMESSAGE_TOTAL, totalNum);
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
    public void setCommentList(CommentAdapter adapter) {
        xrvReview.setLayoutManager(new LinearLayoutManager(mWeApplication));
        xrvReview.setAdapter(adapter);

        adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<CommentEntity>() {
            @Override
            public void onItemClick(View view, CommentEntity data, int position) {
                switch (view.getId()) {
                    case R.id.reply_btn:
                        showCommentPop(data);
                        break;
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_concern://关注人的评论
                DataHelper.setIntergerSF(this, ConstantConfig.PUSHMESSAGE_COMMENT_FOLLOWED, 0);
                rbConcern.hiddenBadge();
                mPresenter.switchCommentsList(0);
                break;
            case R.id.rb_indifferent://未关注人的评论
                DataHelper.setIntergerSF(this, ConstantConfig.PUSHMESSAGE_COMMENT_NOTFOLLOWED, 0);
                rbIndifferent.hiddenBadge();
                mPresenter.switchCommentsList(1);
                break;
        }
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
    }

    @OnClick({R.id.tv_audience, R.id.tv_gift, R.id.tv_sysmsg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_audience:
                DataHelper.setIntergerSF(this, ConstantConfig.PUSHMESSAGE_RECENTLYAUDIENCE, 0);
                UiUtils.startActivity(this, LatelyListenerActivity.class);
                break;
            case R.id.tv_gift:
                DataHelper.setIntergerSF(this, ConstantConfig.PUSHMESSAGE_GIFT, 0);
                UiUtils.startActivity(this, GiftListActivity.class);
                break;
            case R.id.tv_sysmsg:
                DataHelper.setIntergerSF(this, ConstantConfig.PUSHMESSAGE_SYSTEM, 0);
                UiUtils.startActivity(this, SystemMsgActivity.class);
                break;
        }
    }

    private void showCommentPop(final CommentEntity data) {
        CommentFragment commentFragment = new CommentFragment();
        commentFragment.setCommentInputCallBack(new CommentFragment.CommentInputCallBack() {
            @Override
            public void sendComment(String content) {
                mPresenter.reply(data, content);
            }
        });
        commentFragment.show(getSupportFragmentManager(), "com.clicktech.snsktv.module_home.ui.fragment.CommentFragment");
    }

    //初始化消息提示
    private void init_msgtip() {
        int numOfAudience = DataHelper.getIntergerSF(this, ConstantConfig.PUSHMESSAGE_RECENTLYAUDIENCE);
        if (numOfAudience > 0) {
            tvAudience.showCirclePointBadge();
        } else {
            tvAudience.hiddenBadge();
        }

        int numOfGift = DataHelper.getIntergerSF(this, ConstantConfig.PUSHMESSAGE_GIFT);
        if (numOfGift > 0) {
            tvGift.showCirclePointBadge();
        } else {
            tvGift.hiddenBadge();
        }

        int numOfSystem = DataHelper.getIntergerSF(this, ConstantConfig.PUSHMESSAGE_SYSTEM);
        if (numOfSystem > 0) {
            tvSysmsg.showCirclePointBadge();
        } else {
            tvSysmsg.hiddenBadge();
        }

        int numOfCommentFollowed = DataHelper.getIntergerSF(this, ConstantConfig.PUSHMESSAGE_COMMENT_FOLLOWED);
        if (numOfCommentFollowed > 0) {
            rbConcern.showCirclePointBadge();
        } else {
            rbConcern.hiddenBadge();
        }

        int numOfCommentNoFollowed = DataHelper.getIntergerSF(this, ConstantConfig.PUSHMESSAGE_COMMENT_NOTFOLLOWED);
        if (numOfCommentNoFollowed > 0) {
            rbIndifferent.showCirclePointBadge();
        } else {
            rbIndifferent.hiddenBadge();
        }

    }
}
