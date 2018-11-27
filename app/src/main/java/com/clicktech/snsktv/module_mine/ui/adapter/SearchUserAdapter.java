package com.clicktech.snsktv.module_mine.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SearchUserResponse;
import com.clicktech.snsktv.module_discover.ui.activity.SingerIntroActivity;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/29.
 */

public class SearchUserAdapter extends DefaultAdapter<SearchUserResponse.ListBean> {


    public SearchUserAdapter(List<SearchUserResponse.ListBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<SearchUserResponse.ListBean> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dapter_sreachuser;
    }


    class MHolder extends BaseHolder<SearchUserResponse.ListBean> {
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.name)
        TextView name;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(final SearchUserResponse.ListBean data, int position) {
            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            name.setText(data.getUser_nickname());

            ktvApplication.getAppComponent().imageLoader().loadImage(context, GlideImageConfig.builder()
                    .url(data.getUser_photo())
                    .placeholder(R.mipmap.def_avatar_round)
                    .errorPic(R.mipmap.def_avatar_round)
                    .transformation(new CircleWithBorderTransformation(context))
                    .imageView(avatar)
                    .build());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SingerIntroActivity.class);
                    intent.putExtra("userid", String.valueOf(data.getId()));
                    UiUtils.startActivity(intent);
                }
            });

        }
    }
}
