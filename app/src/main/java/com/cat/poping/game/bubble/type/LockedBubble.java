package com.cat.poping.game.bubble.type;
import com.cat.poping.R;
import com.cat.poping.game.bubble.Bubble;
import com.cat.poping.game.bubble.BubbleColor;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.entity.particles.ParticleSystem;
public class LockedBubble extends Bubble {

    private static final int EXPLOSION_PARTICLES = 8;

    private final BubbleColor mUnlockBubbleColor;
    private final ParticleSystem mExplosionParticleSystem;

    public boolean mIsLocked = true;

    public LockedBubble(Game game, BubbleColor bubbleColor) {
        super(game, BubbleColor.LOCKED);
        mUnlockBubbleColor = bubbleColor;
        mExplosionParticleSystem = new ParticleSystem(game, R.drawable.ice_particle, EXPLOSION_PARTICLES)
                .setLayer(4)
                .setDurationPerParticle(600)
                .setSpeedX(-800, 800)
                .setSpeedY(-800, 800)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0, 300)
                .setScale(1, 0, 300);
    }

    @Override
    public void popBubble() {
        if (mIsLocked) {
            unlock();
        } else {
            super.popBubble();
        }
    }

    @Override
    public void popFloater() {
        if (mIsLocked) {
            mIsLocked = false;
        }
        super.popFloater();
    }

    private void unlock() {
        mExplosionParticleSystem.oneShot(mX + mWidth / 2f, mY + mHeight / 2f, EXPLOSION_PARTICLES);
        setBubbleColor(mUnlockBubbleColor);
        mIsLocked = false;
    }

}
