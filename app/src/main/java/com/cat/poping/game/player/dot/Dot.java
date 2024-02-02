package com.cat.poping.game.player.dot;
import com.cat.poping.R;
import com.cat.poping.game.Layer;
import com.cat.poping.game.bubble.Bubble;
import com.cat.poping.game.bubble.BubbleColor;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.collision.Collidable;
import com.nativegame.nattyengine.collision.shape.CircleCollisionShape;
import com.nativegame.nattyengine.entity.sprite.CollidableSprite;

public class Dot extends CollidableSprite {

    public final float mMinX;
    public final float mMaxX;
    private final float mRange;

    public Dot mNextDot;
    public Bubble mCollideBubble;
    public boolean mIsCollide;

    public Dot(Game game) {
        super(game, R.drawable.dot_blue);
        int collisionBoxWidth = mWidth * 3;
        int collisionBoxHeight = mHeight * 3;
        setCollisionShape(new CircleCollisionShape(collisionBoxWidth, collisionBoxHeight));
        mMinX = collisionBoxWidth / 2f;
        mMaxX = mGame.getScreenWidth() - collisionBoxWidth / 2f;
        mRange = mMaxX - mMinX;
        mLayer = Layer.BACKGROUND_LAYER;
    }

    public void setPosition(float x, float y) {
        if (x > mMaxX) {
            float diff = x - mMaxX;
            if ((int) (diff / mRange) % 2 == 0) {
                mX = mMaxX - diff % mRange - mWidth / 2f;
            } else {
                mX = mMinX + diff % mRange - mWidth / 2f;
            }
        } else if (x < mMinX) {
            float diff = mMinX - x;
            if ((int) (diff / mRange) % 2 == 0) {
                mX = mMinX + diff % mRange - mWidth / 2f;
            } else {
                mX = mMaxX - diff % mRange - mWidth / 2f;
            }
        } else {
            mX = x - mWidth / 2f;
        }

        mY = y - mHeight / 2f;
    }

    @Override
    public void onUpdate(long elapsedMillis) {
        if (mIsCollide) {
            if (mNextDot != null) {
                mNextDot.mIsCollide = true;
            }
            mIsVisible = false;
        } else {
            mIsVisible = true;
        }
        mCollideBubble = null;
        mIsCollide = false;
    }

    @Override
    public void onCollision(Collidable otherObject) {
        if (otherObject instanceof Bubble) {
            Bubble bubble = (Bubble) otherObject;
            if (bubble.mBubbleColor != BubbleColor.BLANK) {
                mCollideBubble = bubble;
                mIsCollide = true;
            }
        }
    }

}
