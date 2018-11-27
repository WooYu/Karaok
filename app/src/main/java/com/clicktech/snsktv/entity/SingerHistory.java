package com.clicktech.snsktv.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class SingerHistory implements Parcelable {


    public static final Parcelable.Creator<SingerHistory> CREATOR = new Parcelable.Creator<SingerHistory>() {
        @Override
        public SingerHistory createFromParcel(Parcel source) {
            return new SingerHistory(source);
        }

        @Override
        public SingerHistory[] newArray(int size) {
            return new SingerHistory[size];
        }
    };
    /**
     * msg : success
     * errCode : 0
     * singerList : [{"singer_id":1,"singer_name_cn":"汪峰","singer_name_us":"WF","category_name":"内地男歌手","singer_photo":"111.jpg","add_time":"2016-12-22 18:05:30"}]
     */

    private String msg;
    private int errCode;
    /**
     * singer_id : 1
     * singer_name_cn : 汪峰
     * singer_name_us : WF
     * category_name : 内地男歌手
     * singer_photo : 111.jpg
     * add_time : 2016-12-22 18:05:30
     */

    private List<SingerListBean> singerList = new ArrayList<>();

    public SingerHistory() {
    }

    protected SingerHistory(Parcel in) {
        this.msg = in.readString();
        this.errCode = in.readInt();
        this.singerList = new ArrayList<SingerListBean>();
        in.readList(this.singerList, SingerListBean.class.getClassLoader());
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public List<SingerListBean> getSingerList() {
        return singerList;
    }

    public void setSingerList(List<SingerListBean> singerList) {
        this.singerList = singerList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.msg);
        dest.writeInt(this.errCode);
        dest.writeList(this.singerList);
    }

    public static class SingerListBean implements Parcelable {
        public static final Creator<SingerListBean> CREATOR = new Creator<SingerListBean>() {
            @Override
            public SingerListBean createFromParcel(Parcel source) {
                return new SingerListBean(source);
            }

            @Override
            public SingerListBean[] newArray(int size) {
                return new SingerListBean[size];
            }
        };
        private int singer_id;
        private String singer_name_cn;
        private String singer_name_us;
        private String category_name;
        private String singer_photo;
        private String add_time;

        public SingerListBean() {
        }

        protected SingerListBean(Parcel in) {
            this.singer_id = in.readInt();
            this.singer_name_cn = in.readString();
            this.singer_name_us = in.readString();
            this.category_name = in.readString();
            this.singer_photo = in.readString();
            this.add_time = in.readString();
        }

        public int getSinger_id() {
            return singer_id;
        }

        public void setSinger_id(int singer_id) {
            this.singer_id = singer_id;
        }

        public String getSinger_name_cn() {
            return singer_name_cn;
        }

        public void setSinger_name_cn(String singer_name_cn) {
            this.singer_name_cn = singer_name_cn;
        }

        public String getSinger_name_us() {
            return singer_name_us;
        }

        public void setSinger_name_us(String singer_name_us) {
            this.singer_name_us = singer_name_us;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getSinger_photo() {
            return singer_photo;
        }

        public void setSinger_photo(String singer_photo) {
            this.singer_photo = singer_photo;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.singer_id);
            dest.writeString(this.singer_name_cn);
            dest.writeString(this.singer_name_us);
            dest.writeString(this.category_name);
            dest.writeString(this.singer_photo);
            dest.writeString(this.add_time);
        }
    }
}
