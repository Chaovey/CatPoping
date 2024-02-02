package com.cat.poping.game.counter;
import android.widget.TextView;

import com.cat.poping.R;
import com.cat.poping.game.MyGameEvent;
import com.cat.poping.level.MyLevel;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.entity.UIGameObject;
import com.nativegame.nattyengine.event.GameEvent;


public class MoveCounter extends UIGameObject {

    private static final int EXTRA_MOVES = 5;

    private final MyLevel mLevel;
    private final TextView mText;

    private int mMoves;

    public MoveCounter(Game game) {
        super(game);
        mLevel = (MyLevel) game.getLevel();
        mText = (TextView) game.getGameActivity().findViewById(R.id.txt_move);
    }

    @Override
    public void onStart() {
        mMoves = mLevel.mMove;
        drawUI();
    }

    @Override
    public void onUpdate(long elapsedMillis) {
    }

    @Override
    protected void onDrawUI() {
        mText.setText(String.valueOf(mMoves));
    }

    @Override
    public void onGameEvent(GameEvent gameEvents) {
        switch ((MyGameEvent) gameEvents) {
            case BUBBLE_SHOT:
                mMoves--;
                mLevel.mMove = mMoves;
                drawUI();
                break;
            case ADD_EXTRA_MOVE:
                mMoves += EXTRA_MOVES;
                mLevel.mMove = mMoves;
                drawUI();
                break;
        }
    }

}
