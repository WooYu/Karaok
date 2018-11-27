package com.clicktech.snsktv.module_mine.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseHolder;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.MineGiftListResponse;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;
import com.clicktech.snsktv.widget.dialog.CommentListener;
import com.clicktech.snsktv.widget.dialog.CommentPopupWindow;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MineGiftListAdapter extends DefaultAdapter<MineGiftListResponse.GiftListBean> {

    private GiftListListener mGiftListener;
    private FragmentActivity mContext;


    public MineGiftListAdapter(FragmentActivity context, List<MineGiftListResponse.GiftListBean> infos) {
        super(infos);
        this.mContext = context;
    }

    public void setGiftListListener(GiftListListener listListener) {
        this.mGiftListener = listListener;
    }

    @Override
    public BaseHolder<MineGiftListResponse.GiftListBean> getHolder(View v) {
        return new MHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_giftmsg;
    }

    public interface GiftListListener {
        void onClickReply(int position, String content);
    }

    class MHolder extends BaseHolder<MineGiftListResponse.GiftListBean> {

        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.nickname)
        TextView nickname;
        @BindView(R.id.detail)
        TextView detail;
        @BindView(R.id.flowernum)
        TextView flowernum;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.reply)
        TextView reply;

        private CommentPopupWindow mCommentPop;

        public MHolder(View itemView) {
            super(itemView);
            initCommentPop();
        }

        @Override
        public void setData(MineGiftListResponse.GiftListBean data, int position) {

            KtvApplication ktvApplication = (KtvApplication) context.getApplicationContext();
            ktvApplication.getAppComponent().imageLoader()
                    .loadImage(context, GlideImageConfig.builder()
                            .url(data.getUser_photo())
                            .errorPic(R.mipmap.def_avatar_round)
                            .transformation(new CircleWithBorderTransformation(context))
                            .placeholder(R.mipmap.def_avatar_round)
                            .imageView(avatar)
                            .build());

            nickname.setText(data.getUser_nickname());
            flowernum.setText(String.format(context.getString(R.string.format_numofflowers), data.getGift_num()));
            time.setText(data.getAdd_time());
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (null != mCommentPop) {
                        mCommentPop.showReveal();
                    }

                }
            });

        }

        private void initCommentPop() {
            mCommentPop = new CommentPopupWindow(mContext, new CommentListener() {
                @Override
                public void sendComment(String content) {
                    Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
                    if (null != mGiftListener) {
                        mGiftListener.onClickReply(getLayoutPosition(), content);
                    }
                }
            });
        }

    }


}
