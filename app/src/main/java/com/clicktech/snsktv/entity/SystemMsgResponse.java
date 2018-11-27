package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */

public class SystemMsgResponse extends BaseResponse {
    private List<MessageListBean> messageList;

    public List<MessageListBean> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageListBean> messageList) {
        this.messageList = messageList;
    }

    public static class MessageListBean {
        /**
         * is_read : 0
         * message_content : 今天登录送了您新的鲜花和K币。
         * update_time : 2017-05-15 13:53:31
         * user_id : 23
         * message_title : 登录提示
         * id : 3
         */

        private int is_read;
        private String message_content;
        private String update_time;
        private int user_id;
        private String message_title;
        private int id;

        public int getIs_read() {
            return is_read;
        }

        public void setIs_read(int is_read) {
            this.is_read = is_read;
        }

        public String getMessage_content() {
            return message_content;
        }

        public void setMessage_content(String message_content) {
            this.message_content = message_content;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getMessage_title() {
            return message_title;
        }

        public void setMessage_title(String message_title) {
            this.message_title = message_title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
