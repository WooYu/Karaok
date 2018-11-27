package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/26.
 * 标准的歌曲信息实体
 * 歌曲全部信息(所有的歌曲实体都以这个为准)
 */

public class SongInfoBean implements Parcelable {

    public static final Creator<SongInfoBean> CREATOR = new Creator<SongInfoBean>() {
        @Override
        public SongInfoBean createFromParcel(Parcel source) {
            return new SongInfoBean(source);
        }

        @Override
        public SongInfoBean[] newArray(int size) {
            return new SongInfoBean[size];
        }
    };
    /**
     * singer_name_cn : 汪峰
     * prelude_second : 20
     * works_second : 150
     * song_name_jp : rising girl（日）
     * song_id : 3
     * singer_name_us : WF
     * works_name : 小幸运
     * grade_file_url : 111.jpg
     * accompany_size : 3096
     * song_image : 111.jpg
     * user_nickname : 123456
     * accompany_url : risinggirl.mp3
     * user_photo : 2017030615082329333081.jpg
     * start_second : 10
     * works_image : 111.jpg
     * works_size : 3021
     * works_url : 111.jpg
     * is_new : 1
     * works_type : 1
     * song_name_us : rising girl（英）
     * sing_count : 1
     * singer_name_jp :
     * lyric_url : risinggirl.lrc
     * chorus_count :
     * singer_id : 1
     * user_id : 1
     * song_name_cn : rising girl（中）
     * mv_url : risinggirl.mp3
     * original_url : risinggirl.mp3
     * allow_chorus： 0
     */

    private String singer_name_cn;
    private String prelude_second;
    private String song_name_jp;
    private String song_id;
    private String singer_name_us;
    private String grade_file_url;
    private String accompany_size;
    private String song_image;
    private String user_nickname;
    private String accompany_url;
    private String user_photo;
    private String start_second;
    private String works_id;
    private String works_image;
    private String works_size;
    private String works_url;
    private String works_type;//works_type	是	int	类型：0普通K歌; 1合唱第一人唱A; 2合唱第一人唱B; 3合唱第二人; 4清唱K歌
    private String works_name;
    private String works_desc;
    private String works_level;
    private String works_score;
    private String works_second;
    private String is_new;
    private String song_name_us;
    private String sing_count;
    private String singer_name_jp;
    private String lyric_url;
    private String chorus_count;
    private String singer_id;
    private String user_id;
    private String song_name_cn;
    private String mv_url;
    private String original_url;
    private String lyric_firstline;
    private String singer_name_jp_ping;
    private String allow_chorus;
    private String listen_count;
    private String id;
    private String add_time;
    private String commentNum;
    private String coinNum;
    private String flowerNum;
    private String user_sex;
    private String first_letter;
    private String album_id;
    private String chorus_with;
    private String is_top;
    private String commNum;
    private String songName;
    private boolean bPlayStatus;
    private String store_type;
    private String album_name;
    private String album_image;
    private String itunes_apple_com;
    private String music_google_com;
    private String times;
    private String works_sum;
    private String phone_type;
    private String album_introduce;
    private String singer_photo;

    public SongInfoBean() {
    }

    protected SongInfoBean(Parcel in) {
        this.singer_name_cn = in.readString();
        this.prelude_second = in.readString();
        this.song_name_jp = in.readString();
        this.song_id = in.readString();
        this.singer_name_us = in.readString();
        this.grade_file_url = in.readString();
        this.accompany_size = in.readString();
        this.song_image = in.readString();
        this.user_nickname = in.readString();
        this.accompany_url = in.readString();
        this.user_photo = in.readString();
        this.start_second = in.readString();
        this.works_id = in.readString();
        this.works_image = in.readString();
        this.works_size = in.readString();
        this.works_url = in.readString();
        this.works_type = in.readString();
        this.works_name = in.readString();
        this.works_desc = in.readString();
        this.works_level = in.readString();
        this.works_score = in.readString();
        this.works_second = in.readString();
        this.is_new = in.readString();
        this.song_name_us = in.readString();
        this.sing_count = in.readString();
        this.singer_name_jp = in.readString();
        this.lyric_url = in.readString();
        this.chorus_count = in.readString();
        this.singer_id = in.readString();
        this.user_id = in.readString();
        this.song_name_cn = in.readString();
        this.mv_url = in.readString();
        this.original_url = in.readString();
        this.lyric_firstline = in.readString();
        this.singer_name_jp_ping = in.readString();
        this.allow_chorus = in.readString();
        this.listen_count = in.readString();
        this.id = in.readString();
        this.add_time = in.readString();
        this.commentNum = in.readString();
        this.coinNum = in.readString();
        this.flowerNum = in.readString();
        this.user_sex = in.readString();
        this.first_letter = in.readString();
        this.album_id = in.readString();
        this.chorus_with = in.readString();
        this.is_top = in.readString();
        this.commNum = in.readString();
        this.songName = in.readString();
        this.bPlayStatus = in.readByte() != 0;
        this.store_type = in.readString();
        this.album_name = in.readString();
        this.album_image = in.readString();
        this.itunes_apple_com = in.readString();
        this.music_google_com = in.readString();
        this.times = in.readString();
        this.works_sum = in.readString();
        this.phone_type = in.readString();
        this.album_introduce = in.readString();
        this.singer_photo = in.readString();
    }

    public String getSinger_name_cn() {
        return singer_name_cn;
    }

    public void setSinger_name_cn(String singer_name_cn) {
        this.singer_name_cn = singer_name_cn;
    }

    public String getPrelude_second() {
        return prelude_second;
    }

    public void setPrelude_second(String prelude_second) {
        this.prelude_second = prelude_second;
    }

    public String getSong_name_jp() {
        return song_name_jp;
    }

    public void setSong_name_jp(String song_name_jp) {
        this.song_name_jp = song_name_jp;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getSinger_name_us() {
        return singer_name_us;
    }

    public void setSinger_name_us(String singer_name_us) {
        this.singer_name_us = singer_name_us;
    }

    public String getGrade_file_url() {
        return grade_file_url;
    }

    public void setGrade_file_url(String grade_file_url) {
        this.grade_file_url = grade_file_url;
    }

    public String getAccompany_size() {
        return accompany_size;
    }

    public void setAccompany_size(String accompany_size) {
        this.accompany_size = accompany_size;
    }

    public String getSong_image() {
        return song_image;
    }

    public void setSong_image(String song_image) {
        this.song_image = song_image;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getAccompany_url() {
        return accompany_url;
    }

    public void setAccompany_url(String accompany_url) {
        this.accompany_url = accompany_url;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getStart_second() {
        return start_second;
    }

    public void setStart_second(String start_second) {
        this.start_second = start_second;
    }

    public String getWorks_id() {
        return works_id;
    }

    public void setWorks_id(String works_id) {
        this.works_id = works_id;
    }

    public String getWorks_image() {
        return works_image;
    }

    public void setWorks_image(String works_image) {
        this.works_image = works_image;
    }

    public String getWorks_size() {
        return works_size;
    }

    public void setWorks_size(String works_size) {
        this.works_size = works_size;
    }

    public String getWorks_url() {
        return works_url;
    }

    public void setWorks_url(String works_url) {
        this.works_url = works_url;
    }

    public String getWorks_type() {
        return works_type;
    }

    public void setWorks_type(String works_type) {
        this.works_type = works_type;
    }

    public String getWorks_name() {
        return works_name;
    }

    public void setWorks_name(String works_name) {
        this.works_name = works_name;
    }

    public String getWorks_desc() {
        return works_desc;
    }

    public void setWorks_desc(String works_desc) {
        this.works_desc = works_desc;
    }

    public String getWorks_level() {
        return works_level;
    }

    public void setWorks_level(String works_level) {
        this.works_level = works_level;
    }

    public String getWorks_score() {
        return works_score;
    }

    public void setWorks_score(String works_score) {
        this.works_score = works_score;
    }

    public String getWorks_second() {
        return works_second;
    }

    public void setWorks_second(String works_second) {
        this.works_second = works_second;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getSong_name_us() {
        return song_name_us;
    }

    public void setSong_name_us(String song_name_us) {
        this.song_name_us = song_name_us;
    }

    public String getSing_count() {
        return sing_count;
    }

    public void setSing_count(String sing_count) {
        this.sing_count = sing_count;
    }

    public String getSinger_name_jp() {
        return singer_name_jp;
    }

    public void setSinger_name_jp(String singer_name_jp) {
        this.singer_name_jp = singer_name_jp;
    }

    public String getLyric_url() {
        return lyric_url;
    }

    public void setLyric_url(String lyric_url) {
        this.lyric_url = lyric_url;
    }

    public String getChorus_count() {
        return chorus_count;
    }

    public void setChorus_count(String chorus_count) {
        this.chorus_count = chorus_count;
    }

    public String getSinger_id() {
        return singer_id;
    }

    public void setSinger_id(String singer_id) {
        this.singer_id = singer_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSong_name_cn() {
        return song_name_cn;
    }

    public void setSong_name_cn(String song_name_cn) {
        this.song_name_cn = song_name_cn;
    }

    public String getMv_url() {
        return mv_url;
    }

    public void setMv_url(String mv_url) {
        this.mv_url = mv_url;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public String getLyric_firstline() {
        return lyric_firstline;
    }

    public void setLyric_firstline(String lyric_firstline) {
        this.lyric_firstline = lyric_firstline;
    }

    public String getSinger_name_jp_ping() {
        return singer_name_jp_ping;
    }

    public void setSinger_name_jp_ping(String singer_name_jp_ping) {
        this.singer_name_jp_ping = singer_name_jp_ping;
    }

    public String getAllow_chorus() {
        return allow_chorus;
    }

    public void setAllow_chorus(String allow_chorus) {
        this.allow_chorus = allow_chorus;
    }

    public String getListen_count() {
        return listen_count;
    }

    public void setListen_count(String listen_count) {
        this.listen_count = listen_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(String coinNum) {
        this.coinNum = coinNum;
    }

    public String getFlowerNum() {
        return flowerNum;
    }

    public void setFlowerNum(String flowerNum) {
        this.flowerNum = flowerNum;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getFirst_letter() {
        return first_letter;
    }

    public void setFirst_letter(String first_letter) {
        this.first_letter = first_letter;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getChorus_with() {
        return chorus_with;
    }

    public void setChorus_with(String chorus_with) {
        this.chorus_with = chorus_with;
    }

    public String getIs_top() {
        return is_top;
    }

    public void setIs_top(String is_top) {
        this.is_top = is_top;
    }

    public String getCommNum() {
        return commNum;
    }

    public void setCommNum(String commNum) {
        this.commNum = commNum;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public boolean isbPlayStatus() {
        return bPlayStatus;
    }

    public void setbPlayStatus(boolean bPlayStatus) {
        this.bPlayStatus = bPlayStatus;
    }

    public String getStore_type() {
        return store_type;
    }

    public void setStore_type(String store_type) {
        this.store_type = store_type;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_image() {
        return album_image;
    }

    public void setAlbum_image(String album_image) {
        this.album_image = album_image;
    }

    public String getItunes_apple_com() {
        return itunes_apple_com;
    }

    public void setItunes_apple_com(String itunes_apple_com) {
        this.itunes_apple_com = itunes_apple_com;
    }

    public String getMusic_google_com() {
        return music_google_com;
    }

    public void setMusic_google_com(String music_google_com) {
        this.music_google_com = music_google_com;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getWorks_sum() {
        return works_sum;
    }

    public void setWorks_sum(String works_sum) {
        this.works_sum = works_sum;
    }

    public String getPhone_type() {
        return phone_type;
    }

    public void setPhone_type(String phone_type) {
        this.phone_type = phone_type;
    }

    public String getAlbum_introduce() {
        return album_introduce;
    }

    public void setAlbum_introduce(String album_introduce) {
        this.album_introduce = album_introduce;
    }

    public String getSinger_photo() {
        return singer_photo;
    }

    public void setSinger_photo(String singer_photo) {
        this.singer_photo = singer_photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.singer_name_cn);
        dest.writeString(this.prelude_second);
        dest.writeString(this.song_name_jp);
        dest.writeString(this.song_id);
        dest.writeString(this.singer_name_us);
        dest.writeString(this.grade_file_url);
        dest.writeString(this.accompany_size);
        dest.writeString(this.song_image);
        dest.writeString(this.user_nickname);
        dest.writeString(this.accompany_url);
        dest.writeString(this.user_photo);
        dest.writeString(this.start_second);
        dest.writeString(this.works_id);
        dest.writeString(this.works_image);
        dest.writeString(this.works_size);
        dest.writeString(this.works_url);
        dest.writeString(this.works_type);
        dest.writeString(this.works_name);
        dest.writeString(this.works_desc);
        dest.writeString(this.works_level);
        dest.writeString(this.works_score);
        dest.writeString(this.works_second);
        dest.writeString(this.is_new);
        dest.writeString(this.song_name_us);
        dest.writeString(this.sing_count);
        dest.writeString(this.singer_name_jp);
        dest.writeString(this.lyric_url);
        dest.writeString(this.chorus_count);
        dest.writeString(this.singer_id);
        dest.writeString(this.user_id);
        dest.writeString(this.song_name_cn);
        dest.writeString(this.mv_url);
        dest.writeString(this.original_url);
        dest.writeString(this.lyric_firstline);
        dest.writeString(this.singer_name_jp_ping);
        dest.writeString(this.allow_chorus);
        dest.writeString(this.listen_count);
        dest.writeString(this.id);
        dest.writeString(this.add_time);
        dest.writeString(this.commentNum);
        dest.writeString(this.coinNum);
        dest.writeString(this.flowerNum);
        dest.writeString(this.user_sex);
        dest.writeString(this.first_letter);
        dest.writeString(this.album_id);
        dest.writeString(this.chorus_with);
        dest.writeString(this.is_top);
        dest.writeString(this.commNum);
        dest.writeString(this.songName);
        dest.writeByte(this.bPlayStatus ? (byte) 1 : (byte) 0);
        dest.writeString(this.store_type);
        dest.writeString(this.album_name);
        dest.writeString(this.album_image);
        dest.writeString(this.itunes_apple_com);
        dest.writeString(this.music_google_com);
        dest.writeString(this.times);
        dest.writeString(this.works_sum);
        dest.writeString(this.phone_type);
        dest.writeString(this.album_introduce);
        dest.writeString(this.singer_photo);
    }
}
