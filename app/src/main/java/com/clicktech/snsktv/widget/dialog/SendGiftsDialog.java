package com.clicktech.snsktv.widget.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.PresentBean;
import com.clicktech.snsktv.module_mine.ui.adapter.GiftListAdapter;

import java.util.ArrayList;

/**
 * Created by wy201 on 2018-02-07.
 * 送礼的弹窗
 */

public class SendGiftsDialog extends DialogFragment implements View.OnClickListener {

    TextView tvKCoin;
    TextView tvBuy;
    RecyclerView rvGifts;
    LinearLayout llSendGifts;
    private OnEvent_ClickReqeust mListener;
    private ArrayList<PresentBean> mGiftList;
    private int wallet_coin;
    private int wallet_flower;
    private int type = 0;//0 选择的花，1 选择的礼物
    private PresentBean mSelectedObj;//选中的对象
    private GiftListAdapter mPresentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //去掉留白的标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        //接受关联activity传来的参数
        Bundle bundle = getArguments();
        if (null != bundle) {
            mGiftList = bundle.getParcelableArrayList("giftlist");
            wallet_coin = bundle.getInt("wallet_coin");
            wallet_flower = bundle.getInt("wallet_flower");
        }

        View view = inflater.inflate(R.layout.dialog_sendgifts, container);

        tvKCoin = (TextView) view.findViewById(R.id.tv_kcoin);
        tvBuy = (TextView) view.findViewById(R.id.tv_buy);
        rvGifts = (RecyclerView) view.findViewById(R.id.rv_gifts);
        llSendGifts = (LinearLayout) view.findViewById(R.id.ll_sendgifts);

        tvKCoin.setText(String.valueOf(wallet_coin) + getString(R.string.symbol_kcoin_unit));
        if (EmptyUtils.isNotEmpty(mGiftList)) {
            //添加鲜花到礼物列表头部
            PresentBean presentBean = new PresentBean();
            presentBean.setGift_price(wallet_flower);
            mGiftList.add(0, presentBean);

            GridLayoutManager layoutManager = new GridLayoutManager
                    (KtvApplication.getContext(), 2, GridLayoutManager.HORIZONTAL, false);
            rvGifts.setLayoutManager(layoutManager);
            mPresentAdapter = new GiftListAdapter(mGiftList);
            mPresentAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<PresentBean>() {
                @Override
                public void onItemClick(View view, PresentBean data, int position) {
                    type = (position == 0 ? position : 1);
                    mSelectedObj = data;
                }
            });
            rvGifts.setAdapter(mPresentAdapter);
            mSelectedObj = mGiftList.get(0);
        }
        tvBuy.setOnClickListener(this);
        llSendGifts.setOnClickListener(this);
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
    public void onClick(View v) {
        if (null == mListener) {
            return;
        }

        switch (v.getId()) {
            case R.id.tv_buy:
                onBuyKCoinClicked();
                break;
            case R.id.ll_sendgifts:
                onSendGiftsClicked();
                break;
        }

        dismiss();
    }

    public void setOnEvent_ClickRequest(OnEvent_ClickReqeust listener) {
        this.mListener = listener;
    }

    //购买k币
    private void onBuyKCoinClicked() {
        mListener.buyClicked();
    }

    //点击送礼
    private void onSendGiftsClicked() {
        int giftNum = 1;

        if (type == 1) {
            if (giftNum * mSelectedObj.getGift_price() > wallet_coin) {
                UiUtils.makeText(getString(R.string.error_not_sufficient_funds));
                return;
            }
            mListener.sendGiftsClicked(mSelectedObj.getId(), 1);
        } else {

            if (giftNum > wallet_flower) {
                UiUtils.makeText(getString(R.string.error_not_flower));
                return;
            }
            mListener.sendFlowersClicked(1);
        }
    }

    public interface OnEvent_ClickReqeust {
        void buyClicked();

        void sendFlowersClicked(int flowernum);

        void sendGiftsClicked(long giftid, int giftnum);
    }


}
