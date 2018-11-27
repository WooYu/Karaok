package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.CommentInfoEntity;
import com.clicktech.snsktv.module_discover.ui.holder.PlayMiddleHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 * 播放界面评论
 */

public class PlayMiddleAdapter extends DefaultAdapter<CommentInfoEntity> {

    public PlayMiddleAdapter(List<CommentInfoEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<CommentInfoEntity> getHolder(View v) {
        return new PlayMiddleHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_songdetail_comment;
    }
}
