package com.clicktech.snsktv.entity;

/**
 * Created by wy201 on 2017-09-27.
 * 礼物信息
 */

public class GiftInfoEntity {

    /**
     * coin_num : 39
     * flower_num : 5
     * flower_or_gift : 2
     * send_uid : 1
     * user_nickname : 赵玉帅
     * user_photo : 2017030615082329333081.jpg
     * works_id : 1
     */

    private long coin_num;
    private int flower_num;
    private int flower_or_gift;
    private long send_uid;
    private String user_nickname;
    private String user_photo;
    private long works_id;

    public long getCoin_num() {
        return coin_num;
    }

    public void setCoin_num(long coin_num) {
        this.coin_num = coin_num;
    }

    public int getFlower_num() {
        return flower_num;
    }

    public void setFlower_num(int flower_num) {
        this.flower_num = flower_num;
    }

    public int getFlower_or_gift() {
        return flower_or_gift;
    }

    public void setFlower_or_gift(int flower_or_gift) {
        this.flower_or_gift = flower_or_gift;
    }

    public long getSend_uid() {
        return send_uid;
    }

    public void setSend_uid(long send_uid) {
        this.send_uid = send_uid;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public long getWorks_id() {
        return works_id;
    }

    public void setWorks_id(long works_id) {
        this.works_id = works_id;
    }
}
