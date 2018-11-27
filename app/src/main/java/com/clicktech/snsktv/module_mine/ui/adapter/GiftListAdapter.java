package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.PresentBean;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/20.
 */

public class GiftListAdapter extends DefaultAdapter<PresentBean> {
    int curClickPosition = 0;
    int lastClickPosition = -1;

    public GiftListAdapter(List<PresentBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<PresentBean> getHolder(View v) {
        return new GiftListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_gift_list;
    }

    class GiftListHolder extends BaseHolder<PresentBean> {

        @BindView(R.id.im_gift_photo)
        ImageView photo;
        @BindView(R.id.tv_gift_price)
        TextView giftPrice;
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        public GiftListHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(final PresentBean data, final int position) {

            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            if (curClickPosition == position) {
                if (position % 2 == 0) {
                    llItem.setBackgroundResource(R.drawable.shape_send_gift_bg2);
                } else {
                    llItem.setBackgroundResource(R.drawable.shape_send_gift_bg);
                }
            } else {
                if (position % 2 == 0) {
                    llItem.setBackgroundResource(R.drawable.shape_send_gift_bg2_normal);
                } else {
                    llItem.setBackgroundResource(R.drawable.shape_send_gift_bg_normal);
                }
            }

            llItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    lastClickPosition = curClickPosition;
                    curClickPosition = getLayoutPosition();
                    notifyItemChanged(lastClickPosition);
                    notifyItemChanged(curClickPosition);
                    if (null != mOnItemClickListener) {
                        mOnItemClickListener.onItemClick(v, data, position);
                    }
                }
            });

            if (EmptyUtils.isEmpty(data.getGift_image())) {
                photo.setImageResource(R.mipmap.gift_flowers);
            } else {
                ktvApplication.getAppComponent().imageLoader().loadImage(context,
                        GlideImageConfig.builder()
                                .url(data.getGift_image())
                                .errorPic(R.mipmap.gift_flowers)
                                .placeholder(R.mipmap.def_square_large)
                                .imageView(photo)
                                .build());
            }

            if (0 == position) {
                giftPrice.setText(String.valueOf(data.getGift_price()));
            } else {
                giftPrice.setText(data.getGift_price() +
                        context.getString(R.string.symbol_kcoin_unit));
            }

        }


    }


}
