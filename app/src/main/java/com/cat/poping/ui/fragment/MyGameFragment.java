package com.cat.poping.ui.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.cat.poping.R;
import com.cat.poping.game.MyGame;
import com.cat.poping.sound.MySoundEvent;
import com.cat.poping.ui.UIEffect;
import com.nativegame.nattyengine.Game;
import com.nativegame.nattyengine.GameView;
import com.nativegame.nattyengine.ui.GameFragment;

public class MyGameFragment extends GameFragment implements View.OnClickListener {

    private static final String LEVEL = "level";

    private Game mGame;
    private int mLevel;

    public MyGameFragment() {

    }

    public static MyGameFragment newInstance(int level) {
        MyGameFragment fragment = new MyGameFragment();
        Bundle args = new Bundle();
        args.putInt(LEVEL, level);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLevel = getArguments().getInt(LEVEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    protected void onLayoutCreated() {
        ImageButton btnPause = (ImageButton) getGameActivity().findViewById(R.id.btn_pause);
        UIEffect.createButtonEffect(btnPause);
        btnPause.setOnClickListener(this);
        mGame = new MyGame(getGameActivity(), (GameView) getView().findViewById(R.id.game_view), mLevel);
        mGame.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGame.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGame.stop();
    }

    @Override
    public boolean onBackPressed() {
        mGame.pause();
        return true;
    }

    @Override
    public void onClick(View view) {
        getGameActivity().getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
        mGame.pause();
    }

}
