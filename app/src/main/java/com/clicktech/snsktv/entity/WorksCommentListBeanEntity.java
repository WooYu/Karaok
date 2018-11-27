package com.clicktech.snsktv.entity;

/**
 * Created by Administrator on 2017/4/18.
 */

public class WorksCommentListBeanEntity {

    /**
     * replyUserId : 1
     * user_id : 1
     * parent_id : 2
     * user_nickname : 123456
     * works_id : 1
     * comment_content : 谢谢
     * id : 3
     * add_time : 2017-03-15 11:35:09
     * user_photo : 2017030615082329333081.jpg
     * replyUserNickName : 123456
     */

    private int replyUserId;
    private int user_id;
    private int parent_id;
    private String user_nickname;
    private int works_id;
    private String comment_content;
    private int id;
    private String add_time;
    private String user_photo;
    private String replyUserNickName;

    public int getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(int replyUserId) {
        this.replyUserId = replyUserId;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
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

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getReplyUserNickName() {
        return replyUserNickName;
    }

    public void setReplyUserNickName(String replyUserNickName) {
        this.replyUserNickName = replyUserNickName;
    }
}
