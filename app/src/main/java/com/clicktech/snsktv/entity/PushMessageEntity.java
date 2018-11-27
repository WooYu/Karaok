package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

//推送消息实体
public class PushMessageEntity implements Parcelable {

    public static final Parcelable.Creator<PushMessageEntity> CREATOR = new Parcelable.Creator<PushMessageEntity>() {
        @Override
        public PushMessageEntity createFromParcel(Parcel source) {
            return new PushMessageEntity(source);
        }

        @Override
        public PushMessageEntity[] newArray(int size) {
            return new PushMessageEntity[size];
        }
    };
    /**
     * message : 您有新的评论
     * msgCode : 2
     * msgType : 17
     * title : 评论消息
     * type : 0
     * userId : 123
     */

    private String message;
    private int msgCode;
    private int msgType;
    private String title;
    private String type;
    private String userId;

    public PushMessageEntity() {
    }

    protected PushMessageEntity(Parcel in) {
        this.message = in.readString();
        this.msgCode = in.readInt();
        this.msgType = in.readInt();
        this.title = in.readString();
        this.type = in.readString();
        this.userId = in.readString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeInt(this.msgCode);
        dest.writeInt(this.msgType);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeString(this.userId);
    }
}
