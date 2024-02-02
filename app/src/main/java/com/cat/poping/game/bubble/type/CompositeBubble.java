package com.cat.poping.game.bubble.type;
import com.cat.poping.game.bubble.Bubble;
import com.cat.poping.game.bubble.BubbleColor;
import com.nativegame.nattyengine.Game;
import java.util.ArrayList;
import java.util.List;
public class CompositeBubble extends Bubble {

    private final List<DummyBubble> mDummyBubbles = new ArrayList<>();

    public CompositeBubble(Game game, BubbleColor bubbleColor) {
        super(game, bubbleColor);
    }

    protected void addDummyBubble(DummyBubble dummyBubble) {
        dummyBubble.mTargetBubble = this;
        mDummyBubbles.add(dummyBubble);
    }

    protected void clearDummyBubble() {
        for (DummyBubble d : mDummyBubbles) {
            d.setBubbleColor(BubbleColor.BLANK);
            d.mTargetBubble = null;
        }
    }

}
