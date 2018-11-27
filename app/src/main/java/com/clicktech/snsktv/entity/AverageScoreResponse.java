package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

/**
 * Created by wy201 on 2017-10-25.
 * 平均分
 */

public class AverageScoreResponse extends BaseResponse {

    /**
     * songAverageScore : {"expression":10,"vibrato_times":1,"song_id":69,"vibrato_and_long_tone":49,"vibrato_length_sec":1,"total_score":62,"long_tone":92,"stability":3,"rythm":18,"pitch_all":0,"vibrato":0,"user_id":123,"pitch":73,"relative_rythm":-3}
     */

    private ScoreResultEntity songAverageScore;

    public ScoreResultEntity getSongAverageScore() {
        return songAverageScore;
    }

    public void setSongAverageScore(ScoreResultEntity songAverageScore) {
        this.songAverageScore = songAverageScore;
    }

}
