package com.cat.poping.game.bubble.type;
import com.cat.poping.R;
import com.cat.poping.game.MyGameEvent;
import com.cat.poping.game.bubble.Bubble;
import com.cat.poping.game.bubble.BubbleColor;
import com.cat.poping.game.effect.BubblePopEffect;
import com.cat.poping.game.effect.ItemEffect;
import com.cat.poping.sound.MySoundEvent;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.collision.shape.CircleCollisionShape;
import com.nativegame.nattyengine.entity.sprite.CollidableSprite;
import java.util.ArrayList;
import java.util.List;
public class LargeItemBubble extends CompositeBubble {

    private static final int ITEMS_PER_BUBBLE = 5;

    private final BubblePopEffect mBubblePopEffect;
    private final List<ItemEffect> mItemEffectsPool = new ArrayList<>(ITEMS_PER_BUBBLE);

    private long mTotalTime;
    private boolean mIsItem = true;
    private boolean mCollected = false;

    public LargeItemBubble(Game game) {
        super(game, BubbleColor.LARGE_ITEM);
        mBubblePopEffect = new BubblePopEffect(game);
        for (int i = 0; i < ITEMS_PER_BUBBLE; i++) {
            mItemEffectsPool.add(new ItemEffect(game, R.drawable.nut));
        }
        mLayer++;
    }

    @Override
    public void onStart() {
        super.onStart();
        mX -= mWidth / 3f;
        mY -= mHeight / 3f;
        mBubblePopEffect.mScale = 3;
        for (Bubble b : mEdges) {
            addDummyBubble((DummyBubble) b);
        }
    }

    @Override
    public void onUpdate(long elapsedMillis) {
        super.onUpdate(elapsedMillis);
        if (mCollected) {
            mTotalTime += elapsedMillis;
            if (mTotalTime >= 150) {
                if (!mItemEffectsPool.isEmpty()) {
                    activateItemEffect();
                } else {
                    mCollected = false;
                }
                mTotalTime = 0;
            }
        }
    }

    @Override
    public void popBubble() {
        if (mIsItem) {
            popLargeItem();
        } else {
            super.popBubble();
        }
    }

    @Override
    public void popFloater() {
        if (mIsItem) {
            popLargeItem();
        } else {
            super.popFloater();
        }
    }

    private void popLargeItem() {
        mBubblePopEffect.activate(mX + mWidth / 2f, mY + mHeight / 2f);
        activateItemEffect();
        setBubbleColor(BubbleColor.BLANK);
        setCollisionShape(new CircleCollisionShape(mWidth, mHeight));
        clearDummyBubble();
        for (int i = 0; i < ITEMS_PER_BUBBLE; i++) {
            gameEvent(MyGameEvent.COLLECT_ITEM);
        }
        mGame.getSoundManager().playSound(MySoundEvent.COLLECT_ITEM);
        mX += mWidth;
        mY += mHeight;
        mLayer--;
        mCollected = true;
        mIsItem = false;
    }

    private void activateItemEffect() {
        mItemEffectsPool.remove(0)
                .activate(mX + mWidth / 2f, mY + mHeight / 2f);
    }

    @Override
    public Bubble getCollidedBubble(CollidableSprite collidableSprite) {
        if (mIsItem) {
            if (collidableSprite.mY > mY + mHeight * 3 / 4f) {
                if (collidableSprite.mX > mX + mWidth * 2 / 3f) {
                    return mEdges[5].mEdges[5];
                } else if (collidableSprite.mX > mX + mWidth / 3f) {
                    return mEdges[5].mEdges[4];
                } else {
                    return mEdges[4].mEdges[4];
                }
            } else if (collidableSprite.mY > mY + mHeight / 2f) {
                if (collidableSprite.mX > mX + mWidth / 2f) {
                    return mEdges[3].mEdges[5];
                } else {
                    return mEdges[2].mEdges[4];
                }
            } else {
                if (collidableSprite.mX > mX + mWidth / 2f) {
                    return mEdges[3].mEdges[3];
                } else {
                    return mEdges[2].mEdges[2];
                }
            }
        } else {
            return super.getCollidedBubble(collidableSprite);
        }
    }

}
