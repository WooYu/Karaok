package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 * 别的用户 个人中心  专辑
 */

public class OthersAlbumResponse extends BaseResponse {

    /**
     * attentionType : 0
     * albums : [{"works_sum":4,"comment_sum":0,"user_nickname":"赵玉帅","share_sum":0,"album_introduce":"很不错","id":3,"collect_sum":0,"gift_sum":0,"user_photo":"2017030615082329333081.jpg","user_id":1,"album_image":"","add_time":"2017-03-15 16:26:58","album_name":"hh","songNum":4},{"works_sum":3,"comment_sum":0,"user_nickname":"赵玉帅","share_sum":0,"album_introduce":"很不错","id":2,"collect_sum":0,"gift_sum":0,"user_photo":"2017030615082329333081.jpg","user_id":1,"album_image":"","add_time":"2016-12-26 16:26:06","album_name":"0","songNum":1},{"works_sum":0,"comment_sum":0,"user_nickname":"赵玉帅","share_sum":0,"album_introduce":"范德萨","id":1,"collect_sum":0,"gift_sum":0,"user_photo":"2017030615082329333081.jpg","user_id":1,"album_image":"111.jpg","add_time":"2016-07-14 14:37:04","album_name":"哈哈","songNum":6}]
     * fansNum : 5
     * user : {"id":1,"user_photo":"2017030615082329333081.jpg","user_district_dtl":"中国北京","is_lead":0,"user_nickname":"赵玉帅","user_sex":1,"user_district_id":"","photo":"2017031617174222448620.jpg","user_age":6}
     * attentionNum : 4
     * worksNum : 0
     * works : [{"flowerNum":5,"works_url":"Let.It.Go.mp4","works_image":"111.jpg","works_desc":null,"user_nickname":"赵玉帅","works_second":150,"works_name":"十二月的奇迹（日）","commentNum":3,"id":1,"works_level":"ss","user_photo":"2017030615082329333081.jpg","user_id":1,"add_time":"2016-12-27 11:27:42","coinNum":39,"listen_count":3,"works_type":1}]
     */

    private int attentionType;
    private int fansNum;
    private UserInfoBean user;
    private int attentionNum;
    private int worksNum;
    private List<AlbumBean> albums;
    private List<SongInfoBean> works;

    public int getAttentionType() {
        return attentionType;
    }

    public void setAttentionType(int attentionType) {
        this.attentionType = attentionType;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public int getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(int attentionNum) {
        this.attentionNum = attentionNum;
    }

    public int getWorksNum() {
        return worksNum;
    }

    public void setWorksNum(int worksNum) {
        this.worksNum = worksNum;
    }

    public List<AlbumBean> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumBean> albums) {
        this.albums = albums;
    }

    public List<SongInfoBean> getWorks() {
        return works;
    }

    public void setWorks(List<SongInfoBean> works) {
        this.works = works;
    }
}
