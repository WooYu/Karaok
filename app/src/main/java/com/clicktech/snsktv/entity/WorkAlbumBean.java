package com.clicktech.snsktv.entity;

/**
 * Created by Administrator on 2017/1/19.
 * 我的--关注--专辑
 */

public class WorkAlbumBean {

    /**
     * 参数名	类型	说明
     * id	long	专辑id
     * works_sum	int	包含歌曲数
     * comment_sum	string	评论数
     * songList	string	包含歌曲
     * share_sum	int	分享数
     * user_id	long	用户id
     * user_photo	string	用户头像
     * user_nickname	string	用户昵称
     * album_introduce	string	专辑介绍
     * collect_sum	int	收藏数
     * add_time	string	添加时间
     * album_name	string	专辑名称
     * gift_sum	int	礼物数
     * album_image	string	专辑图片
     * user_sex
     */
    private String works_sum;
    private String comment_sum;
    private String songList;
    private String user_nickname;
    private String share_sum;
    private String album_introduce;
    private String id;
    private String collect_sum;
    private String gift_sum;
    private String user_photo;
    private String user_id;
    private String album_image;
    private String add_time;
    private String album_name;
    private int user_sex;


    public int getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public String getWorks_sum() {
        return works_sum;
    }

    public void setWorks_sum(String works_sum) {
        this.works_sum = works_sum;
    }

    public String getComment_sum() {
        return comment_sum;
    }

    public void setComment_sum(String comment_sum) {
        this.comment_sum = comment_sum;
    }

    public String getSongList() {
        return songList;
    }

    public void setSongList(String songList) {
        this.songList = songList;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getShare_sum() {
        return share_sum;
    }

    public void setShare_sum(String share_sum) {
        this.share_sum = share_sum;
    }

    public String getAlbum_introduce() {
        return album_introduce;
    }

    public void setAlbum_introduce(String album_introduce) {
        this.album_introduce = album_introduce;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollect_sum() {
        return collect_sum;
    }

    public void setCollect_sum(String collect_sum) {
        this.collect_sum = collect_sum;
    }

    public String getGift_sum() {
        return gift_sum;
    }

    public void setGift_sum(String gift_sum) {
        this.gift_sum = gift_sum;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAlbum_image() {
        return album_image;
    }

    public void setAlbum_image(String album_image) {
        this.album_image = album_image;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }
}
