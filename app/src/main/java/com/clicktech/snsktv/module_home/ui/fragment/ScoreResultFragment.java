package com.clicktech.snsktv.module_home.ui.fragment;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.TimeUtils;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.widget.scoreresult.RadarChart;
import com.clicktech.snsktv.widget.scoreresult.ScoreView_Pitch;
import com.clicktech.snsktv.widget.scoreresult.ScoreView_Rhythm;
import com.clicktech.snsktv.widget.scoreresult.ScoreView_Stability;
import com.clicktech.snsktv.widget.scoreresult.TextView_Border;
import com.clicktech.snsktv.widget.scoreresult.TextView_Scores;
import com.clicktech.snsktv.widget.scoreresult.TextView_Synthetical;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wy201 on 2017-09-23.
 */

public class ScoreResultFragment extends DialogFragment {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_synthetical)
    TextView_Synthetical tvSynthetical;
    @BindView(R.id.tv_score)
    TextView_Scores tvScore;
    @BindView(R.id.progress_red)
    ProgressBar progressRed;
    @BindView(R.id.progress_blue)
    ProgressBar progressBlue;
    @BindView(R.id.tv_average)
    TextView tvAverage;
    @BindView(R.id.radarchart)
    RadarChart radarchart;
    @BindView(R.id.tv_tone)
    TextView_Border tvTone;
    @BindView(R.id.scoreview_pitch)
    ScoreView_Pitch scoreViewPitch;
    @BindView(R.id.tv_accuracy)
    TextView tvAccuracy;
    @BindView(R.id.tv_accuracy_value)
    TextView tvAccuracyValue;
    @BindView(R.id.tv_accuracy_unit)
    TextView tvAccuracyUnit;
    @BindView(R.id.tv_rhythm)
    TextView_Border tvRhythm;
    @BindView(R.id.scoreview_rhythm)
    ScoreView_Rhythm scoreviewRhythm;
    @BindView(R.id.tv_stability)
    TextView_Border tvStability;
    @BindView(R.id.scoreview_stability)
    ScoreView_Stability scoreviewStability;
    @BindView(R.id.tv_trill)
    TextView_Border tvTrill;
    @BindView(R.id.rbar_trill)
    RatingBar rbarTrill;
    @BindView(R.id.tv_longsound)
    TextView_Border tvLongsound;
    @BindView(R.id.rbar_longsound)
    RatingBar rbarLongsound;
    @BindView(R.id.tv_trill_sum)
    TextView tvLongSum;

    Unbinder unbinder;
    private ScoreResultEventListener mListener;
    private ScoreResultEntity mTotalScoreBean;
    private ScoreResultEntity mAverageScore;

    //保留两位小数
    public static String keepDecimals(float value, int places) {
        DecimalFormat df = null;
        if (places == 2) {
            df = new DecimalFormat("0.00");
        } else {
            df = new DecimalFormat("0.000");
        }
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_scoreresult, container);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        if (null != mListener) {
            mListener.clickClose();
        }
    }

    //获取打分结果
    private void initData() {
        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }

        mTotalScoreBean = bundle.getParcelable("totalscore");
        mAverageScore = bundle.getParcelable("averagescore");
        if (null == mTotalScoreBean) {
            return;
        }

        if (null == mAverageScore) {
            mAverageScore = new ScoreResultEntity();
        }
        fillingData(mTotalScoreBean, mAverageScore);
    }

    //填充数据
    private void fillingData(ScoreResultEntity entity, ScoreResultEntity average) {
        //总分和平均值
        tvScore.setFinalScore(Float.parseFloat(keepDecimals(entity.getTotal_score(), 3)));
        tvAverage.setText(String.format(getString(R.string.format_nationaverage),
                String.valueOf(average.getTotal_score()), TimeUtils.getNowTimeString("yyyy/MM/dd HH:mm")));
        if (entity.getTotal_score() >= average.getTotal_score()) {
            playSound();
        }
        progressRed.setProgress((int) entity.getTotal_score());
        progressBlue.setProgress((int) average.getTotal_score());
        //雷达
        radarchart.setTheScore(entity, average);
        //音准
        int pitchall = (int) entity.getPitch_all();
        tvAccuracyValue.setText(String.valueOf(pitchall));//根据数组计算
        scoreViewPitch.fillingData(entity.getPitch_part());
        //节奏
        scoreviewRhythm.fillingData((int) entity.getRelative_rythm());
        //长音
        rbarLongsound.setRating(entity.getLong_tone() / 100 * 5);
        //稳定性
        scoreviewStability.fillingData(entity.getStability());
        //颤音
        rbarTrill.setRating(entity.getVibrato() / 100 * 5);
        //
        tvLongSum.setText(String.format(getString(R.string.format_trillsound),
                String.valueOf(keepDecimals(entity.getVibrato_length_sec(), 2)),
                String.valueOf(keepDecimals(entity.getVibrato_times(), 2))));
    }

    //播放庆祝声音
    private void playSound() {
        SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(getActivity(), R.raw.sound_celebrate, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {

            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                soundPool.play(1,  //声音id
                        1, //左声道
                        1, //右声道
                        1, //优先级
                        0, // 0表示不循环，-1表示循环播放
                        1);//播放比率，0.5~2，一般为1
            }
        });
    }

    public void setEventListener(ScoreResultEventListener listener) {
        mListener = listener;
    }

    public interface ScoreResultEventListener {
        void clickClose();
    }

}
