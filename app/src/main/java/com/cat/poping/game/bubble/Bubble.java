package com.cat.poping.game.bubble;
import com.cat.poping.R;
import com.cat.poping.game.Layer;
import com.cat.poping.game.MyGameEvent;
import com.cat.poping.game.bubble.type.DummyBubble;
import com.cat.poping.game.bubble.type.LargeObstacleBubble;
import com.cat.poping.game.bubble.type.LockedBubble;
import com.cat.poping.game.bubble.type.ObstacleBubble;
import com.cat.poping.game.effect.FloaterEffect;
import com.cat.poping.game.effect.HintEffect;
import com.cat.poping.game.effect.ScoreEffect;
import com.cat.poping.sound.MySoundEvent;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.collision.CollisionType;
import com.nativegame.nattyengine.collision.shape.CircleCollisionShape;
import com.nativegame.nattyengine.entity.particles.ParticleSystem;
import com.nativegame.nattyengine.entity.sprite.CollidableSprite;

import java.util.Arrays;

public class Bubble extends CollidableSprite {

    private static final int EXPLOSION_PARTICLES = 6;
    public int mRow;
    public int mCol;
    public BubbleColor mBubbleColor;
    public int mDepth = -1;
    public boolean mDiscover = false;
    public final Bubble[] mEdges = new Bubble[6];

    private final ParticleSystem mExplosionParticleSystem;
    private final ParticleSystem mLightParticleSystem;
    private final ScoreEffect mScoreEffect;
    private final FloaterEffect mFloaterEffect;
    private final HintEffect mHintEffect;

    private final float mSpeedY;
    private float mScaleSpeed;
    private float mAlphaSpeed;
    private float mShiftPosition;
    private long mShiftTotalTime;
    private long mPopTotalTime;

    private boolean mShiftDown = false;
    private boolean mShiftUp = false;
    private boolean mPop = false;

    public Bubble(Game game, BubbleColor bubbleColor) {
        super(game, bubbleColor.getDrawableId());
        setCollisionShape(new CircleCollisionShape(mWidth, mHeight));
        setCollisionType(bubbleColor);
        mBubbleColor = bubbleColor;
        mSpeedY = mPixelFactor * 3000 / 1000f;
        mExplosionParticleSystem = new ParticleSystem(game, R.drawable.sparkle, EXPLOSION_PARTICLES)
                .setDurationPerParticle(800)
                .setSpeedX(-600, 600)
                .setSpeedY(-600, 600)
                .setAccelerationY(1)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0, 400)
                .setScale(1, 0, 400)
                .setLayer(Layer.EFFECT_LAYER);
        mLightParticleSystem = new ParticleSystem(game, R.drawable.circle_light, 1)
                .setDurationPerParticle(400)
                .setAlpha(255, 0, 200)
                .setScale(0, 2)
                .setLayer(Layer.EFFECT_LAYER);
        mScoreEffect = new ScoreEffect(game, bubbleColor.getScoreDrawableId());
        mFloaterEffect = new FloaterEffect(game, bubbleColor.getDrawableId());
        mHintEffect = new HintEffect(game);
        mLayer = Layer.BUBBLE_LAYER;
    }

    public void setBubbleColor(BubbleColor bubbleColor) {
        mBubbleColor = bubbleColor;
        setSpriteBitmap(bubbleColor.getDrawableId());
        setCollisionType(bubbleColor);
        if (bubbleColor != BubbleColor.BLANK) {
            mScoreEffect.setSpriteBitmap(bubbleColor.getScoreDrawableId());
            mFloaterEffect.setSpriteBitmap(bubbleColor.getDrawableId());
        }
    }

    private void setCollisionType(BubbleColor bubbleColor) {
        if (bubbleColor == BubbleColor.BLANK || bubbleColor == BubbleColor.DUMMY) {
            setCollisionType(CollisionType.NONE);
        } else {
            setCollisionType(CollisionType.PASSIVE);
        }
    }

    public void setPosition(float x, float y, int row, int col) {
        mX = x;
        mY = y;
        mRow = row;
        mCol = col;
    }

    @Override
    public void onStart() {
        mFloaterEffect.addToGame();
    }

    @Override
    public void onRemove() {
        mFloaterEffect.removeFromGame();
    }

    @Override
    public void onUpdate(long elapsedMillis) {
        checkPopBubble(elapsedMillis);
        updatePosition(elapsedMillis);
        updateShape(elapsedMillis);
    }

    private void checkPopBubble(long elapsedTimeMillis) {
        if (mPop) {
            mPopTotalTime += elapsedTimeMillis;
            if (mPopTotalTime > mDepth * 80L) {
                mExplosionParticleSystem.oneShot(mX + mWidth / 2f, mY + mHeight / 2f, EXPLOSION_PARTICLES);
                mLightParticleSystem.oneShot(mX + mWidth / 2f, mY + mHeight / 2f, 1);
                mScoreEffect.activate(mX + mWidth / 2f, mY + mHeight / 2f);
                checkLockedBubble();
                mScaleSpeed = -2 / 1000f;
                mAlphaSpeed = -500 / 1000f;
                mGame.getSoundManager().playSound(MySoundEvent.BUBBLE_POP);
                mDepth = -1;
                mPop = false;
            }
        }
    }

    private void updatePosition(long elapsedTimeMillis) {
        if (mShiftDown) {
            mShiftTotalTime += elapsedTimeMillis;
            if (mShiftTotalTime > 800) {
                mY += mSpeedY * elapsedTimeMillis;
                if (mY >= mShiftPosition) {
                    mShiftPosition = 0;
                    mShiftDown = false;
                }
            }
        }
        if (mShiftUp) {
            mY -= mSpeedY * elapsedTimeMillis;
            if (mY <= mShiftPosition) {
                mShiftPosition = 0;
                mShiftUp = false;
            }
        }
    }

    private void updateShape(long elapsedTimeMillis) {
        mScale += mScaleSpeed * elapsedTimeMillis;
        mAlpha += mAlphaSpeed * elapsedTimeMillis;
        if (mScale <= 0) {
            setBubbleColor(BubbleColor.BLANK);
            mScaleSpeed = 0;
            mAlphaSpeed = 0;
            mScale = 1;
            mAlpha = 255;
        }
    }
    public void popBubble() {
        if (mBubbleColor == BubbleColor.BLANK) {
            return;
        }
        mBubbleColor = BubbleColor.BLANK;
        gameEvent(MyGameEvent.BUBBLE_POP);
        mPopTotalTime = 0;
        mPop = true;
    }

    public void popFloater() {
        if (mBubbleColor == BubbleColor.BLANK) {
            return;
        }
        mFloaterEffect.activate(mX + mWidth / 2f, mY + mHeight / 2f);
        gameEvent(MyGameEvent.BUBBLE_POP);
        setBubbleColor(BubbleColor.BLANK);
    }

    public void checkLockedBubble() {
        for (Bubble b : mEdges) {
            if (b instanceof LockedBubble) {
                LockedBubble lockedBubble = (LockedBubble) b;
                if (lockedBubble.mIsLocked) {
                    lockedBubble.popBubble();
                }
            }
        }
    }

    public void checkObstacleBubble() {
        for (Bubble b : mEdges) {
            if (b instanceof ObstacleBubble) {
                ObstacleBubble obstacleBubble = (ObstacleBubble) b;
                if (obstacleBubble.mIsObstacle) {
                    obstacleBubble.popBubble();
                    popBubble();
                }
            } else if (b instanceof LargeObstacleBubble) {
                LargeObstacleBubble largeObstacleBubble = (LargeObstacleBubble) b;
                if (largeObstacleBubble.mIsObstacle) {
                    largeObstacleBubble.popBubble();
                    popBubble();
                }
            } else if (b instanceof DummyBubble) {
                DummyBubble dummyBubble = (DummyBubble) b;
                Bubble targetBubble = dummyBubble.mTargetBubble;
                if (targetBubble instanceof LargeObstacleBubble
                        && !Arrays.asList(mEdges).contains(targetBubble)) {
                    dummyBubble.popBubble();
                    popBubble();
                }
            }
        }
    }

    public Bubble getCollidedBubble(CollidableSprite collidableSprite) {
        if (collidableSprite.mY > mY + mHeight / 2f) {
            if (collidableSprite.mX > mX) {
                return mEdges[5];
            } else {
                return mEdges[4];
            }
        } else {
            if (collidableSprite.mX > mX) {
                return mEdges[3];
            } else {
                return mEdges[2];
            }
        }
    }

    public void shiftBubble(float shiftDistance) {
        if (shiftDistance > 0) {
            mShiftDown = true;
        } else {
            mShiftUp = true;
        }
        mShiftPosition = mY + shiftDistance;
        mShiftTotalTime = 0;
    }

    public boolean isShifting() {
        return mShiftUp || mShiftDown;
    }

    public void addHint() {
        mHintEffect.activate(mX + mWidth / 2f, mY + mHeight / 2f);
    }

    public void removeHint() {
        mHintEffect.removeFromGame();
    }

}
