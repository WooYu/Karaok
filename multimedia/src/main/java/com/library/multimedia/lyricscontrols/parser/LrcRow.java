package com.library.multimedia.lyricscontrols.parser;

/**
 * 每行歌词的实体类，实现了Comparable接口，方便List<LrcRow>的sort排序
 */
public class LrcRow implements Comparable<LrcRow> {

    /**
     * 开始时间 为00:10:00
     ***/
    private String timeStr;
    /**
     * 开始时间 毫米数  00:10:00  为10000
     **/
    private int time;
    /**
     * 歌词内容
     **/
    private String content;
    /**
     * 该行歌词显示的总时间
     **/
    private int totalTime;
    /**
     * 歌词颜色
     */
    private int lyriccolor;
    /**
     * 角色切换
     */
    private boolean roleSwitch;
    /**
     * 角色类型
     */
    private int roletype;

    public LrcRow(String timeStr, int time, String content, int color, boolean switchColor, int roletype) {
        super();
        this.timeStr = timeStr;
        this.time = time;
        this.content = content;
        this.lyriccolor = color;
        this.roleSwitch = switchColor;
        this.roletype = roletype;
    }

    public int getRoletype() {
        return roletype;
    }

    public void setRoletype(int roletype) {
        this.roletype = roletype;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLyriccolor() {
        return lyriccolor;
    }

    public void setLyriccolor(int lyriccolor) {
        this.lyriccolor = lyriccolor;
    }

    public boolean isSwitch() {
        return roleSwitch;
    }

    public void setSwitchColor(boolean switchColor) {
        this.roleSwitch = switchColor;
    }

    @Override
    public int compareTo(LrcRow anotherLrcRow) {
        return this.time - anotherLrcRow.time;
    }

    @Override
    public String toString() {
        return "LrcRow [timeStr=" + timeStr + ", time=" + time + ", content="
                + content + "]";
    }


}
