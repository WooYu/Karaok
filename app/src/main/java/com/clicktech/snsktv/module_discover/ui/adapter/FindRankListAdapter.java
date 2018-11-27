package com.clicktech.snsktv.module_discover.ui.adapter;

import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.entity.FriendSingerBean_Rank_U_Entity;
import com.clicktech.snsktv.module_discover.ui.holder.FindRankListHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 好友排行榜
 */

public class FindRankListAdapter extends DefaultAdapter<FriendSingerBean_Rank_U_Entity> {

    public FindRankListAdapter(List<FriendSingerBean_Rank_U_Entity> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<FriendSingerBean_Rank_U_Entity> getHolder(View v) {
        return new FindRankListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_friendssingers_ranklist;
    }

}
