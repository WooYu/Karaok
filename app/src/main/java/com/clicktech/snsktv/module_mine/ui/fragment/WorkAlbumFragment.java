package com.clicktech.snsktv.module_mine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.WorkAlbumBean;
import com.clicktech.snsktv.module_mine.ui.activity.AlbumDetailActivity;

/**
 * Created by wy201 on 2017-12-21.
 */

public class WorkAlbumFragment extends Fragment implements View.OnClickListener {

    private ImageView ivPhoto;
    private WorkAlbumBean mAlbumBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_work_album, null);
        ivPhoto = (ImageView) rootView.findViewById(R.id.iv_photo);
        if (null != mAlbumBean) {
            KtvApplication.ktvApplication.getAppComponent().imageLoader()
                    .loadImage(getActivity(), GlideImageConfig.builder()
                            .errorPic(R.mipmap.def_square_large)
                            .placeholder(R.mipmap.def_square_large)
                            .url(mAlbumBean.getAlbum_image())
                            .imageView(ivPhoto)
                            .build());
        }

        ivPhoto.setOnClickListener(this);

        return rootView;
    }

    public void bindData(WorkAlbumBean bean) {
        this.mAlbumBean = bean;
    }

    @Override
    public void onClick(View v) {
        if (null == mAlbumBean) {
            return;
        }
        Intent intent = new Intent(getActivity(), AlbumDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", mAlbumBean.getUser_id());
        bundle.putString("albumid", mAlbumBean.getId());
        intent.putExtras(bundle);
        UiUtils.startActivity(intent);
    }
}
