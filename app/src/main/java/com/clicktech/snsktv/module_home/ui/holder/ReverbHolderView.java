package com.clicktech.snsktv.module_home.ui.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.KnifeUtil;
import com.clicktech.snsktv.entity.SoundReverbEntity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/10.
 */

public class ReverbHolderView implements Holder<SoundReverbEntity> {
    @BindView(R.id.iv_reverb)
    ImageView img;
    @BindView(R.id.tv_reverbname)
    TextView tvReverbName;
    @BindView(R.id.tv_reverbdesc)
    TextView tvReverbDesc;

    @Override
    public View createView(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.adapter_reverbeffect, null);
        KnifeUtil.bindTarget(this, contentView);
        return contentView;
    }

    @Override
    public void UpdateUI(Context context, int position, SoundReverbEntity data) {
        tvReverbDesc.setText(data.getEffectDesc());
        tvReverbName.setText(data.getEffectName());
        img.setImageResource(data.getResid());
    }
}
