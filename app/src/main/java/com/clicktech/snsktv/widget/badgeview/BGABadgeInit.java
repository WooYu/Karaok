package com.clicktech.snsktv.widget.badgeview;

import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.bingoogolapple.badgeview.annotation.BGABadge;


@BGABadge({
        ImageView.class, // 对应 cn.bingoogolapple.badgeview.BGABadgeImageView，不想用这个类的话就删了这一行
        TextView.class, // 对应 cn.bingoogolapple.badgeview.BGABadgeFloatingTextView，不想用这个类的话就删了这一行
        RadioButton.class, // 对应 cn.bingoogolapple.badgeview.BGABadgeRadioButton，不想用这个类的话就删了这一行
})
public class BGABadgeInit {

}
