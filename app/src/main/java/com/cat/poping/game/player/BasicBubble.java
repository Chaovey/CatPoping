package com.cat.poping.game.player;
import android.content.Context;
import com.cat.poping.R;
import com.cat.poping.game.Layer;
import com.cat.poping.game.MyGameEvent;
import com.cat.poping.game.bubble.Bubble;
import com.cat.poping.game.bubble.BubbleColor;
import com.cat.poping.game.bubble.BubbleSystem;
import com.cat.poping.game.player.dot.DotSystem;
import com.cat.poping.level.MyLevel;
import com.cat.poping.sound.MySoundEvent;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.entity.sprite.Sprite;
import com.nativegame.nattyengine.entity.particles.ParticleSystem;
import com.nativegame.nattyengine.event.GameEvent;

public class BasicBubble extends PlayerBubble {

    private final BubbleQueue mBubbleQueue;
    private final NextBubble mNextBubble;
    private final CircleBg mCircleBg;
    private final ParticleSystem mTrailParticleSystem;
    private final boolean mHintEnable;

    public BubbleColor mBubbleColor;
    private boolean mDetectedCollision;

    public BasicBubble(BubbleSystem bubbleSystem, Game game) {
        super(bubbleSystem, game, BubbleColor.BLANK.getDrawableId());
        mBubbleQueue = new BubbleQueue(((MyLevel) game.getLevel()).mPlayer.toCharArray());
        mNextBubble = new NextBubble(game);
        mCircleBg = new CircleBg(game);
        mTrailParticleSystem = new ParticleSystem(game, R.drawable.sparkle, 50)
                .setDurationPerParticle(300)
                .setEmissionRate(ParticleSystem.RATE_HIGH)
                .setAccelerationX(-2, 2)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0)
                .setScale(2, 4)
                .setLayer(mLayer - 1);
        mHintEnable = game.getGameActivity().getSharedPreferences("prefs_setting", Context.MODE_PRIVATE)
                .getBoolean("hint", true);
    }

    public void setBubbleColor(BubbleColor bubbleColor) {
        mBubbleColor = bubbleColor;
        setSpriteBitmap(bubbleColor.getDrawableId());
    }

    public BubbleQueue getBubbleQueue() {
        return mBubbleQueue;
    }

    @Override
    protected DotSystem getDotSystem() {
        return new DotSystem(this, mGame);
    }

    @Override
    public void onStart() {
        super.onStart();
        mCircleBg.addToGame();
        mNextBubble.addToGame();
        mTrailParticleSystem.addToGame();
        updateBubbleColor();
    }

    @Override
    public void onRemove() {
        super.onRemove();
        mNextBubble.removeFromGame();
        mTrailParticleSystem.removeFromGame();
    }

    @Override
    public void onUpdate(long elapsedMillis) {
        super.onUpdate(elapsedMillis);
        mTrailParticleSystem.setEmissionPosition(mX + mWidth / 2f, mY + mHeight / 2f);
    }

    @Override
    protected void onBubbleShoot() {
        mBubbleQueue.popBubble();
        mTrailParticleSystem.emit();
        gameEvent(MyGameEvent.BUBBLE_SHOT);
        mGame.getSoundManager().playSound(MySoundEvent.BUBBLE_SHOOT);
        mDetectedCollision = false;
    }

    @Override
    protected void onBubbleSwitch() {
        mBubbleQueue.switchBubble();
        updateBubbleColor();
        mGame.getSoundManager().playSound(MySoundEvent.BUBBLE_SWITCH);
    }

    @Override
    protected void onBubbleHit(Bubble bubble) {
        if (mY >= bubble.mY
                && !mDetectedCollision) {
            gameEvent(MyGameEvent.BUBBLE_HIT);
            mBubbleSystem.addCollidedBubble(this, bubble);
            mDetectedCollision = true;
            mGame.getSoundManager().playSound(MySoundEvent.BUBBLE_HIT);
            reset();
        }
    }

    @Override
    protected void onBubbleReset() {
        mX = mStartX - mWidth / 2f;
        mY = mStartY - mHeight / 2f;
        mTrailParticleSystem.stopEmit();
        updateBubbleColor();
        gameEvent(MyGameEvent.BUBBLE_CONSUMED);
    }

    private void updateBubbleColor() {
        // Update current bubble
        BubbleColor bubbleColor = mBubbleQueue.getBubble();
        if (bubbleColor != null) {
            setBubbleColor(bubbleColor);
            mDotSystem.setDotBitmap(bubbleColor.getDotDrawableId());
        } else {
            hide();
        }
        // Update next bubble
        BubbleColor nextBubbleColor = mBubbleQueue.getNextBubble();
        if (nextBubbleColor != null) {
            mNextBubble.setBubbleColor(nextBubbleColor);
        } else {
            mNextBubble.hide();
        }
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        super.onGameEvent(gameEvent);
        switch ((MyGameEvent) gameEvent) {
            case BOOSTER_ADDED:
                BubbleColor currentBubbleColor = mBubbleQueue.getBubble();
                if (currentBubbleColor != null) {
                    mNextBubble.setBubbleColor(currentBubbleColor);
                }
                hide();
                setEnable(false);
                break;
            case BOOSTER_REMOVED:
            case BOOSTER_CONSUMED:
                BubbleColor nextBubbleColor = mBubbleQueue.getNextBubble();
                if (nextBubbleColor != null) {
                    mNextBubble.setBubbleColor(nextBubbleColor);
                }
                show();
                setEnable(true);
                break;
            case ADD_EXTRA_MOVE:
                addExtraBubble();
                break;
        }
    }

    private void show() {
        mIsVisible = true;
        mPlayerBubbleBg.mIsVisible = true;
    }

    private void hide() {
        mIsVisible = false;
        mPlayerBubbleBg.mIsVisible = false;
    }

    @Override
    public void showHint() {
        if (mHintEnable) {
            mBubbleSystem.addBubbleHint(mBubbleColor);
        }
    }

    @Override
    public void removeHint() {
        if (mHintEnable) {
            mBubbleSystem.removeBubbleHint(mBubbleColor);
        }
    }

    private void addExtraBubble() {
        mBubbleQueue.initExtraBubble();
        updateBubbleColor();
        show();
        mNextBubble.show();
    }

    private class NextBubble extends Sprite {

        public NextBubble(Game game) {
            super(game, BubbleColor.BLANK.getDrawableId());
            mLayer = Layer.BUBBLE_LAYER;
        }

        public void setBubbleColor(BubbleColor bubbleColor) {
            setSpriteBitmap(bubbleColor.getDrawableId());
        }

        @Override
        public void onStart() {
            float distance = mPixelFactor * 300;
            mX = mStartX + distance - mWidth / 2f;
            mY = mStartY + distance - mHeight / 2f;
            mScale = 0.8f;
        }

        @Override
        public void onUpdate(long elapsedMillis) {
        }

        private void show() {
            mIsVisible = true;
        }

        private void hide() {
            mIsVisible = false;
        }

    }

}
