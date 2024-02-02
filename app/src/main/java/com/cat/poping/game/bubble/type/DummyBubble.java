package com.cat.poping.game.bubble.type;
import com.cat.poping.game.bubble.Bubble;
import com.cat.poping.game.bubble.BubbleColor;
import com.nativegame.nattyengine.Game;
public class DummyBubble extends Bubble {

    public Bubble mTargetBubble;

    public DummyBubble(Game game) {
        super(game, BubbleColor.DUMMY);
    }

    @Override
    public void popBubble() {
        if (mTargetBubble != null) {
            mTargetBubble.popBubble();
        } else {
            super.popBubble();
        }
    }

    @Override
    public void popFloater() {
        if (mTargetBubble != null) {
            return;
        }
        super.popFloater();
    }

}
