package com.clicktech.snsktv.common;

/**
 * Created by Administrator on 2016/12/20.
 * 存储EventBus的Tag常量
 */

public interface EventBusTag {
    String SEARCHRESULT_MESSAGE = "searchreult_message";//搜索结果
    String LOGIN_SUCCESS = "login_success";//登录成功
    String LOGOUTSUCCESS = "logout_success";//退出登录成功
    String SWITCHLANGUAGE = "switchlanguage";//切换语言
    String SHARE_MODECHANGE = "share_modechange";//分享添加模式
    String SHARE_SHOWPOPWINDOW = "share_showpopwindow";//分享展示弹窗
    String CHANGEAVATAR = "changeavatar";//更换个人头像
    String PHOTOALBUMCHANGE = "photoalbumchange";//相册更新成
    String ANNOUNCESUCCESS = "announcesuccess";//作品发布成功
    String SWITCHMODE = "switchsingingmode";//切换录制模式
    String MINIPLAYER_SHOW = "miniplayer_show";//展示迷你播放器
    String MINIPLAYER_HIDE = "miniplayer_hide";//隐藏迷你播发器
    String MINIPLAYER_UPDATEPROGRESS = "miniplayer_updateprogress";//更新迷你播发器进度
    String MINIPLAYER_CLOSEPROGRESS = "miniplayer_closeprogress";//关闭更新迷你播放器进度
    String MINIPLAYER_OPENPROGRESS = "miniplayer_openprogress";//打开更新迷你播放器进度
    String MINIPLAYER_TOGGLE = "miniplayer_toggle";//迷你播放器点击播放暂停
    String MINIPLAYER_STOP = "miniplayer_stop";//录制作品时，需要暂停播放
    String MINIPLAYER_UPDATEPLAYSTATE = "miniplayer_updateplaystate";//更新迷你播放器状态
    String PLAYLIST_ADDSONG = "playlist_addsong";//播放列表添加歌曲:1添加单首歌曲，2添加全部歌曲
    String PLAYLIST_DELSONG = "playlist_delsong";//播放列表删除歌曲：1删除单首歌曲，2删除全部歌曲
    String PLAYLIST_TOGGLESONG = "playlist_togglesong";//播放列表切换播放歌曲
    String PLAYLIST_SHOW = "playlist_show";//展示播放列表

}
