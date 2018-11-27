package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-06-28.
 */

public class KSingRGVEntity implements Parcelable {
    public static final Creator<KSingRGVEntity> CREATOR = new Creator<KSingRGVEntity>() {
        @Override
        public KSingRGVEntity createFromParcel(Parcel source) {
            return new KSingRGVEntity(source);
        }

        @Override
        public KSingRGVEntity[] newArray(int size) {
            return new KSingRGVEntity[size];
        }
    };
    private boolean openFrontCamera;//是否开启前置摄像头
    private String accompanypath;//伴奏路径
    private String lyricpath;//歌词路径
    private String chorusvideopath;//下载的合唱mv路径
    private int workstype;
    private int layouttype;
    private String recordmvpath;
    private String recordaudiopath;
    private int recordtime;
    /**
     * 两个视频左右合并
     *
     * @param leftPath    左视频
     * @param rightPath   右视频
     * @param outputPath  输出视频
     * @param leftWidth   左视频宽度
     * @param leftHeight  左视频高度
     * @param rigthWidth  右视频宽度
     * @param rightHeight 右视频高度，必须和左视频高度相同
     */
    private String leftpath;
    private String rightpath;
    private int leftwidth;
    private int leftheight;
    private int rightwidth;
    private int rightheight;
    /**
     * 两个视频前后合并
     *
     * @param outsidePath   小视频路径
     * @param insidePath    大视频路径
     * @param outputPath    输出视频路径
     * @param outsideWidth  小视频宽度
     * @param outsideHeight 小视频高度
     * @param insideWidth   大视频宽度
     * @param insideHeight  大视频高度
     * @param insideBottomRightX    小视频离右下角的X坐标
     * @param insideBottomRightY    小视频离左下角的Y坐标
     * @param callback
     */
    private String outsidepath;
    private String insidepath;
    private String outputpath;
    private int outsidewidth;
    private int outsideheight;
    private int insidewidth;
    private int insideheight;
    private int insidebottomRightx;
    private int insidebottomRightY;

    public KSingRGVEntity() {
    }

    protected KSingRGVEntity(Parcel in) {
        this.openFrontCamera = in.readByte() != 0;
        this.accompanypath = in.readString();
        this.lyricpath = in.readString();
        this.chorusvideopath = in.readString();
        this.workstype = in.readInt();
        this.layouttype = in.readInt();
        this.recordmvpath = in.readString();
        this.recordaudiopath = in.readString();
        this.recordtime = in.readInt();
        this.leftpath = in.readString();
        this.rightpath = in.readString();
        this.leftwidth = in.readInt();
        this.leftheight = in.readInt();
        this.rightwidth = in.readInt();
        this.rightheight = in.readInt();
        this.outsidepath = in.readString();
        this.insidepath = in.readString();
        this.outputpath = in.readString();
        this.outsidewidth = in.readInt();
        this.outsideheight = in.readInt();
        this.insidewidth = in.readInt();
        this.insideheight = in.readInt();
        this.insidebottomRightx = in.readInt();
        this.insidebottomRightY = in.readInt();
    }

    public boolean isOpenFrontCamera() {
        return openFrontCamera;
    }

    public void setOpenFrontCamera(boolean openFrontCamera) {
        this.openFrontCamera = openFrontCamera;
    }

    public String getAccompanypath() {
        return accompanypath;
    }

    public void setAccompanypath(String accompanypath) {
        this.accompanypath = accompanypath;
    }

    public String getLyricpath() {
        return lyricpath;
    }

    public void setLyricpath(String lyricpath) {
        this.lyricpath = lyricpath;
    }

    public String getChorusvideopath() {
        return chorusvideopath;
    }

    public void setChorusvideopath(String chorusvideopath) {
        this.chorusvideopath = chorusvideopath;
    }

    public int getWorkstype() {
        return workstype;
    }

    public void setWorkstype(int workstype) {
        this.workstype = workstype;
    }

    public int getLayouttype() {
        return layouttype;
    }

    public void setLayouttype(int layouttype) {
        this.layouttype = layouttype;
    }

    public String getRecordmvpath() {
        return recordmvpath;
    }

    public void setRecordmvpath(String recordmvpath) {
        this.recordmvpath = recordmvpath;
    }

    public String getRecordaudiopath() {
        return recordaudiopath;
    }

    public void setRecordaudiopath(String recordaudiopath) {
        this.recordaudiopath = recordaudiopath;
    }

    public int getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(int recordtime) {
        this.recordtime = recordtime;
    }

    public String getLeftpath() {
        return leftpath;
    }

    public void setLeftpath(String leftpath) {
        this.leftpath = leftpath;
    }

    public String getRightpath() {
        return rightpath;
    }

    public void setRightpath(String rightpath) {
        this.rightpath = rightpath;
    }

    public int getLeftwidth() {
        return leftwidth;
    }

    public void setLeftwidth(int leftwidth) {
        this.leftwidth = leftwidth;
    }

    public int getLeftheight() {
        return leftheight;
    }

    public void setLeftheight(int leftheight) {
        this.leftheight = leftheight;
    }

    public int getRightwidth() {
        return rightwidth;
    }

    public void setRightwidth(int rightwidth) {
        this.rightwidth = rightwidth;
    }

    public int getRightheight() {
        return rightheight;
    }

    public void setRightheight(int rightheight) {
        this.rightheight = rightheight;
    }

    public String getOutsidepath() {
        return outsidepath;
    }

    public void setOutsidepath(String outsidepath) {
        this.outsidepath = outsidepath;
    }

    public String getInsidepath() {
        return insidepath;
    }

    public void setInsidepath(String insidepath) {
        this.insidepath = insidepath;
    }

    public String getOutputpath() {
        return outputpath;
    }

    public void setOutputpath(String outputpath) {
        this.outputpath = outputpath;
    }

    public int getOutsidewidth() {
        return outsidewidth;
    }

    public void setOutsidewidth(int outsidewidth) {
        this.outsidewidth = outsidewidth;
    }

    public int getOutsideheight() {
        return outsideheight;
    }

    public void setOutsideheight(int outsideheight) {
        this.outsideheight = outsideheight;
    }

    public int getInsidewidth() {
        return insidewidth;
    }

    public void setInsidewidth(int insidewidth) {
        this.insidewidth = insidewidth;
    }

    public int getInsideheight() {
        return insideheight;
    }

    public void setInsideheight(int insideheight) {
        this.insideheight = insideheight;
    }

    public int getInsidebottomRightx() {
        return insidebottomRightx;
    }

    public void setInsidebottomRightx(int insidebottomRightx) {
        this.insidebottomRightx = insidebottomRightx;
    }

    public int getInsidebottomRightY() {
        return insidebottomRightY;
    }

    public void setInsidebottomRightY(int insidebottomRightY) {
        this.insidebottomRightY = insidebottomRightY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.openFrontCamera ? (byte) 1 : (byte) 0);
        dest.writeString(this.accompanypath);
        dest.writeString(this.lyricpath);
        dest.writeString(this.chorusvideopath);
        dest.writeInt(this.workstype);
        dest.writeInt(this.layouttype);
        dest.writeString(this.recordmvpath);
        dest.writeString(this.recordaudiopath);
        dest.writeInt(this.recordtime);
        dest.writeString(this.leftpath);
        dest.writeString(this.rightpath);
        dest.writeInt(this.leftwidth);
        dest.writeInt(this.leftheight);
        dest.writeInt(this.rightwidth);
        dest.writeInt(this.rightheight);
        dest.writeString(this.outsidepath);
        dest.writeString(this.insidepath);
        dest.writeString(this.outputpath);
        dest.writeInt(this.outsidewidth);
        dest.writeInt(this.outsideheight);
        dest.writeInt(this.insidewidth);
        dest.writeInt(this.insideheight);
        dest.writeInt(this.insidebottomRightx);
        dest.writeInt(this.insidebottomRightY);
    }
}
