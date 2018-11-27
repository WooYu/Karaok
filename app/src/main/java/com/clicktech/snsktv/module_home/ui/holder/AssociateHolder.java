package com.clicktech.snsktv.module_home.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.util.StringHelper;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/12.
 */

public class AssociateHolder extends BaseHolder<SongInfoBean> {


    @BindView(R.id.name)
    TextView name;

    public AssociateHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SongInfoBean data, int position) {
        name.setText(StringHelper.getLau_With_J_U_C(data.getSinger_name_jp(),
                data.getSinger_name_us(), data.getSinger_name_cn()));
    }

}
