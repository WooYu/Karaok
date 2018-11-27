package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */

public class SingInfoResponse extends BaseResponse implements Parcelable {


    public static final Parcelable.Creator<SingInfoResponse> CREATOR = new Parcelable.Creator<SingInfoResponse>() {
        @Override
        public SingInfoResponse createFromParcel(Parcel source) {
            return new SingInfoResponse(source);
        }

        @Override
        public SingInfoResponse[] newArray(int size) {
            return new SingInfoResponse[size];
        }
    };
    /**
     * msg : success
     * errCode : 0
     * carouselFigureList : [{"carousel_image":"","carousel_link":"","carousel_order":0,"carousel_status":1,"carousel_title":"","id":1,"position":0,"source_id":0,"update_account":"","update_time":null}]
     * hotSongList : [{"category_name":"1980","is_new":1,"prelude_second":null,"song_name_us":null,"sing_count":1,"lyric_url":"111.jpg","song_name_jp":"告白气球","singer_id":1,"song_id":1,"category_image":"111.jpg","grade_file_url":"111.jpg","accompany_size":3096,"song_name_cn":null,"song_image":"111.jpg","mv_url":"111.jpg","original_url":"111.jpg","accompany_url":"111.jpg","add_time":""},{"category_name":"1980","is_new":0,"prelude_second":null,"song_name_us":"告白气球","sing_count":1,"lyric_url":"111.jpg","song_name_jp":"告白气球","singer_id":1,"song_id":2,"category_image":"111.jpg","grade_file_url":null,"accompany_size":2099,"song_name_cn":"告白气球","song_image":"111.jpg","mv_url":"111.jpg","original_url":"111.jpg","accompany_url":"111.jpg","add_time":""}]
     */

    private List<CarouselFigureListBean> carouselFigureList;
    private List<SongInfoBean> hotSongList;
    private List<SongInfoBean> songList;

    public SingInfoResponse() {
    }

    protected SingInfoResponse(Parcel in) {
        this.carouselFigureList = new ArrayList<CarouselFigureListBean>();
        in.readList(this.carouselFigureList, CarouselFigureListBean.class.getClassLoader());
        this.hotSongList = new ArrayList<SongInfoBean>();
        in.readList(this.hotSongList, SongInfoBean.class.getClassLoader());
    }

    public List<SongInfoBean> getSongList() {
        return songList;
    }

    public void setSongList(List<SongInfoBean> songList) {
        this.songList = songList;
    }

    public List<CarouselFigureListBean> getCarouselFigureList() {
        return carouselFigureList;
    }

    public void setCarouselFigureList(List<CarouselFigureListBean> carouselFigureList) {
        this.carouselFigureList = carouselFigureList;
    }

    public List<SongInfoBean> getHotSongList() {
        return hotSongList;
    }

    public void setHotSongList(List<SongInfoBean> hotSongList) {
        this.hotSongList = hotSongList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.carouselFigureList);
        dest.writeList(this.hotSongList);
    }

    public static class CarouselFigureListBean {
        /**
         * carousel_image :
         * carousel_link :
         * carousel_order : 0
         * carousel_status : 1
         * carousel_title :
         * id : 1
         * position : 0
         * source_id : 0
         * update_account :
         * update_time : null
         */

        private String carousel_image;
        private String carousel_link;
        private int carousel_order;
        private int carousel_status;
        private String carousel_title;
        private int id;
        private int position;
        private int source_id;
        private String update_account;
        private Object update_time;

        public String getCarousel_image() {
            return carousel_image;
        }

        public void setCarousel_image(String carousel_image) {
            this.carousel_image = carousel_image;
        }

        public String getCarousel_link() {
            return carousel_link;
        }

        public void setCarousel_link(String carousel_link) {
            this.carousel_link = carousel_link;
        }

        public int getCarousel_order() {
            return carousel_order;
        }

        public void setCarousel_order(int carousel_order) {
            this.carousel_order = carousel_order;
        }

        public int getCarousel_status() {
            return carousel_status;
        }

        public void setCarousel_status(int carousel_status) {
            this.carousel_status = carousel_status;
        }

        public String getCarousel_title() {
            return carousel_title;
        }

        public void setCarousel_title(String carousel_title) {
            this.carousel_title = carousel_title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getSource_id() {
            return source_id;
        }

        public void setSource_id(int source_id) {
            this.source_id = source_id;
        }

        public String getUpdate_account() {
            return update_account;
        }

        public void setUpdate_account(String update_account) {
            this.update_account = update_account;
        }

        public Object getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(Object update_time) {
            this.update_time = update_time;
        }
    }
}
