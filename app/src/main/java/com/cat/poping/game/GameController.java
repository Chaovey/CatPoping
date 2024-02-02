package com.cat.poping.game;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import com.cat.poping.R;
import com.cat.poping.game.bonus.BonusSystem;
import com.cat.poping.game.bubble.BubbleSystem;
import com.cat.poping.game.effect.WinTextEffect;
import com.cat.poping.game.player.BasicBubble;
import com.cat.poping.game.player.BubbleQueue;
import com.cat.poping.game.player.booster.BoosterManager;
import com.cat.poping.level.MyLevel;
import com.cat.poping.sound.MySoundEvent;
import com.cat.poping.ui.dialog.AdExtraMoveDialog;
import com.cat.poping.ui.dialog.LossDialog;
import com.cat.poping.ui.dialog.StartDialog;
import com.cat.poping.ui.dialog.TutorialDialog;
import com.cat.poping.ui.dialog.WinDialog;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.entity.GameObject;
import com.nativegame.nattyengine.entity.particles.ParticleSystem;
import com.nativegame.nattyengine.event.GameEvent;
import com.nativegame.nattyengine.ui.GameActivity;
public class GameController extends GameObject {
    private static final String PREFS_NAME = "prefs_tutorial";
    private final GameActivity mParent;
    private final BubbleSystem mBubbleSystem;
    private final BonusSystem mBonusSystem;
    private final BasicBubble mBasicBubble;
    private final BoosterManager mBoosterManager;
    private final WinTextEffect mWinText;
    private final ParticleSystem mLeftConfetti;
    private final ParticleSystem mRightConfetti;
    private GameControllerState mState;
    private long mTotalMillis;
    private boolean mExtraLives = true;
    public GameController(Game game) {
        super(game);
        mParent = game.getGameActivity();
        mBubbleSystem = new BubbleSystem(game);
        mBonusSystem = new BonusSystem(game);
        mBasicBubble = new BasicBubble(mBubbleSystem, game);
        mBoosterManager = new BoosterManager(mBubbleSystem, game);
        mWinText = new WinTextEffect(game);
        int[] confettiId = new int[]{
                R.drawable.confetti_blue,
                R.drawable.confetti_green,
                R.drawable.confetti_pink,
                R.drawable.confetti_yellow};
        mLeftConfetti = new ParticleSystem(game, confettiId, 50)
                .setDurationPerParticle(1500)
                .setEmissionRate(30)
                .setEmissionPositionX(0)
                .setEmissionRangeY(game.getScreenHeight() / 3f, game.getScreenHeight() * 3 / 4f)
                .setSpeedX(1000, 1500)
                .setSpeedY(-4000, -3000)
                .setAccelerationX(-2, 0)
                .setAccelerationY(5, 10)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0, 500)
                .setScale(0.75f, 0, 1000)
                .setLayer(Layer.EFFECT_LAYER);
        mRightConfetti = new ParticleSystem(game, confettiId, 50)
                .setDurationPerParticle(1500)
                .setEmissionRate(30)
                .setEmissionPositionX(game.getScreenWidth())
                .setEmissionRangeY(game.getScreenHeight() / 3f, game.getScreenHeight() * 3 / 4f)
                .setSpeedX(-1500, -1000)
                .setSpeedY(-4000, -3000)
                .setAccelerationX(0, 2)
                .setAccelerationY(5, 10)
                .setInitialRotation(0, 360)
                .setRotationSpeed(-720, 720)
                .setAlpha(255, 0, 500)
                .setScale(0.75f, 0, 1000)
                .setLayer(Layer.EFFECT_LAYER);
    }
    @Override
    public void onStart() {
        mState = GameControllerState.SHIFT_BUBBLE;
        mTotalMillis = 0;
        mLeftConfetti.addToGame();
        mRightConfetti.addToGame();
    }
    @Override
    public void onUpdate(long elapsedMillis) {
        switch (mState) {
            case WAITING:
                break;
            case SHIFT_BUBBLE:
                mTotalMillis += elapsedMillis;
                if (mTotalMillis >= 1000) {
                    mBubbleSystem.shiftBubble();
                    mState = GameControllerState.START_INTRO;
                    mTotalMillis = 0;
                }
                break;
            case START_INTRO:
                if (mBubbleSystem.isShifting()) {
                    return;
                }
                mBasicBubble.addToGame();
                showStartDialog();
                mState = GameControllerState.WAITING;
                break;
            case PLAYER_WIN:
                if (mBubbleSystem.isShifting()) {
                    return;
                }
                mTotalMillis += elapsedMillis;
                if (mTotalMillis >= 1000) {
                    mBasicBubble.removeFromGame();
                    mBubbleSystem.clearBubble();
                    hideBooster();
                    mWinText.activate();
                    createConfetti(2000);
                    mGame.getSoundManager().playSound(MySoundEvent.PLAYER_WIN);
                    mState = GameControllerState.BONUS_TIME;
                    mTotalMillis = 0;
                }
                break;
            case PLAYER_LOSS:
                if (mBubbleSystem.isShifting()) {
                    return;
                }
                mTotalMillis += elapsedMillis;
                if (mTotalMillis >= 500) {
                    if (mExtraLives) {
                        showExtraMoveDialog();
                        mExtraLives = false;
                        mState = GameControllerState.WAITING;
                        mTotalMillis = 0;
                        return;
                    }
                    showLossDialog();
                    mState = GameControllerState.WAITING;
                    mTotalMillis = 0;
                }
                break;
            case BONUS_TIME:
                mTotalMillis += elapsedMillis;
                if (mTotalMillis >= 1200) {
                    BubbleQueue bubbleQueue = mBasicBubble.getBubbleQueue();
                    while (bubbleQueue.hasBubble()) {
                        mBonusSystem.addBonusBubble(bubbleQueue.popBubble());
                    }
                    mBonusSystem.addToGame();
                    mState = GameControllerState.WAITING;
                    mTotalMillis = 0;
                }
                break;
        }
    }
    @Override
    public void onGameEvent(GameEvent gameEvent) {
        switch ((MyGameEvent) gameEvent) {
            case BOOSTER_CONSUMED:
                mBoosterManager.consumeBooster();
                break;
            case EMIT_CONFETTI:
                createConfetti(800);
                break;
            case GAME_WIN:
                mBasicBubble.setEnable(false);
                mBoosterManager.setEnable(false);
                mState = GameControllerState.PLAYER_WIN;
                break;
            case GAME_OVER:
                mBasicBubble.setEnable(false);
                mBoosterManager.setEnable(false);
                mState = GameControllerState.PLAYER_LOSS;
                break;
            case SHOW_WIN_DIALOG:
                hideState();
                showWinDialog();
                break;
            case ADD_EXTRA_MOVE:
                mBasicBubble.setEnable(true);
                mBoosterManager.setEnable(true);
                break;
        }
    }
    private void createConfetti(long duration) {
        mLeftConfetti.setDuration(duration).emit();
        mRightConfetti.setDuration(duration).emit();
    }
    private void hideBooster() {
        mParent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mParent.findViewById(R.id.layout_booster).setVisibility(View.INVISIBLE);
            }
        });
    }
    private void hideState() {
        mParent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mParent.findViewById(R.id.layout_state).setVisibility(View.INVISIBLE);
                mParent.findViewById(R.id.txt_move).setVisibility(View.INVISIBLE);
            }
        });
    }
    private void showStartDialog() {
        mParent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StartDialog startDialog = new StartDialog(mParent, (MyLevel) mGame.getLevel()) {
                    @Override
                    public void startGame() {
                        mBasicBubble.setEnable(true);
                        mBoosterManager.setEnable(true);
                        if (((MyLevel) mGame.getLevel()).mLevelTutorial != null) {
                            showTutorialDialog();
                        }
                    }
                };
                mParent.showDialog(startDialog);
                mParent.findViewById(R.id.txt_move).setVisibility(View.VISIBLE);
            }
        });
    }
    private void showTutorialDialog() {
        SharedPreferences prefs = mParent
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String prefsKey = "level" + mGame.getLevel().mLevel;
        if (!prefs.getBoolean(prefsKey, true)) {
            return;
        } else {
            prefs.edit()
                    .putBoolean(prefsKey, false)
                    .apply();
        }
        mParent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TutorialDialog tutorialDialog = new TutorialDialog(mParent,
                        (MyLevel) mGame.getLevel()) {
                    @Override
                    public void updateBooster() {
                        mBoosterManager.initBoosterText();
                    }
                };
                mParent.showDialog(tutorialDialog);
            }
        });
    }
    private void showWinDialog() {
        mParent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WinDialog winDialog = new WinDialog(mParent, (MyLevel) mGame.getLevel()) {
                    @Override
                    public void stopGame() {
                        mGame.stop();
                    }
                };
                mParent.showDialog(winDialog);
            }
        });
    }
    private void showLossDialog() {
        mParent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LossDialog lossDialog = new LossDialog(mParent) {
                    @Override
                    public void stopGame() {
                        mGame.stop();
                    }
                };
                mParent.showDialog(lossDialog);
            }
        });
    }
    private void showExtraMoveDialog() {
        mParent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AdExtraMoveDialog extraMoveDialog = new AdExtraMoveDialog(mParent) {
                    @Override
                    public void showAd() {
                        showRewardedAd();
                    }
                };
                mParent.showDialog(extraMoveDialog);
            }
        });
    }
    private void showRewardedAd() {
        gameEvent(MyGameEvent.GAME_OVER);
    }
}
