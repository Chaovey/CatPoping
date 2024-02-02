package com.cat.poping.game;
import android.widget.ImageView;
import com.cat.poping.MainActivity;
import com.cat.poping.R;
import com.cat.poping.game.animal.Animal;
import com.cat.poping.game.counter.MoveCounter;
import com.cat.poping.game.counter.ProgressBarCounter;
import com.cat.poping.game.counter.ScoreCounter;
import com.cat.poping.game.counter.combo.ComboCounter;
import com.cat.poping.game.counter.target.CollectTargetCounter;
import com.cat.poping.game.counter.target.PopTargetCounter;
import com.cat.poping.game.input.InputController;
import com.cat.poping.level.LevelType;
import com.cat.poping.level.MyLevel;
import com.cat.poping.ui.dialog.PauseDialog;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.GameView;
import com.nativegame.nattyengine.ui.GameActivity;

public class MyGame extends Game {

    public MyGame(GameActivity activity, GameView gameView, int level) {
        super(activity, gameView);
        setPixelFactor(3300);
        setTouchController(new InputController(this));
        setSoundManager(getGameActivity().getSoundManager());
        setLevel(getGameActivity().getLevelManager().getLevel(level));
        new GameController(this).addToGame();
        new MoveCounter(this).addToGame();
        new ComboCounter(this).addToGame();
        new ScoreCounter(this).addToGame();
        new ProgressBarCounter(this).addToGame();
        setTargetCounter(((MyLevel) getLevel()).mLevelType);
        new Animal(this).addToGame();
    }

    private void setTargetCounter(LevelType levelType) {
        ImageView imageTarget = (ImageView) getGameActivity().findViewById(R.id.image_target);
        imageTarget.setImageResource(levelType.getTargetDrawableId());
        switch (levelType) {
            case POP_BUBBLE:
                new PopTargetCounter(this).addToGame();
                break;
            case COLLECT_ITEM:
                new CollectTargetCounter(this).addToGame();
                break;
        }
    }

    @Override
    protected void onStart() {
        getGameActivity().getSoundManager().loadMusic(R.raw.village);
    }

    @Override
    protected void onPause() {
        showPauseDialog();
    }

    @Override
    protected void onStop() {
        getGameActivity().getSoundManager().unloadMusic();
        getGameActivity().getSoundManager().loadMusic(R.raw.happy_and_joyful_children);
    }

    private void showPauseDialog() {
        PauseDialog dialog = new PauseDialog(getGameActivity(), getLevel().mLevel) {
            @Override
            public void resumeGame() {
                resume();
            }

            @Override
            public void quitGame() {
                ((MainActivity) getGameActivity()).getLivesTimer().reduceLive();
                getGameActivity().navigateBack();
            }
        };
        getGameActivity().showDialog(dialog);
    }

}
