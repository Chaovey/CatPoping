package com.cat.poping.game.counter.target;
import com.cat.poping.game.MyGameEvent;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.event.GameEvent;

public class PopTargetCounter extends TargetCounter {

    public PopTargetCounter(Game game) {
        super(game);
    }

    @Override
    protected boolean isTargetReach() {
        return mPoints == 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPoints = mTarget;
    }

    @Override
    protected void onDrawUI() {
        mText.setText(String.valueOf(mPoints));
    }

    @Override
    public void onGameEvent(GameEvent gameEvents) {
        super.onGameEvent(gameEvents);
        switch ((MyGameEvent) gameEvents) {
            case BUBBLE_POP:
                mPoints--;
                drawUI();
                if (isTargetReach()) {
                    mTargetHaveReached = true;
                }
                break;
            case BUBBLE_HIT:
                mPoints++;
                drawUI();
                break;
        }
    }

}
