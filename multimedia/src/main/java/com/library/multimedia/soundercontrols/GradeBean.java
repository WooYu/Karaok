package com.library.multimedia.soundercontrols;

/**
 * Created by wy201 on 2017-09-06.
 * 评分实体
 */

public class GradeBean {

    private int score;
    private float starttime;//起始时间,毫秒
    private float endtime;//结束时间，毫秒
    private int noteY;//对应的音准线
    private float noteX1;//评分的起始坐标
    private float noteX2;//评分的结束坐标
    private boolean match;//是否匹配
    private float curPositionOfHLine;//当前评分开始时，竖线的位置
    private int indexOfIcon;//图片下标

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    public float getStarttime() {
        return starttime;
    }

    public void setStarttime(float starttime) {
        this.starttime = starttime;
    }

    public float getEndtime() {
        return endtime;
    }

    public void setEndtime(float endtime) {
        this.endtime = endtime;
    }

    public int getNoteY() {
        return noteY;
    }

    public void setNoteY(int noteY) {
        this.noteY = noteY;
    }

    public float getNoteX1() {
        return noteX1;
    }

    public void setNoteX1(float noteX1) {
        this.noteX1 = noteX1;
    }

    public float getNoteX2() {
        return noteX2;
    }

    public void setNoteX2(float noteX2) {
        this.noteX2 = noteX2;
    }

    public float getCurPositionOfHLine() {
        return curPositionOfHLine;
    }

    public void setCurPositionOfHLine(float curPositionOfHLine) {
        this.curPositionOfHLine = curPositionOfHLine;
    }

    public int getIndexOfIcon() {
        return indexOfIcon;
    }

    public void setIndexOfIcon(int indexOfIcon) {
        this.indexOfIcon = indexOfIcon;
    }
}
