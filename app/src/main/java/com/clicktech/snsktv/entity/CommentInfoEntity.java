package com.clicktech.snsktv.entity;

import java.io.Serializable;

/**
 * Created by wy201 on 2017-09-27.
 * 评论信息
 */

public class CommentInfoEntity implements Serializable {


    /**
     * add_time : 2017-03-15 11:35:09
     * comment_content : 谢谢
     * id : 3
     * parent_id : 2
     * replyUserId : 1
     * replyUserNickName : 赵玉帅
     * user_id : 2
     * user_nickname : 郭澎湃
     * user_photo : 2017030615082329333081.jpg
     * works_id : 1
     */

    private String add_time;
    private String comment_content;
    private long id;
    private long parent_id;
    private long replyUserId;
    private String replyUserNickName;
    private long user_id;
    private String user_nickname;
    private String user_photo;
    private long works_id;

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParent_id() {
        return parent_id;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }

    public long getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyUserNickName() {
        return replyUserNickName;
    }

    public void setReplyUserNickName(String replyUserNickName) {
        this.replyUserNickName = replyUserNickName;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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
