package com.clicktech.snsktv.module_home.ui.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.entity.SingerInfoEntity;
import com.clicktech.snsktv.util.StringHelper;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/11.
 */

public class CommonSingerHolder extends BaseHolder<SingerInfoEntity> {
    @BindView(R.id.tv_singername)
    TextView singerNameTv;

    public CommonSingerHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SingerInfoEntity data, int position) {
        ViewGroup.MarginLayoutParams lp =
                (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
        lp.rightMargin = 30;
        itemView.setLayoutParams(lp);
        singerNameTv.setText(StringHelper.getLau_With_J_U_C(
                data.getSinger_name_jp(), data.getSinger_name_us(), data.getSinger_name_cn()
        ));
    }

}
