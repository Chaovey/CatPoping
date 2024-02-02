package com.cat.poping.game.effect;
import com.cat.poping.game.Layer;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.entity.sprite.Sprite;

public class FloaterEffect extends Sprite {

    private final float mMinSpeedX;
    private final float mMaxSpeedX;
    private final float mMinSpeedY;
    private final float mMaxSpeedY;
    private final float mGravity;

    private float mSpeedX, mSpeedY;

    public FloaterEffect(Game game, int drawableId) {
        super(game, drawableId);
        mMinSpeedX = -mPixelFactor * 1000 / 1000f;
        mMaxSpeedX = mPixelFactor * 1000 / 1000f;
        mMinSpeedY = -mPixelFactor * 4000 / 1000f;
        mMaxSpeedY = -mPixelFactor * 7000 / 1000f;
        mGravity = mPixelFactor * 20 / 1000f;
        mLayer = Layer.EFFECT_LAYER;
    }

    public void activate(float x, float y) {
        mX = x - mWidth / 2f;
        mY = y - mHeight / 2f;
        mSpeedX = mGame.getRandom().nextFloat() * (mMaxSpeedX - mMinSpeedX) + mMinSpeedX;
        mSpeedY = mGame.getRandom().nextFloat() * (mMaxSpeedY - mMinSpeedY) + mMinSpeedY;
        mIsVisible = true;
        mIsActive = true;
    }

    @Override
    public void onStart() {
        mIsVisible = false;
        mIsActive = false;
    }

    @Override
    public void onUpdate(long elapsedMillis) {
        mX += mSpeedX * elapsedMillis;
        mY += mSpeedY * elapsedMillis;
        mSpeedY += mGravity * elapsedMillis;
        if (mY > mGame.getScreenHeight()) {
            mIsVisible = false;
            mIsActive = false;
        }
    }

}
