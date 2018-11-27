package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */

public class SearchUserResponse extends BaseResponse {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        /**
         * id : 115
         * attention_type : 1
         * user_photo : 2017092819185906702845.png
         * user_nickname : 123
         * user_realname : name
         * user_no : fdo4yv7d3gv
         */

        private int id;
        private int attention_type;
        private String user_photo;
        private String user_nickname;
        private String user_realname;
        private String user_no;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAttention_type() {
            return attention_type;
        }

        public void setAttention_type(int attention_type) {
            this.attention_type = attention_type;
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

        public String getUser_realname() {
            return user_realname;
        }

        public void setUser_realname(String user_realname) {
            this.user_realname = user_realname;
        }

        public String getUser_no() {
            return user_no;
        }

        public void setUser_no(String user_no) {
            this.user_no = user_no;
        }
    }
}
