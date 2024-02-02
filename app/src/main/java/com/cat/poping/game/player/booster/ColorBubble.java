package com.cat.poping.game.player.booster;
import com.cat.poping.R;
import com.cat.poping.game.Layer;
import com.cat.poping.game.bubble.Bubble;
import com.cat.poping.game.bubble.BubbleColor;
import com.cat.poping.game.bubble.BubbleSystem;
import com.cat.poping.game.player.dot.DotSystem;
import com.cat.poping.sound.MySoundEvent;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.entity.particles.ParticleSystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ColorBubble extends BoosterBubble {

    private static final int EXPLOSION_PARTICLES = 30;

    private final ParticleSystem mTrailParticleSystem;
    private final ParticleSystem mSparkleParticleSystem;
    private final ParticleSystem mExplosionParticleSystem;

    public ColorBubble(BubbleSystem bubbleSystem, Game game) {
        super(bubbleSystem, game, R.drawable.color_bubble);
        mTrailParticleSystem = new ParticleSystem(game, R.drawable.sparkle, 50)
                .setDurationPerParticle(300)
                .setEmissionRate(ParticleSystem.RATE_HIGH)
                .setAccelerationX(-2, 2)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0)
                .setScale(2, 4)
                .setLayer(mLayer - 1);
        mSparkleParticleSystem = new ParticleSystem(game, R.drawable.white_particle, 10)
                .setDurationPerParticle(400)
                .setSpeedX(-1000, 1000)
                .setSpeedY(-1000, 1000)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0)
                .setScale(1, 0.5f)
                .setLayer(mLayer - 1);
        mExplosionParticleSystem = new ParticleSystem(game, R.drawable.white_particle, EXPLOSION_PARTICLES)
                .setDurationPerParticle(600)
                .setSpeedAngle(-2500, 2500)
                .setAccelerationY(1)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0, 200)
                .setScale(1, 0, 200)
                .setLayer(Layer.EFFECT_LAYER);
        mDotSystem.setDotBitmap(R.drawable.dot_white);
    }

    @Override
    protected DotSystem getDotSystem() {
        return new DotSystem(this, mGame);
    }

    @Override
    public void onStart() {
        super.onStart();
        mTrailParticleSystem.addToGame();
        mSparkleParticleSystem.addToGame();
        mSparkleParticleSystem.emit();   // Start emitting
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
    protected void onBubbleShoot() {
        super.onBubbleShoot();
        mTrailParticleSystem.emit();
    }

    @Override
    protected void onBubbleHit(Bubble bubble) {
        if (mY >= bubble.mY
                && !mConsume) {
            Bubble newBubble = mBubbleSystem.getCollidedBubble(this, bubble);
            for (Bubble b : newBubble.mEdges) {
                if (b != null && isColoredBubble(b)) {
                    bfs(b, b.mBubbleColor);
                }
            }
            newBubble.checkLockedBubble();
            newBubble.checkObstacleBubble();
            mExplosionParticleSystem.oneShot(mX + mWidth / 2f, mY + mHeight / 2f, EXPLOSION_PARTICLES);
            mGame.getSoundManager().playSound(MySoundEvent.BOMB_EXPLODE);
            reset();
        }
    }

    public boolean isColoredBubble(Bubble bubble) {
        return bubble.mBubbleColor == BubbleColor.BLUE
                || bubble.mBubbleColor == BubbleColor.RED
                || bubble.mBubbleColor == BubbleColor.YELLOW
                || bubble.mBubbleColor == BubbleColor.GREEN;
    }

    private void bfs(Bubble root, BubbleColor color) {
        List<Bubble> removedList = new ArrayList<>();
        Queue<Bubble> queue = new LinkedList<>();
        root.mDepth = 0;
        queue.offer(root);
        while (!queue.isEmpty()) {
            Bubble currentBubble = queue.poll();
            removedList.add(currentBubble);
            for (Bubble b : currentBubble.mEdges) {
                if (b != null
                        && b.mDepth == -1
                        && b.mBubbleColor == color) {
                    b.mDepth = currentBubble.mDepth + 1;
                    queue.offer(b);
                }
            }
        }
        for (Bubble b : removedList) {
            b.popBubble();
        }
        removedList.clear();
    }

    @Override
    protected void onBubbleReset() {
        super.onBubbleReset();
        mTrailParticleSystem.stopEmit();
        mSparkleParticleSystem.stopEmit();
    }
}
