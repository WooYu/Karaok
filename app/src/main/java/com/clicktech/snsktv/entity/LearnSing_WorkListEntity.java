package com.clicktech.snsktv.entity;

/**
 * Created by Administrator on 2017/1/19.
 * 学唱主页  排行榜
 */

public class LearnSing_WorkListEntity {

    /**
     * 参数名	必选	类型	说明
     * song_id	是	String	歌曲id
     * singer_id	是	int	歌手id
     * user_id	是	int	用户id
     * user_photo	是	String	头像
     * user_nickname	是	String	昵称
     * works_type	是	String	类型：0默认 1合唱第一人 2合唱第二人 3清唱
     * lyric_firstline	是	String	首行歌词
     */
    private String
            song_id,//	是	String	歌曲id
            singer_id,//是	int	歌手id
            user_id,//	是	int	用户id
            user_photo,//	是	String	头像
            user_nickname,//	是	String	昵称
            works_type,//是	String	类型：0默认 1合唱第一人 2合唱第二人 3清唱
            lyric_firstline,//是	String	首行歌词
            user_sex,//是	String	0女  1男
            works_id;// 作品id

    public String getWorks_id() {
        return works_id;
    }

    public void setWorks_id(String works_id) {
        this.works_id = works_id;
    }


    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
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

    public String getWorks_type() {
        return works_type;
    }

    public void setWorks_type(String works_type) {
        this.works_type = works_type;
    }

    public String getLyric_firstline() {
        return lyric_firstline;
    }

    public void setLyric_firstline(String lyric_firstline) {
        this.lyric_firstline = lyric_firstline;
    }
}
