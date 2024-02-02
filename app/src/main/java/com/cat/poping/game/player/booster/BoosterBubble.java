package com.cat.poping.game.player.booster;
import com.cat.poping.game.MyGameEvent;
import com.cat.poping.game.bubble.BubbleSystem;
import com.cat.poping.game.player.PlayerBubble;
import com.cat.poping.sound.MySoundEvent;
import com.nativegame.nattyengine.Game;

public abstract class BoosterBubble extends PlayerBubble {

    protected boolean mConsume;

    protected BoosterBubble(BubbleSystem bubbleSystem, Game game, int drawableId) {
        super(bubbleSystem, game, drawableId);
    }

    @Override
    public void onStart() {
        super.onStart();
        mConsume = false;
        gameEvent(MyGameEvent.BOOSTER_ADDED);
        mGame.getSoundManager().playSound(MySoundEvent.ADD_BOOSTER);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (mConsume) {
            gameEvent(MyGameEvent.BOOSTER_CONSUMED);
        } else {
            gameEvent(MyGameEvent.BOOSTER_REMOVED);
        }
    }

    @Override
    protected void onBubbleShoot() {
        gameEvent(MyGameEvent.BOOSTER_SHOT);
        mGame.getSoundManager().playSound(MySoundEvent.BUBBLE_SHOOT);
    }

    @Override
    protected void onBubbleSwitch() {
    }

    @Override
    protected void onBubbleReset() {
        mBubbleSystem.popFloater();
        mBubbleSystem.shiftBubble();
        mConsume = true;
        removeFromGame();
    }

}
