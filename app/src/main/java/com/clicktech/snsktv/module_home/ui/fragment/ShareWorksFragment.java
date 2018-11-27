package com.clicktech.snsktv.module_home.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.ShareBean;
import com.clicktech.snsktv.module_home.ui.activity.OtherShareActivity;
import com.clicktech.snsktv.module_home.ui.adapter.ShareWorkAdapter;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.line.Line;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import timber.log.Timber;

/**
 * Created by wy201 on 2018-03-09.
 */

public class ShareWorksFragment extends DialogFragment implements PlatformActionListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ll_other)
    LinearLayout llOther;
    Unbinder unbinder;
    private int RequestCode_OtherShare = 0x0013;
    private String[] shareType_jpenviro;
    private int[] shareImg_jpenviro = new int[]{R.mipmap.share_facebook, R.mipmap.share_twitter,
            R.mipmap.share_line};
    private String[] shareType_nojpenviro;
    private int[] shareImg_nojpenviro = new int[]{R.mipmap.share_wx, R.mipmap.share_circle,
            R.mipmap.share_qq, R.mipmap.share_sina};
    private ArrayList<ShareBean> mShareList;
    private ShareWorkAdapter mShareAdapter;
    private String mWorksName;//作品名称
    private String mWorksDesc;//作品描述
    private String mWorksUrl;//作品链接
    private String mWorksPhoto;//作品图片

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //去掉留白的标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_share, container);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);//注册到事件主线
        initArguments();
        initRecyclerView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置dialog宽度
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    private void initArguments() {
        Bundle bundle = getArguments();
        mWorksName = bundle.getString("worksname");
        mWorksDesc = bundle.getString("worksdesc");
        mWorksUrl = bundle.getString("worksurl");
        mWorksPhoto = bundle.getString("worksimg");
    }

    private void initRecyclerView() {
        shareType_jpenviro = new String[]{
                getString(R.string.shares_supporttypes_facebook),
                getString(R.string.shares_supporttypes_twitter),
                getString(R.string.shares_supporttypes_line)};
        shareType_nojpenviro = new String[]{getString(R.string.shares_supporttypes_wechat),
                getString(R.string.shares_supporttypes_friend),
                getString(R.string.shares_supporttypes_qq),
                getString(R.string.shares_supporttypes_sina)};

        mShareAdapter = new ShareWorkAdapter(mShareList);
        mShareAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<ShareBean>() {
            @Override
            public void onItemClick(View view, ShareBean data, int position) {
                shareByPlatformName(data.getPlatformName());
            }
        });
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerview.setAdapter(mShareAdapter);

        initShareMode();
    }

    private void initShareMode() {
        if (null == mShareList) {
            mShareList = new ArrayList<>();
        } else {
            mShareList.clear();
        }

        String language = KtvApplication.ktvApplication.getLocaleCode();
        for (int i = 0; i < shareType_jpenviro.length; i++) {
            if (language.equals(getString(R.string.language_japan))
                    || DataHelper.getBooleanSF(getContext(), shareType_jpenviro[i])) {
                ShareBean shareBean = new ShareBean();
                shareBean.setPlatformName(shareType_jpenviro[i]);
                shareBean.setPlatformImg(shareImg_jpenviro[i]);
                shareBean.setLanguage(getString(R.string.language_japan));
                mShareList.add(shareBean);
            }

        }

        for (int i = 0; i < shareType_nojpenviro.length; i++) {
            if (language.equals(getString(R.string.language_china))
                    || language.equals(getString(R.string.language_english))
                    || DataHelper.getBooleanSF(getContext(), shareType_nojpenviro[i])) {
                ShareBean shareBean = new ShareBean();
                shareBean.setPlatformName(shareType_nojpenviro[i]);
                shareBean.setPlatformImg(shareImg_nojpenviro[i]);
                shareBean.setLanguage(getString(R.string.language_china));
                mShareList.add(shareBean);
            }
        }

        mShareAdapter.setmInfos(mShareList);
    }


    @OnClick(R.id.ll_other)
    void onLlOtherClicked() {
        Intent intent = new Intent(getContext(), OtherShareActivity.class);
        startActivityForResult(intent, RequestCode_OtherShare);
    }

    @OnClick(R.id.tv_cancel)
    void onTvCancelClicked() {
        dismiss();
    }

    @Subscriber(tag = EventBusTag.SHARE_MODECHANGE, mode = ThreadMode.MAIN)
    public void eventShareAddMode(Message message) {
        initShareMode();
    }

    private void shareByPlatformName(String name) {
        if (name.equals(getString(R.string.shares_supporttypes_facebook))) {
            share2FaceBook();
        } else if (name.equals(getString(R.string.shares_supporttypes_twitter))) {
            share2Twitter();
        } else if (name.equals(getString(R.string.shares_supporttypes_line))) {
            share2Line();
        } else if (name.equals(getString(R.string.shares_supporttypes_yahoo))) {

        } else if (name.equals(getString(R.string.shares_supporttypes_wechat))) {
            share2WeChat();
        } else if (name.equals(getString(R.string.shares_supporttypes_friend))) {
            share2WeChatFriend();
        } else if (name.equals(getString(R.string.shares_supporttypes_qq))) {
            share2QQ();
        } else if (name.equals(getString(R.string.shares_supporttypes_sina))) {
            share2Sina();
        }

        dismiss();

    }

//    //一键分享
//    private void oneKeyShare(String platform) {
//        String name = "";
//
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        oks.setPlatform(platform);
//
//        // title标题，微信、QQ和QQ空间等平台使用
//        oks.setTitle("我是分享文本，啦啦啦~http://www.baidu.com/");
//        // titleUrl QQ和QQ空间跳转链接
//        oks.setTitleUrl("https://www.cnblogs.com");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("Text");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/storage/emulated/0/TeSt.jpg");//确保SDcard下面存在此张图片
//        // url在微信、微博，Facebook等平台中使用
//        oks.setUrl("http://wiki.mob.com");
//        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("Comment");
//        // 启动分享GUI
//        oks.show(KtvApplication.ktvApplication);
//    }

    private void share2Sina() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setText(mWorksDesc + "~" + mWorksUrl);
        sp.setImageUrl(mWorksPhoto);

        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(this); // 设置
        // 分享事件回调
        weibo.share(sp);
    }

    private void share2WeChat() {

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
        sp.setTitle(mWorksName);  //分享标题
        sp.setText(mWorksDesc);   //分享文本(朋友圈不显示）
        sp.setImageUrl(mWorksPhoto);//网络图片rul
        sp.setUrl(mWorksUrl);   //网友点进链接后，可以看到分享的详情

        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (!wechat.isClientValid()) {
            UiUtils.SnackbarText(getString(R.string.tip_share_clientvalid));
            return;
        }
        wechat.setPlatformActionListener(this); // 设置分享事件回调
        wechat.share(sp);
    }

    private void share2WeChatFriend() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
        sp.setTitle(mWorksName);  //分享标题
        sp.setImageUrl(mWorksPhoto);//网络图片rul
        sp.setUrl(mWorksUrl);   //网友点进链接后，可以看到分享的详情

        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
        if (!wechatMoments.isClientValid()) {
            UiUtils.SnackbarText(getString(R.string.tip_share_clientvalid));
            return;
        }
        wechatMoments.setPlatformActionListener(this); // 设置分享事件回调
        wechatMoments.share(sp);
    }

    private void share2QQ() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(mWorksName);
        sp.setText(mWorksDesc);
        sp.setImageUrl(mWorksPhoto);//网络图片rul
        sp.setTitleUrl(mWorksUrl);  //网友点进链接后，可以看到分享的详情

        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(this); // 设置分享事件回调
        qq.share(sp);
    }

    private void share2FaceBook() {
        Facebook.ShareParams sp = new Facebook.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(mWorksDesc + "~" + mWorksUrl);
        sp.setImageUrl(mWorksPhoto);//网络图片rul
        sp.setUrl(mWorksUrl);  //网友点进链接后，可以看到分享的详情

        Platform facebook = ShareSDK.getPlatform(Facebook.NAME);
        facebook.setPlatformActionListener(this); // 设置分享事件回调
        facebook.share(sp);
    }

    private void share2Twitter() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setText(mWorksDesc + "~" + mWorksUrl);
        sp.setImageUrl(mWorksPhoto);//网络图片rul

        Platform twitter = ShareSDK.getPlatform(Twitter.NAME);
        twitter.setPlatformActionListener(this); // 设置分享事件回调
        twitter.share(sp);
    }

    private void share2Line() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setText(mWorksDesc + "~" + mWorksUrl);
        sp.setImageUrl(mWorksPhoto);//网络图片rul

        Platform line = ShareSDK.getPlatform(Line.NAME);
        line.setPlatformActionListener(this); // 设置分享事件回调
        line.share(sp);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Timber.d(getString(R.string.tip_share_success));
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Timber.e(throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Timber.d(getString(R.string.tip_share_cancel));
    }

}
