package com.clicktech.snsktv.module_enter.ui.adapter;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.entity.SongInfoBean;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wy201 on 2018-03-06.
 */

public class PlayListAdapter extends DefaultAdapter<SongInfoBean> {
    public PlayListAdapter(List<SongInfoBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new PlayListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_playlist_song;
    }

    class PlayListHolder extends BaseHolder<SongInfoBean> {
        @BindView(R.id.tv_songname)
        TextView tvSongname;
        @BindView(R.id.tv_singername)
        TextView tvSingername;
        @BindView(R.id.iv_status)
        ImageView ivStatus;

        public PlayListHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.iv_delete)
        public void onIvDeleteClicked() {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            bundle.putInt("position", getAdapterPosition());
            Message message = new Message();
            message.setData(bundle);
            EventBus.getDefault().post(message, EventBusTag.PLAYLIST_DELSONG);
        }

        @Override
        public void setData(SongInfoBean data, int position) {
            tvSongname.setText(data.getWorks_name());
            tvSingername.setText(data.getUser_nickname());
            if (data.isbPlayStatus()) {
                ivStatus.setVisibility(View.VISIBLE);
            } else {
                ivStatus.setVisibility(View.GONE);
            }
        }
    }
}
