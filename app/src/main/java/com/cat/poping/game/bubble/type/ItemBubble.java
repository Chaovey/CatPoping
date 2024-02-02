package com.cat.poping.game.bubble.type;
import com.cat.poping.R;
import com.cat.poping.game.MyGameEvent;
import com.cat.poping.game.bubble.Bubble;
import com.cat.poping.game.bubble.BubbleColor;
import com.cat.poping.game.effect.BubblePopEffect;
import com.cat.poping.game.effect.ItemEffect;
import com.cat.poping.sound.MySoundEvent;
import com.nativegame.nattyengine.Game;

public class ItemBubble extends Bubble {

    private static final float ITEM_SCALE = 1.3f;

    private final ItemEffect mItemEffect;
    private final BubblePopEffect mBubblePopEffect;

    private boolean mIsItem = true;

    public ItemBubble(Game game) {
        super(game, BubbleColor.ITEM);
        mItemEffect = new ItemEffect(game, R.drawable.nut);
        mBubblePopEffect = new BubblePopEffect(game);
        mLayer++;
    }

    @Override
    public void onStart() {
        super.onStart();
        mScale = ITEM_SCALE;
        mItemEffect.mScale = ITEM_SCALE;
        mBubblePopEffect.mScale = ITEM_SCALE;
    }

    @Override
    public void popBubble() {
        if (mIsItem) {
            popItem();
        } else {
            super.popBubble();
        }
    }

    @Override
    public void popFloater() {
        if (mIsItem) {
            popItem();
        } else {
            super.popFloater();
        }
    }

    private void popItem() {
        mItemEffect.activate(mX + mWidth / 2f, mY + mHeight / 2f);
        mBubblePopEffect.activate(mX + mWidth / 2f, mY + mHeight / 2f);
        setBubbleColor(BubbleColor.BLANK);
        gameEvent(MyGameEvent.COLLECT_ITEM);
        mGame.getSoundManager().playSound(MySoundEvent.COLLECT_ITEM);
        mScale = 1;
        mIsItem = false;
    }

}
