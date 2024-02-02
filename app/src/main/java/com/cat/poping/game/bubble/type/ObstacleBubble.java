package com.cat.poping.game.bubble.type;
import com.cat.poping.R;
import com.cat.poping.game.Layer;
import com.cat.poping.game.bubble.Bubble;
import com.cat.poping.game.bubble.BubbleColor;
import com.cat.poping.sound.MySoundEvent;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.entity.particles.ParticleSystem;
public class ObstacleBubble extends Bubble {

    private static final int EXPLOSION_PARTICLES = 10;

    private final ParticleSystem mExplosionParticleSystem;

    public boolean mIsObstacle = true;

    public ObstacleBubble(Game game) {
        super(game, BubbleColor.OBSTACLE);
        mExplosionParticleSystem = new ParticleSystem(game, R.drawable.wood_particle_01, EXPLOSION_PARTICLES)
                .setDurationPerParticle(600)
                .setSpeedX(-800, 800)
                .setSpeedY(-800, 800)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0, 300)
                .setScale(1, 0, 300)
                .setLayer(Layer.EFFECT_LAYER);
    }
    @Override
    public void popBubble() {
        if (mIsObstacle) {
            popObstacle();
        } else {
            super.popBubble();
        }
    }
    @Override
    public void popFloater() {
        if (mIsObstacle) {
            popObstacle();
        } else {
            super.popFloater();
        }
    }
    private void popObstacle() {
        mExplosionParticleSystem.oneShot(mX + mWidth / 2f, mY + mHeight / 2f, EXPLOSION_PARTICLES);
        setBubbleColor(BubbleColor.BLANK);
        mGame.getSoundManager().playSound(MySoundEvent.WOOD_EXPLODE);
        mIsObstacle = false;
    }
}
