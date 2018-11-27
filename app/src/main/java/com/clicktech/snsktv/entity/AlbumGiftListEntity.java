package com.clicktech.snsktv.entity;

/**
 * Created by Administrator on 2017/6/19.
 */

public class AlbumGiftListEntity {

    /**
     * flower_num : 5
     * send_uid : 1
     * coin_num : 39
     * user_nickname : 赵玉帅
     * works_id : 1
     * flower_or_gift : 2
     * user_photo : 2017030615082329333081.jpg
     */

    private int flower_num;
    private int send_uid;
    private int coin_num;
    private String user_nickname;
    private int works_id;
    private int flower_or_gift;
    private String user_photo;

    public int getFlower_num() {
        return flower_num;
    }

    public void setFlower_num(int flower_num) {
        this.flower_num = flower_num;
    }

    public int getSend_uid() {
        return send_uid;
    }

    public void setSend_uid(int send_uid) {
        this.send_uid = send_uid;
    }

    public int getCoin_num() {
        return coin_num;
    }

    public void setCoin_num(int coin_num) {
        this.coin_num = coin_num;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public int getWorks_id() {
        return works_id;
    }

    public void setWorks_id(int works_id) {
        this.works_id = works_id;
    }

    public int getFlower_or_gift() {
        return flower_or_gift;
    }

    public void setFlower_or_gift(int flower_or_gift) {
        this.flower_or_gift = flower_or_gift;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }
}
