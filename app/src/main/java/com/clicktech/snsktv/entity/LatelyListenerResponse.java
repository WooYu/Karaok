package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class LatelyListenerResponse extends BaseResponse {

    /**
     * currentList : [{"works_id":756,"user_photo":"2017120702423850344056.jpg","user_nickname":"长城","user_id":130,"add_time":"2018-02-05 03:33:02","works_name":"アイネクライネ","type":1},{"works_id":756,"user_photo":"2017120702423850344056.jpg","user_nickname":"长城","user_id":130,"add_time":"2018-02-05 03:33:01","works_name":"アイネクライネ","type":1},{"works_id":870,"user_photo":"2017120609431380988636.png","user_nickname":"丶淼","user_id":123,"add_time":"2018-02-04 09:24:23","works_name":"R. Y. U. S. E. I.　","type":1},{"works_id":871,"user_photo":"2017112705000147631101.JPEG","user_nickname":"woo","user_id":131,"add_time":"2018-02-03 13:50:01","works_name":"R. Y. U. S. E. I.　","type":1},{"works_id":869,"user_photo":"2017112705000147631101.JPEG","user_nickname":"woo","user_id":131,"add_time":"2018-02-03 12:55:13","works_name":"R. Y. U. S. E. I.　","type":1},{"works_id":869,"user_photo":"2017112705000147631101.JPEG","user_nickname":"woo","user_id":131,"add_time":"2018-02-03 12:52:24","works_name":"R. Y. U. S. E. I.　","type":1},{"works_id":869,"user_photo":"2017112705000147631101.JPEG","user_nickname":"woo","user_id":131,"add_time":"2018-02-03 12:49:55","works_name":"R. Y. U. S. E. I.　","type":1},{"works_id":868,"user_photo":"2017112705000147631101.JPEG","user_nickname":"woo","user_id":131,"add_time":"2018-02-03 12:34:19","works_name":"R. Y. U. S. E. I.　","type":1},{"works_id":865,"user_photo":"2017112705000147631101.JPEG","user_nickname":"woo","user_id":131,"add_time":"2018-02-03 09:54:55","works_name":"R. Y. U. S. E. I.　","type":1},{"works_id":864,"user_photo":"2017112705000147631101.JPEG","user_nickname":"woo","user_id":131,"add_time":"2018-02-03 09:49:38","works_name":"R. Y. U. S. E. I.　","type":1}]
     * totalCount : 377
     * dayCount : 2
     */

    private String totalCount;
    private String dayCount;
    private List<CurrentListBean> currentList;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getDayCount() {
        return dayCount;
    }

    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }

    public List<CurrentListBean> getCurrentList() {
        return currentList;
    }

    public void setCurrentList(List<CurrentListBean> currentList) {
        this.currentList = currentList;
    }

    public static class CurrentListBean {
        /**
         * works_id : 756
         * user_photo : 2017120702423850344056.jpg
         * user_nickname : 长城
         * user_id : 130
         * add_time : 2018-02-05 03:33:02
         * works_name : アイネクライネ
         * type : 1
         */

        private String works_id;
        private String user_photo;
        private String user_nickname;
        private String user_id;
        private String add_time;
        private String works_name;
        private int type;

        public String getWorks_id() {
            return works_id;
        }

        public void setWorks_id(String works_id) {
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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
