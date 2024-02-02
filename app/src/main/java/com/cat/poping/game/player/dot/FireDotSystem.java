package com.cat.poping.game.player.dot;
import com.cat.poping.R;
import com.cat.poping.game.player.booster.FireBubble;
import com.nativegame.nattyengine.Game;
public class FireDotSystem extends DotSystem {

    public FireDotSystem(FireBubble fireBubble, Game game) {
        super(fireBubble, game);
        setDotBitmap(R.drawable.dot_fire);
    }

    @Override
    protected void setDotPosition(Dot dot, float x, float y) {
        if (x <= dot.mMinX || x >= dot.mMaxX) {
            dot.setPosition(-dot.mWidth, -dot.mHeight);
        } else {
            dot.setPosition(x, y);
        }
    }

}
