package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wy201 on 2017-11-22.
 * 打分结果
 */

public class ScoreResultEntity implements Parcelable {
    public static final Creator<ScoreResultEntity> CREATOR = new Creator<ScoreResultEntity>() {
        @Override
        public ScoreResultEntity createFromParcel(Parcel source) {
            return new ScoreResultEntity(source);
        }

        @Override
        public ScoreResultEntity[] newArray(int size) {
            return new ScoreResultEntity[size];
        }
    };
    private boolean score_valid;
    private float result_from_time_sec;
    private float result_to_time_sec;
    private float total_score;
    private float part_score;
    private float pitch;
    private float rythm;
    private float relative_rythm;
    private float expression;
    private float yokuyo;
    private float shakuri;
    private int shakuri_count;
    private float kobushi;
    private int kobushi_count;
    private float fall;
    private int fall_count;
    private float long_tone;
    private float stability;
    private int voice_range_low;
    private int voice_range_high;
    private int guide_melody_range_low;
    private int guide_melody_range_high;
    private float over_tone;
    private float vibrato;
    private float vibrato_times;
    private float vibrato_length_sec;
    private float vibrato_and_long_tone;
    private int key_count;
    private float pitch_all;
    private float pitch_part[];//[24]
    private long song_id;
    private long user_id;
    private long userId;
    private long works_id;

    public ScoreResultEntity() {
    }

    protected ScoreResultEntity(Parcel in) {
        this.score_valid = in.readByte() != 0;
        this.result_from_time_sec = in.readFloat();
        this.result_to_time_sec = in.readFloat();
        this.total_score = in.readFloat();
        this.part_score = in.readFloat();
        this.pitch = in.readFloat();
        this.rythm = in.readFloat();
        this.relative_rythm = in.readFloat();
        this.expression = in.readFloat();
        this.yokuyo = in.readFloat();
        this.shakuri = in.readFloat();
        this.shakuri_count = in.readInt();
        this.kobushi = in.readFloat();
        this.kobushi_count = in.readInt();
        this.fall = in.readFloat();
        this.fall_count = in.readInt();
        this.long_tone = in.readFloat();
        this.stability = in.readFloat();
        this.voice_range_low = in.readInt();
        this.voice_range_high = in.readInt();
        this.guide_melody_range_low = in.readInt();
        this.guide_melody_range_high = in.readInt();
        this.over_tone = in.readFloat();
        this.vibrato = in.readFloat();
        this.vibrato_times = in.readFloat();
        this.vibrato_length_sec = in.readFloat();
        this.vibrato_and_long_tone = in.readFloat();
        this.key_count = in.readInt();
        this.pitch_all = in.readFloat();
        this.pitch_part = in.createFloatArray();
        this.song_id = in.readLong();
        this.user_id = in.readLong();
        this.userId = in.readLong();
        this.works_id = in.readLong();
    }

    public boolean isScore_valid() {
        return score_valid;
    }

    public void setScore_valid(boolean score_valid) {
        this.score_valid = score_valid;
    }

    public float getResult_from_time_sec() {
        return result_from_time_sec;
    }

    public void setResult_from_time_sec(float result_from_time_sec) {
        this.result_from_time_sec = result_from_time_sec;
    }

    public float getResult_to_time_sec() {
        return result_to_time_sec;
    }

    public void setResult_to_time_sec(float result_to_time_sec) {
        this.result_to_time_sec = result_to_time_sec;
    }

    public float getTotal_score() {
        return total_score;
    }

    public void setTotal_score(float total_score) {
        this.total_score = total_score;
    }

    public float getPart_score() {
        return part_score;
    }

    public void setPart_score(float part_score) {
        this.part_score = part_score;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRythm() {
        return rythm;
    }

    public void setRythm(float rythm) {
        this.rythm = rythm;
    }

    public float getRelative_rythm() {
        return relative_rythm;
    }

    public void setRelative_rythm(float relative_rythm) {
        this.relative_rythm = relative_rythm;
    }

    public float getExpression() {
        return expression;
    }

    public void setExpression(float expression) {
        this.expression = expression;
    }

    public float getYokuyo() {
        return yokuyo;
    }

    public void setYokuyo(float yokuyo) {
        this.yokuyo = yokuyo;
    }

    public float getShakuri() {
        return shakuri;
    }

    public void setShakuri(float shakuri) {
        this.shakuri = shakuri;
    }

    public int getShakuri_count() {
        return shakuri_count;
    }

    public void setShakuri_count(int shakuri_count) {
        this.shakuri_count = shakuri_count;
    }

    public float getKobushi() {
        return kobushi;
    }

    public void setKobushi(float kobushi) {
        this.kobushi = kobushi;
    }

    public int getKobushi_count() {
        return kobushi_count;
    }

    public void setKobushi_count(int kobushi_count) {
        this.kobushi_count = kobushi_count;
    }

    public float getFall() {
        return fall;
    }

    public void setFall(float fall) {
        this.fall = fall;
    }

    public int getFall_count() {
        return fall_count;
    }

    public void setFall_count(int fall_count) {
        this.fall_count = fall_count;
    }

    public float getLong_tone() {
        return long_tone;
    }

    public void setLong_tone(float long_tone) {
        this.long_tone = long_tone;
    }

    public float getStability() {
        return stability;
    }

    public void setStability(float stability) {
        this.stability = stability;
    }

    public int getVoice_range_low() {
        return voice_range_low;
    }

    public void setVoice_range_low(int voice_range_low) {
        this.voice_range_low = voice_range_low;
    }

    public int getVoice_range_high() {
        return voice_range_high;
    }

    public void setVoice_range_high(int voice_range_high) {
        this.voice_range_high = voice_range_high;
    }

    public int getGuide_melody_range_low() {
        return guide_melody_range_low;
    }

    public void setGuide_melody_range_low(int guide_melody_range_low) {
        this.guide_melody_range_low = guide_melody_range_low;
    }

    public int getGuide_melody_range_high() {
        return guide_melody_range_high;
    }

    public void setGuide_melody_range_high(int guide_melody_range_high) {
        this.guide_melody_range_high = guide_melody_range_high;
    }

    public float getOver_tone() {
        return over_tone;
    }

    public void setOver_tone(float over_tone) {
        this.over_tone = over_tone;
    }

    public float getVibrato() {
        return vibrato;
    }

    public void setVibrato(float vibrato) {
        this.vibrato = vibrato;
    }

    public float getVibrato_times() {
        return vibrato_times;
    }

    public void setVibrato_times(float vibrato_times) {
        this.vibrato_times = vibrato_times;
    }

    public float getVibrato_length_sec() {
        return vibrato_length_sec;
    }

    public void setVibrato_length_sec(float vibrato_length_sec) {
        this.vibrato_length_sec = vibrato_length_sec;
    }

    public float getVibrato_and_long_tone() {
        return vibrato_and_long_tone;
    }

    public void setVibrato_and_long_tone(float vibrato_and_long_tone) {
        this.vibrato_and_long_tone = vibrato_and_long_tone;
    }

    public int getKey_count() {
        return key_count;
    }

    public void setKey_count(int key_count) {
        this.key_count = key_count;
    }

    public float getPitch_all() {
        return pitch_all;
    }

    public void setPitch_all(float pitch_all) {
        this.pitch_all = pitch_all;
    }

    public float[] getPitch_part() {
        return pitch_part;
    }

    public void setPitch_part(float[] pitch_part) {
        this.pitch_part = pitch_part;
    }

    public long getSong_id() {
        return song_id;
    }

    public void setSong_id(long song_id) {
        this.song_id = song_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getWorks_id() {
        return works_id;
    }

    public void setWorks_id(long works_id) {
        this.works_id = works_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.score_valid ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.result_from_time_sec);
        dest.writeFloat(this.result_to_time_sec);
        dest.writeFloat(this.total_score);
        dest.writeFloat(this.part_score);
        dest.writeFloat(this.pitch);
        dest.writeFloat(this.rythm);
        dest.writeFloat(this.relative_rythm);
        dest.writeFloat(this.expression);
        dest.writeFloat(this.yokuyo);
        dest.writeFloat(this.shakuri);
        dest.writeInt(this.shakuri_count);
        dest.writeFloat(this.kobushi);
        dest.writeInt(this.kobushi_count);
        dest.writeFloat(this.fall);
        dest.writeInt(this.fall_count);
        dest.writeFloat(this.long_tone);
        dest.writeFloat(this.stability);
        dest.writeInt(this.voice_range_low);
        dest.writeInt(this.voice_range_high);
        dest.writeInt(this.guide_melody_range_low);
        dest.writeInt(this.guide_melody_range_high);
        dest.writeFloat(this.over_tone);
        dest.writeFloat(this.vibrato);
        dest.writeFloat(this.vibrato_times);
        dest.writeFloat(this.vibrato_length_sec);
        dest.writeFloat(this.vibrato_and_long_tone);
        dest.writeInt(this.key_count);
        dest.writeFloat(this.pitch_all);
        dest.writeFloatArray(this.pitch_part);
        dest.writeLong(this.song_id);
        dest.writeLong(this.user_id);
        dest.writeLong(this.userId);
        dest.writeLong(this.works_id);
    }
}
