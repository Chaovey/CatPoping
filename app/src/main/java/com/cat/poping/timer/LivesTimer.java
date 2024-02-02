package com.cat.poping.timer;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.cat.poping.R;

import java.util.Locale;


public class LivesTimer {

    private static final String PREFS_NAME = "prefs_lives";
    private static final String LIVES_PREF_KEY = "lives";
    private static final String MILLIS_LEFT_PREF_KEY = "millis_left";
    private static final String END_TIME_PREF_KEY = "end_time";

    private static final long LIVES_CD = 1200000;
    private static final int LIVES_MAX = 5;

    private final Activity mActivity;
    private final SharedPreferences mPrefs;

    private TextView mTxtLives;
    private TextView mTxtTime;
    private CountDownTimer mCountDownTimer;

    private int mLivesNum;
    private long mTimeLeftInMillis;
    private long mEndTime;

    public LivesTimer(Activity activity) {
        mActivity = activity;
        mPrefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void start() {
        mTxtLives = mActivity.findViewById(R.id.txt_lives);
        mTxtTime = mActivity.findViewById(R.id.txt_lives_time);

        mLivesNum = mPrefs.getInt(LIVES_PREF_KEY, LIVES_MAX);
        if (mLivesNum == LIVES_MAX) {
            updateLivesText();
            return;
        }
        mEndTime = mPrefs.getLong(END_TIME_PREF_KEY, 0);
        mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
        if (mTimeLeftInMillis < 0) {
            long timePass = -mTimeLeftInMillis;
            long timeRemaining = timePass % LIVES_CD;
            int livesPass = 1 + (int) (timePass / LIVES_CD);
            mLivesNum += livesPass;
            mTimeLeftInMillis = LIVES_CD - timeRemaining;
            if (mLivesNum >= LIVES_MAX) {
                mLivesNum = LIVES_MAX;
                mTimeLeftInMillis = 0;
                mEndTime = 0;
            } else {
                startTimer();
            }

        } else {
            startTimer();
        }

        updateLivesText();
    }

    public void stop() {
        mPrefs.edit()
                .putInt(LIVES_PREF_KEY, mLivesNum)
                .putLong(MILLIS_LEFT_PREF_KEY, mTimeLeftInMillis)
                .putLong(END_TIME_PREF_KEY, mEndTime)
                .apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    public void addLive() {
        if (mLivesNum < LIVES_MAX) {
            mLivesNum++;
        }
        mPrefs.edit()
                .putInt(LIVES_PREF_KEY, mLivesNum)
                .apply();
    }

    public void reduceLive() {
        mLivesNum--;
        if (mTimeLeftInMillis == 0L) {
            mTimeLeftInMillis = LIVES_CD;
            mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        }
        mPrefs.edit()
                .putInt(LIVES_PREF_KEY, mLivesNum)
                .putLong(MILLIS_LEFT_PREF_KEY, mTimeLeftInMillis)
                .putLong(END_TIME_PREF_KEY, mEndTime)
                .apply();
    }

    public boolean isEnoughLives() {
        return mLivesNum > 0;
    }

    public boolean isLivesFull() {
        return mLivesNum == LIVES_MAX;
    }

    private void updateLivesText() {
        mTxtLives.setText(String.valueOf(mLivesNum));
        if (mLivesNum == 0) {
            mTxtLives.setBackgroundResource(R.drawable.lives_lock);
        } else {
            mTxtLives.setBackgroundResource(R.drawable.lives);
            if (mLivesNum == LIVES_MAX) {
                mTxtTime.setText(mActivity.getResources().getString(R.string.txt_lives_full));
            }
        }
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                if (mLivesNum < LIVES_MAX) {
                    mLivesNum++;
                    if (mLivesNum != LIVES_MAX) {
                        mTimeLeftInMillis = LIVES_CD;
                        startTimer();
                    } else {
                        mTimeLeftInMillis = 0;
                        mEndTime = 0;
                        mTxtTime.setText(mActivity.getResources().getString(R.string.txt_lives_full));
                    }
                }

                updateLivesText();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTxtTime.setText(timeLeftFormatted);
    }

}
