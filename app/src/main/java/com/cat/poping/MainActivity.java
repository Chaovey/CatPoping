package com.cat.poping;
import android.os.Bundle;
import com.cat.poping.database.DatabaseHelper;
import com.cat.poping.level.MyLevelManager;
import com.cat.poping.sound.MySoundManager;
import com.cat.poping.timer.LivesTimer;
import com.cat.poping.ui.fragment.MenuFragment;
import com.nativegame.nattyengine.ui.GameActivity;

public class MainActivity extends GameActivity {

    private DatabaseHelper mDatabaseHelper;
    private LivesTimer mLivesTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AnimalsPop);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_main);
        setContainerId(R.id.container);
        setLevelManager(new MyLevelManager(this));
        setSoundManager(new MySoundManager(this));
        mDatabaseHelper = new DatabaseHelper(this);
        mLivesTimer = new LivesTimer(this);
        if (savedInstanceState == null) {
            navigateToFragment(new MenuFragment());
            getSoundManager().loadMusic(R.raw.happy_and_joyful_children);
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }

    public LivesTimer getLivesTimer() {
        return mLivesTimer;
    }

}
