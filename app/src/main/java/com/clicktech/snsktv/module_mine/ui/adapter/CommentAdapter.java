package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.CommentEntity;
import com.clicktech.snsktv.module_mine.ui.holder.CommentItemHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/9.
 * 消息评论的Adapter
 */

public class CommentAdapter extends DefaultAdapter<CommentEntity> {


    public CommentAdapter(List<CommentEntity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<CommentEntity> getHolder(View v) {
        return new CommentItemHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_comment;
    }
}
