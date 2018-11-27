package com.clicktech.snsktv.module_mine.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/20.
 */

public class AddSingleMusicAdapter extends DefaultAdapter<SongInfoBean> {

    private List<Integer> mIndexListOfSelected = new ArrayList<>();
    private int mMaxNum;

    public AddSingleMusicAdapter(int maxnum, List<SongInfoBean> infos) {
        super(infos);
        this.mMaxNum = maxnum;
    }

    //更新选中的单曲
    public boolean updateTheSelectedSongs(int position) {
        if (EmptyUtils.isEmpty(mIndexListOfSelected)) {
            mIndexListOfSelected = new ArrayList<>();
        }

        if (mIndexListOfSelected.contains(position)) {
            mIndexListOfSelected.remove(mIndexListOfSelected.indexOf(position));
        } else {
            if (mIndexListOfSelected.size() >= mMaxNum) {
                UiUtils.makeText(KtvApplication.ktvApplication.getString(R.string.error_singlelimit));
                return false;
            }
            mIndexListOfSelected.add(position);
        }

        notifyDataSetChanged();
        return true;
    }

    //获取选中的歌曲下标
    public boolean containIndex(int position) {
        if (EmptyUtils.isEmpty(mIndexListOfSelected)) {
            return false;
        }

        return mIndexListOfSelected.contains(position);
    }

    @Override
    public BaseHolder<SongInfoBean> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_addsinglemusic;
    }

    public class MHolder extends BaseHolder<SongInfoBean> {

        @BindView(R.id.mi_avatar)
        MLImageView miAvatar;
        @BindView(R.id.tv_music_name)
        TextView tvMusicName;
        @BindView(R.id.tv_listen_num)
        TextView tvListenNum;
        @BindView(R.id.im_check)
        ImageView imCheck;

        public MHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(final SongInfoBean data, final int position) {
            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            tvMusicName.setText(data.getWorks_name());
            imCheck.setImageResource(mIndexListOfSelected.contains(position) ? R.mipmap.album_checked : R.mipmap.album_uncheck);
            tvListenNum.setText(data.getListen_count());
            ktvApplication.getAppComponent().imageLoader().loadImage(context,
                    GlideImageConfig.builder()
                            .url(data.getWorks_image())
                            .placeholder(R.mipmap.def_square_large)
                            .errorPic(R.mipmap.def_square_large)
                            .imageView(miAvatar)
                            .build());
        }


    }
}
