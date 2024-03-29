package com.cat.poping.game.player.booster;
import com.cat.poping.R;
import com.cat.poping.game.bubble.Bubble;
import com.cat.poping.game.bubble.BubbleSystem;
import com.cat.poping.game.player.dot.DotSystem;
import com.cat.poping.game.player.dot.FireDotSystem;
import com.cat.poping.sound.MySoundEvent;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.entity.particles.ParticleSystem;

public class FireBubble extends BoosterBubble {

    private final ParticleSystem mTrailParticleSystem;
    private final ParticleSystem mSparkleParticleSystem;

    public FireBubble(BubbleSystem bubbleSystem, Game game) {
        super(bubbleSystem, game, R.drawable.fire_bubble);
        mTrailParticleSystem = new ParticleSystem(game, R.drawable.fire_flame, 40)
                .setDurationPerParticle(400)
                .setEmissionRate(ParticleSystem.RATE_HIGH)
                .setSpeedX(-800, 800)
                .setSpeedY(-800, 800)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0)
                .setScale(1, 2)
                .setLayer(mLayer - 1);
        mSparkleParticleSystem = new ParticleSystem(game, R.drawable.fire_particle, 10)
                .setDurationPerParticle(400)
                .setSpeedX(-1000, 1000)
                .setSpeedY(-1000, 1000)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0)
                .setScale(1, 0.5f)
                .setLayer(mLayer - 1);
        mPlayerBubbleBg.setSpriteBitmap(R.drawable.fire_bubble_bg);
    }

    @Override
    protected DotSystem getDotSystem() {
        return new FireDotSystem(this, mGame);
    }

    @Override
    public void onStart() {
        super.onStart();
        mTrailParticleSystem.addToGame();
        mSparkleParticleSystem.addToGame();
        mTrailParticleSystem.emit();   // Start emitting
        mSparkleParticleSystem.emit();
    }

    @Override
    public void onRemove() {
        super.onRemove();
        mTrailParticleSystem.removeFromGame();
        mSparkleParticleSystem.removeFromGame();
    }

    @Override
    public void onUpdate(long elapsedMillis) {
        super.onUpdate(elapsedMillis);
        mTrailParticleSystem.setEmissionPosition(mX + mWidth / 2f, mY + mHeight / 2f);
        mSparkleParticleSystem.setEmissionPosition(mX + mWidth / 2f, mY + mHeight / 2f);
    }

    @Override
    protected void bounceRight() {
        if (mX <= -mWidth) {
            reset();
        }
    }

    @Override
    protected void bounceLeft() {
        if (mX >= mGame.getScreenWidth()) {
            reset();
        }
    }

    @Override
    protected void onBubbleShoot() {
        super.onBubbleShoot();
        mGame.getSoundManager().playSound(MySoundEvent.FIRE_BUBBLE_SHOOT);
    }

    @Override
    protected void onBubbleHit(Bubble bubble) {
        if (mY >= 0) {
            bubble.popBubble();
        }
    }

    @Override
    protected void onBubbleReset() {
        super.onBubbleReset();
        mTrailParticleSystem.stopEmit();
        mSparkleParticleSystem.stopEmit();
    }

}
