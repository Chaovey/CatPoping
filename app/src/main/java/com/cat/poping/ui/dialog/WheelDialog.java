package com.cat.poping.ui.dialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import com.cat.poping.MainActivity;
import com.cat.poping.R;
import com.cat.poping.database.DatabaseHelper;
import com.cat.poping.item.prize.Prize;
import com.cat.poping.item.prize.PrizeManager;
import com.cat.poping.sound.MySoundEvent;
import com.cat.poping.timer.WheelTimer;
import com.cat.poping.ui.UIEffect;
import com.nativegame.nattyengine.ui.GameActivity;
import com.nativegame.nattyengine.ui.GameDialog;

import java.util.Random;

public class WheelDialog extends GameDialog implements View.OnClickListener,
        Animation.AnimationListener {

    private final PrizeManager mPrizeManager;
    private final WheelTimer mWheelTimer;
    private final boolean mWheelReady;

    private int mDegree;

    public WheelDialog(GameActivity activity, WheelTimer wheelTimer) {
        super(activity);
        setContentView(R.layout.dialog_wheel);
        setRootLayoutId(R.layout.dialog_container);
        setEnterAnimationId(R.anim.enter_from_center);
        mPrizeManager = new PrizeManager();
        mWheelTimer = wheelTimer;
        mWheelReady = mWheelTimer.isWheelReady();
        init();
    }

    private void init() {
        ImageButton btnCancel = (ImageButton) findViewById(R.id.btn_cancel);
        ImageButton btnPlay = (ImageButton) findViewById(R.id.btn_play);
        btnCancel.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        UIEffect.createButtonEffect(btnCancel);
        UIEffect.createButtonEffect(btnPlay);
        if (!mWheelReady) {
            btnPlay.setBackgroundResource(R.drawable.btn_play);
        }
        UIEffect.createPopUpEffect(btnCancel);
        UIEffect.createPopUpEffect(findViewById(R.id.txt_bonus), 2);
        UIEffect.createPopUpEffect(btnPlay, 4);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_cancel) {
            mParent.getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            dismiss();
        } else if (id == R.id.btn_play) {
            mParent.getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            if (mWheelReady) {
                mWheelTimer.setWheelTime(System.currentTimeMillis());
                spinWheel();
            } else {
                showAd();
            }
            view.setVisibility(View.INVISIBLE);
        }
    }

    private void spinWheel() {
        int random = new Random().nextInt(360);
        mDegree = random + 720;
        RotateAnimation wheelAnimation = new RotateAnimation(0, mDegree,
                1, 0.5f, 1, 0.5f);
        wheelAnimation.setDuration(4000);
        wheelAnimation.setFillAfter(true);
        wheelAnimation.setInterpolator(new DecelerateInterpolator());
        wheelAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
                showPrize();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        findViewById(R.id.image_wheel).startAnimation(wheelAnimation);
        RotateAnimation pointerAnimation = new RotateAnimation(0, -30,
                1, 0.5f, 1, 0.35f);
        pointerAnimation.setDuration(200);
        pointerAnimation.setRepeatMode(Animation.REVERSE);
        pointerAnimation.setRepeatCount(15);
        findViewById(R.id.image_wheel_pointer).startAnimation(pointerAnimation);
        mParent.getSoundManager().playSound(MySoundEvent.WHEEL_SPIN);
    }

    private void showPrize() {
        mDegree = mDegree % 360;
        Prize prize = getPrize(mDegree);
        savePrizes(prize.getName(), prize.getNum());
        updateCoin();
        NewBoosterDialog newBoosterDialog = new NewBoosterDialog(mParent, prize.getDrawableResId()) {
            @Override
            public void showDialog() {
                showLevel();
            }
        };
        mParent.showDialog(newBoosterDialog);
    }

    private Prize getPrize(int degree) {
        if (degree < 72) {
            return mPrizeManager.getPrize(PrizeManager.PRIZE_FIREBALL);
        } else if (degree < 144) {
            return mPrizeManager.getPrize(PrizeManager.PRIZE_BOMB);
        } else if (degree < 216) {
            return mPrizeManager.getPrize(PrizeManager.PRIZE_COIN_50);
        } else if (degree < 288) {
            return mPrizeManager.getPrize(PrizeManager.PRIZE_COLOR_BALL);
        } else {
            return mPrizeManager.getPrize(PrizeManager.PRIZE_COIN_150);
        }
    }

    private void savePrizes(String name, int amount) {
        DatabaseHelper databaseHelper = ((MainActivity) mParent).getDatabaseHelper();
        int saving = databaseHelper.getItemNum(name);
        databaseHelper.updateItemNum(name, saving + amount);
    }

    private void showAd() {
        int random = new Random().nextInt(360);
        mDegree = random + 720;
        RotateAnimation wheelAnimation = new RotateAnimation(0, mDegree,
                1, 0.5f, 1, 0.5f);
        wheelAnimation.setDuration(4000);
        wheelAnimation.setFillAfter(true);
        wheelAnimation.setInterpolator(new DecelerateInterpolator());
        wheelAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
                showPrize();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        findViewById(R.id.image_wheel).startAnimation(wheelAnimation);
        RotateAnimation pointerAnimation = new RotateAnimation(0, -30,
                1, 0.5f, 1, 0.35f);
        pointerAnimation.setDuration(200);
        pointerAnimation.setRepeatMode(Animation.REVERSE);
        pointerAnimation.setRepeatCount(15);
        findViewById(R.id.image_wheel_pointer).startAnimation(pointerAnimation);
        mParent.getSoundManager().playSound(MySoundEvent.WHEEL_SPIN);
    }

    @Override
    protected void onShow() {
        mParent.getSoundManager().playSound(MySoundEvent.SWEEP_IN);
    }

    public void updateCoin() {
    }

    public void showLevel() {
    }

}
