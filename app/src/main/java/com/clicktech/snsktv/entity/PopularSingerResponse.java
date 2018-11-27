package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 * 发现页面 人气歌手-全部
 */

public class PopularSingerResponse extends BaseResponse {

    private List<PopularSingersBean> popularSingers;

    public List<PopularSingersBean> getPopularSingers() {
        return popularSingers;
    }

    public void setPopularSingers(List<PopularSingersBean> popularSingers) {
        this.popularSingers = popularSingers;
    }

    public static class PopularSingersBean {
        /**
         * id : 7
         * user_photo : 2017030615082329333081.jpg
         * user_district_dtl : 中国北京
         * is_lead : 0
         * user_nickname : 王志刚
         * user_sex : 1
         * listenCount : 156
         * giftNum : 1
         * user_district_id :
         * photo :
         * user_age : 6
         */

        private int id;
        private String user_photo;
        private String user_district_dtl;
        private int is_lead;
        private String user_nickname;
        private int user_sex;
        private int listenCount;
        private int giftNum;
        private String user_district_id;
        private String photo;
        private int user_age;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }

        public String getUser_district_dtl() {
            return user_district_dtl;
        }

        public void setUser_district_dtl(String user_district_dtl) {
            this.user_district_dtl = user_district_dtl;
        }

        public int getIs_lead() {
            return is_lead;
        }

        public void setIs_lead(int is_lead) {
            this.is_lead = is_lead;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public int getUser_sex() {
            return user_sex;
        }

        public void setUser_sex(int user_sex) {
            this.user_sex = user_sex;
        }

        public int getListenCount() {
            return listenCount;
        }

        public void setListenCount(int listenCount) {
            this.listenCount = listenCount;
        }

        public int getGiftNum() {
            return giftNum;
        }

        public void setGiftNum(int giftNum) {
            this.giftNum = giftNum;
        }

        public String getUser_district_id() {
            return user_district_id;
        }

        public void setUser_district_id(String user_district_id) {
            this.user_district_id = user_district_id;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getUser_age() {
            return user_age;
        }

        public void setUser_age(int user_age) {
            this.user_age = user_age;
        }
    }
}
