package com.clicktech.snsktv.module_enter.ui.fragment;


import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_enter.ui.adapter.PlayListAdapter;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wy201 on 2018-03-06.
 */

public class OtherSongFragment extends DialogFragment {
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    Unbinder unbinder;
    private ArrayList<SongInfoBean> mSongList;
    private PlayListAdapter mPlayListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //去掉留白的标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        //接受关联activity传来的参数
        Bundle bundle = getArguments();
        if (null != bundle) {
            mSongList = bundle.getParcelableArrayList("songlist");
        }

        View view = inflater.inflate(R.layout.dialog_othersong, container);
        unbinder = ButterKnife.bind(this, view);

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPlayListAdapter = new PlayListAdapter(mSongList);
        mPlayListAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<SongInfoBean>() {
            @Override
            public void onItemClick(View view, SongInfoBean data, int position) {
                Bundle toggleBundle = new Bundle();
                toggleBundle.putInt("position", position);
                Message message = new Message();
                message.setData(toggleBundle);
                EventBus.getDefault().post(message, EventBusTag.PLAYLIST_TOGGLESONG);
            }
        });
        recyclerview.setAdapter(mPlayListAdapter);
        int size = EmptyUtils.isNotEmpty(mSongList) ? mSongList.size() : 0;
        tvSum.setText(String.format(getString(R.string.format_playlist_sum), String.valueOf(size)));

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
    }

    @OnClick(R.id.iv_clearall)
    public void onIvClearallClicked() {
        if (EmptyUtils.isEmpty(mSongList)) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        Message message = new Message();
        message.setData(bundle);
        EventBus.getDefault().post(message, EventBusTag.PLAYLIST_DELSONG);
        dismiss();
    }

    @OnClick(R.id.tv_close)
    public void onTvCloseClicked() {
        dismiss();
    }

    //删除歌曲后更新集合
    public void updateSongList(ArrayList<SongInfoBean> list) {
        if (EmptyUtils.isEmpty(list)) {
            dismiss();
        }
        mSongList = list;
        mPlayListAdapter.setmInfos(mSongList);
        int size = EmptyUtils.isNotEmpty(mSongList) ? mSongList.size() : 0;
        tvSum.setText(String.format(getString(R.string.format_playlist_sum), String.valueOf(size)));
    }

}
