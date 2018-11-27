package com.clicktech.snsktv.common;

import java.io.File;

/**
 * Created by Administrator on 2017/1/22.
 * 常量配置
 */

public class ConstantConfig {


    //SharedPreferences
    public static final String TIME_DIFF = "time_diff";
    public static final String USERINFO = "userinfo";
    public static final String TOKEN = "token";
    public static final String SONG_SEARCH = "song_history";
    public static final String SINGER_HISTORY = "singer_history";
    public static final String SEARCH_HISTORY = "search_history";
    public static final String VOLUMEGUIDEMELODY = "volume_guidemelody";
    public static final String PUSHMESSAGE_RECENTLYAUDIENCE = "pushmessage_recentlyAudience";
    public static final String PUSHMESSAGE_GIFT = "pushmessage_gift";
    public static final String PUSHMESSAGE_SYSTEM = "pushmessage_system";
    public static final String PUSHMESSAGE_COMMENT_FOLLOWED = "pushmessage_comment_followed";
    public static final String PUSHMESSAGE_COMMENT_NOTFOLLOWED = "pushmessage_comment_notfollowed";
    public static final String PUSHMESSAGE_TOTAL = "pushmessage_total";


    /**
     * 目录设计：
     * 公共common :歌词lyric 、视频 video 、歌曲song、 mid
     * 录制record :hasvideo / novideo
     */

    //公共目录
    private static final String Dirs_Common = File.separator + "common" + File.separator;
    //歌词目录
    public static final String Dirs_Lyric = Dirs_Common + "lyric" + File.separator;
    //视频目录
    public static final String Dirs_Video = Dirs_Common + "video" + File.separator;
    //歌曲目录
    public static final String Dirs_Song = Dirs_Common + "song" + File.separator;
    //mid目录
    public static final String Dirs_Mid = Dirs_Common + "mid" + File.separator;
    //录制临时目录
    private static final String Dirs_Record = File.separator + "record" + File.separator;
    //带视频的录制
    public static final String Dirs_HasVideo = Dirs_Record + "hasvideo" + File.separator;
    //不带视频的录制
    public static final String Dirs_NoVideo = Dirs_Record + "novideo" + File.separator;


}
