package com.clicktech.snsktv.entity;

import android.os.Parcel;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy201 on 2017-09-27.
 * 作品详情
 */

public class ArtworksDetailResponse extends BaseResponse {

    public static final Creator<ArtworksDetailResponse> CREATOR = new Creator<ArtworksDetailResponse>() {
        public ArtworksDetailResponse createFromParcel(Parcel source) {
            return new ArtworksDetailResponse(source);
        }

        public ArtworksDetailResponse[] newArray(int size) {
            return new ArtworksDetailResponse[size];
        }
    };
    /**
     * errCode : 0
     * msg : success
     * songWorksList : [{"accompany_size":2099,"accompany_url":"chuxue_banzhou.mp3","chorus_with":0,"grade_file_url":"","listen_count":472,"lyric_firstline":"第一场雪下起的午后","lyric_url":"chuxue.lrc","mv_url":"chuxue.mp3","original_url":"chuxue.mp3","phone_type":"iphone 5s","prelude_second":20,"singer_id":2,"song_id":2,"song_name_cn":"初雪（中）","song_name_jp":"初雪（日）","song_name_us":"初雪（英）","user_id":1,"user_nickname":"赵玉帅","user_photo":"2017030615082329333081.jpg","user_sex":1,"works_desc":"我刚唱了一首歌曲，快来听听吧","works_image":"sy_002.jpg","works_level":"ss","works_name":"初雪（日）","works_score":6000,"works_second":150,"works_size":3021,"works_type":1,"works_url":"chuxue.mp3"},{"accompany_size":3096,"accompany_url":"risinggirl.mp3","chorus_with":0,"grade_file_url":"","listen_count":399,"lyric_firstline":"君はきっと同じ毎日に","lyric_url":"risinggirl.lrc","mv_url":"risinggirl.mp3","original_url":"risinggirl.mp3","phone_type":"iphone 5s","prelude_second":20,"singer_id":1,"song_id":3,"song_name_cn":"rising girl（中）","song_name_jp":"rising girl（日）","song_name_us":"rising girl（英）","user_id":2,"user_nickname":"郭澎湃","user_photo":"2017030615082329333081.jpg","user_sex":1,"works_desc":"我刚唱了一首歌曲，快来听听吧","works_image":"sy_003.jpg","works_level":"ss","works_name":"rising girl（日）","works_score":6000,"works_second":150,"works_size":3021,"works_type":1,"works_url":"Let.It.Go.mp4"}]
     * total_coin_num : 39
     * total_flower_num : 0
     * worksCommentList : [{"add_time":"2017-03-15 11:35:09","comment_content":"谢谢","id":3,"parent_id":2,"replyUserId":1,"replyUserNickName":"赵玉帅","user_id":2,"user_nickname":"郭澎湃","user_photo":"2017030615082329333081.jpg","works_id":1},{"add_time":"2017-03-15 10:54:30","comment_content":"很不错","id":2,"parent_id":0,"user_id":1,"user_nickname":"赵玉帅","user_photo":"2017030615082329333081.jpg","works_id":1},{"add_time":"2016-12-23 14:58:20","comment_content":"真好听","id":1,"parent_id":0,"user_id":1,"user_nickname":"赵玉帅","user_photo":"2017030615082329333081.jpg","works_id":1}]
     * worksDetail : {"accompany_size":3170,"accompany_url":"02_001.mp3","chorus_with":0,"listen_count":957,"lyric_firstline":"I love you　今だけは悲しい歌聞きたくないよ","mv_url":"02_001.mp3","original_url":"02_001.mp3","phone_type":"iphone 5s","singer_id":6,"singer_name_cn":"尾崎豊","singer_name_jp":"尾崎豊","singer_name_us":"尾崎豊","song_id":8,"song_name_cn":"I LOVE YOU","song_name_jp":"I LOVE YOU","song_name_us":"I LOVE YOU","user_id":1,"user_nickname":"赵玉帅","user_photo":"2017030615082329333081.jpg","user_sex":1,"works_desc":"我刚唱了一首歌曲，快来听听吧","works_image":"sy_001.jpg","works_level":"ss","works_name":"I LOVE YOU","works_score":6000,"works_second":150,"works_size":3021,"works_type":1,"works_url":"02_001.mp3"}
     * worksGiftList : [{"coin_num":39,"flower_num":5,"flower_or_gift":2,"send_uid":1,"user_nickname":"赵玉帅","user_photo":"2017030615082329333081.jpg","works_id":1}]
     */

    private int total_coin_num;
    private int total_flower_num;
    private String attentionType;
    private String attentionTypeWorks;
    private SongInfoBean worksDetail;
    private List<SongInfoBean> songWorksList;
    private List<CommentInfoEntity> worksCommentList;
    private List<GiftInfoEntity> worksGiftList;

    public ArtworksDetailResponse() {
    }

    protected ArtworksDetailResponse(Parcel in) {
        super(in);
        this.total_coin_num = in.readInt();
        this.total_flower_num = in.readInt();
        this.attentionType = in.readString();
        this.attentionTypeWorks = in.readString();
        this.worksDetail = in.readParcelable(SongInfoBean.class.getClassLoader());
        this.songWorksList = in.createTypedArrayList(SongInfoBean.CREATOR);
        this.worksCommentList = new ArrayList<CommentInfoEntity>();
        in.readList(this.worksCommentList, List.class.getClassLoader());
        this.worksGiftList = new ArrayList<GiftInfoEntity>();
        in.readList(this.worksGiftList, List.class.getClassLoader());
    }

    public String getAttentionType() {
        return attentionType;
    }

    public void setAttentionType(String attentionType) {
        this.attentionType = attentionType;
    }

    public String getAttentionTypeWorks() {
        return attentionTypeWorks;
    }

    public void setAttentionTypeWorks(String attentionTypeWorks) {
        this.attentionTypeWorks = attentionTypeWorks;
    }

    public int getTotal_coin_num() {
        return total_coin_num;
    }

    public void setTotal_coin_num(int total_coin_num) {
        this.total_coin_num = total_coin_num;
    }

    public int getTotal_flower_num() {
        return total_flower_num;
    }

    public void setTotal_flower_num(int total_flower_num) {
        this.total_flower_num = total_flower_num;
    }

    public SongInfoBean getWorksDetail() {
        return worksDetail;
    }

    public void setWorksDetail(SongInfoBean worksDetail) {
        this.worksDetail = worksDetail;
    }

    public List<SongInfoBean> getSongWorksList() {
        return songWorksList;
    }

    public void setSongWorksList(List<SongInfoBean> songWorksList) {
        this.songWorksList = songWorksList;
    }

    public List<CommentInfoEntity> getWorksCommentList() {
        return worksCommentList;
    }

    public void setWorksCommentList(List<CommentInfoEntity> worksCommentList) {
        this.worksCommentList = worksCommentList;
    }

    public List<GiftInfoEntity> getWorksGiftList() {
        return worksGiftList;
    }

    public void setWorksGiftList(List<GiftInfoEntity> worksGiftList) {
        this.worksGiftList = worksGiftList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.total_coin_num);
        dest.writeInt(this.total_flower_num);
        dest.writeString(this.attentionType);
        dest.writeString(this.attentionTypeWorks);
        dest.writeParcelable(this.worksDetail, 0);
        dest.writeTypedList(songWorksList);
        dest.writeList(this.worksCommentList);
        dest.writeList(this.worksGiftList);
    }
}
