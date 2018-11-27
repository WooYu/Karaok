package com.clicktech.snsktv.entity;

/**
 * Created by Administrator on 2017/1/9.
 */

public class CommentEntity {


    /**
     * 参数名	类型	说明
     * id	long	评论id
     * comment_uid	long	评论人id
     * works_id	long	作品id
     * user_photo	string	用户头像
     * comment_content	string	评论内容
     * user_nickname	string	用户昵称
     * add_time	string	添加时间
     * works_name	string	作品名称
     * parent_id	int	回复的哪条评论id 父级为0
     */

    private String id,//long	评论id
            comment_uid,//long	评论人id
            works_id,//long	作品id
            user_photo,//	string	用户头像
            comment_content,//	string	评论内容
            user_nickname,//	string	用户昵称
            add_time,//	string	添加时间
            works_name,//	string	作品名称
            parent_id,//int	回复的哪条评论id 父级为0
            replyUserId,//
            replyUserNickName;


    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }


    public String getReplyUserNickName() {
        return replyUserNickName;
    }

    public void setReplyUserNickName(String replyUserNickName) {
        this.replyUserNickName = replyUserNickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment_uid() {
        return comment_uid;
    }

    public void setComment_uid(String comment_uid) {
        this.comment_uid = comment_uid;
    }

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

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
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

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
