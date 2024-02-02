package com.cat.poping.game.bonus;
import com.cat.poping.game.MyGameEvent;
import com.cat.poping.game.bubble.BubbleColor;
import com.cat.poping.sound.MySoundEvent;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.entity.GameObject;

import java.util.ArrayList;
import java.util.List;
public class BonusSystem extends GameObject implements BonusBubble.BonusTimeEndListener {
    private final float mStartX;
    private final float mStartY;
    private final List<BonusBubble> mBonusBubbles = new ArrayList<>();
    private long mTotalTime;
    public BonusSystem(Game game) {
        super(game);
        mStartX = game.getScreenWidth() / 2f;
        mStartY = game.getScreenHeight() * 4 / 5f - game.getPixelFactor() * 300;
    }

    public void addBonusBubble(BubbleColor bubbleColor) {
        mBonusBubbles.add(new BonusBubble(mGame, bubbleColor));
    }

    @Override
    public void onStart() {
        BonusBubble lastBubble = mBonusBubbles.get(mBonusBubbles.size() - 1);
        lastBubble.setBonusTimeEndListener(this);
    }

    @Override
    public void onUpdate(long elapsedMillis) {
        mTotalTime += elapsedMillis;
        if (mTotalTime >= 100) {
            if (!mBonusBubbles.isEmpty()) {
                addOneBonusBubble();
            } else {
                removeFromGame();
            }
            mTotalTime = 0;
        }
    }

    private void addOneBonusBubble() {
        BonusBubble bonusBubble = mBonusBubbles.remove(0);
        bonusBubble.activate(mStartX, mStartY);
        gameEvent(MyGameEvent.BUBBLE_SHOT);
        mGame.getSoundManager().playSound(MySoundEvent.BUBBLE_HIT);
    }

    @Override
    public void onBonusTimeEnd() {
        gameEvent(MyGameEvent.SHOW_WIN_DIALOG);
    }
}
