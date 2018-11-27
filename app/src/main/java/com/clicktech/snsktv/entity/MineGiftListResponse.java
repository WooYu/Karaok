package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MineGiftListResponse extends BaseResponse {

    private List<GiftListBean> giftList;

    public List<GiftListBean> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<GiftListBean> giftList) {
        this.giftList = giftList;
    }

    public static class GiftListBean {
        /**
         * gift_num : 5
         * works_id : 1
         * user_photo : 2017030615082329333081.jpg
         * user_nickname : 赵玉帅
         * user_id : 1
         * add_time : 2017-03-20 15:06:05
         * works_name : 十二月的奇迹（日）
         * gift_name : 鲜花
         */

        private String gift_num;
        private int works_id;
        private String user_photo;
        private String user_nickname;
        private int user_id;
        private String add_time;
        private String works_name;
        private String gift_name;

        public String getGift_num() {
            return gift_num;
        }

        public void setGift_num(String gift_num) {
            this.gift_num = gift_num;
        }

        public int getWorks_id() {
            return works_id;
        }

        public void setWorks_id(int works_id) {
            this.works_id = works_id;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getWorks_name() {
            return works_name;
        }

        public void setWorks_name(String works_name) {
            this.works_name = works_name;
        }

        public String getGift_name() {
            return gift_name;
        }

        public void setGift_name(String gift_name) {
            this.gift_name = gift_name;
        }
    }
}
