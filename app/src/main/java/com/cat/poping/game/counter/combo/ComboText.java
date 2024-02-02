package com.cat.poping.game.counter.combo;
import android.view.animation.OvershootInterpolator;
import com.cat.poping.R;
import com.cat.poping.game.Layer;
import com.cat.poping.sound.MySoundEvent;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.entity.sprite.Sprite;
public class ComboText extends Sprite {

    private static final int TIME_TO_SHRINK = 300;

    private final float mAlphaSpeed;

    private final OvershootInterpolator mInterpolator = new OvershootInterpolator();

    private long mTotalTime;

    public ComboText(Game game) {
        super(game, R.drawable.text_wow);
        mAlphaSpeed = -800 / 1000f;
        mLayer = Layer.TEXT_LAYER;
    }

    public void activate(Combo combo) {
        mX = mGame.getScreenWidth() / 2f - mWidth / 2f;
        mY = mGame.getScreenHeight() / 3f - mHeight / 2f;
        mScale = 5;
        mAlpha = 255;
        setSpriteBitmap(combo.getDrawableId());
        addToGame();
        mGame.getSoundManager().playSound(MySoundEvent.COMBO);
        mTotalTime = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis) {
        mTotalTime += elapsedMillis;
        if (mTotalTime >= 1300) {
            removeFromGame();
            mTotalTime = 0;
        } else {
            if (mTotalTime <= TIME_TO_SHRINK) {
                mScale = 5 - 3 * mInterpolator.getInterpolation(mTotalTime * 1f / TIME_TO_SHRINK);
            } else if (mTotalTime >= 1000) {
                mAlpha += mAlphaSpeed * elapsedMillis;
            }
        }
    }

}
