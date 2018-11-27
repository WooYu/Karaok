package com.clicktech.snsktv.entity;

/**
 * Created by Administrator on 2017/1/19.
 * 我的--已点伴奏
 */

public class SoundHistoryEntity {
    /**
     * 参数名	类型	说明
     * song_id	long	歌曲id
     * singer_name	string	歌手名字
     * song_name	string	歌曲名字
     * user_nickname	string	用户昵称
     * singer_id	long	歌曲id
     * work_id	long	作品id
     * song_image    作品图片
     */

    private String
            song_id,//long	歌曲id
            singer_name,//string	歌手名字
            song_name,//string	歌曲名字
            user_nickname,//string	用户昵称
            singer_id,//long	歌曲id
            work_id,//	long	作品id
            song_image;

    public String getSong_image() {
        return song_image;
    }

    public void setSong_image(String song_image) {
        this.song_image = song_image;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getSinger_name() {
        return singer_name;
    }

    public void setSinger_name(String singer_name) {
        this.singer_name = singer_name;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getSinger_id() {
        return singer_id;
    }

    public void setSinger_id(String singer_id) {
        this.singer_id = singer_id;
    }

    public String getWork_id() {
        return work_id;
    }

    public void setWork_id(String work_id) {
        this.work_id = work_id;
    }
}
