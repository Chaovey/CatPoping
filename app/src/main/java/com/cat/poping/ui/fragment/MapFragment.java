package com.cat.poping.ui.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.cat.poping.MainActivity;
import com.cat.poping.R;
import com.cat.poping.database.DatabaseHelper;
import com.cat.poping.item.Item;
import com.cat.poping.sound.MySoundEvent;
import com.cat.poping.timer.LivesTimer;
import com.cat.poping.timer.WheelTimer;
import com.cat.poping.ui.TransitionEffect;
import com.cat.poping.ui.UIEffect;
import com.cat.poping.ui.dialog.LevelDialog;
import com.cat.poping.ui.dialog.SettingDialog;
import com.cat.poping.ui.dialog.ShopDialog;
import com.cat.poping.ui.dialog.WheelDialog;
import com.nativegame.nattyengine.ui.GameFragment;

import java.util.ArrayList;
public class MapFragment extends GameFragment implements View.OnClickListener,
        TransitionEffect.OnTransitionListener {

    private static final int TOTAL_LEVEL = 15;

    private DatabaseHelper mDatabaseHelper;
    private LivesTimer mLivesTimer;
    private WheelTimer mWheelTimer;
    private TransitionEffect mTransitionEffect;

    private ArrayList<Integer> mLevelStar;
    private int mCurrentLevel;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabaseHelper = ((MainActivity) getGameActivity()).getDatabaseHelper();
        mLivesTimer = ((MainActivity) getGameActivity()).getLivesTimer();
        mWheelTimer = new WheelTimer(getGameActivity());
        mTransitionEffect = new TransitionEffect(getGameActivity());
        mTransitionEffect.setListener(this);
        mLevelStar = mDatabaseHelper.getAllLevelStar();
        mCurrentLevel = mLevelStar.size() + 1;
        init();
    }

    @Override
    protected void onLayoutCreated() {
        if (mCurrentLevel > TOTAL_LEVEL) {
            return;
        }
        ScrollView scrollView = getView().findViewById(R.id.layout_map);
        TextView txtLevel = (TextView) findViewByName("btn_level_" + mCurrentLevel);
        scrollView.scrollTo(0, txtLevel.getBottom() - scrollView.getHeight() / 2);
    }

    private void init() {
        ImageButton btnSetting = (ImageButton) getView().findViewById(R.id.btn_setting);
        ImageButton btnShop = (ImageButton) getView().findViewById(R.id.btn_shop);
        ImageButton btnCoin = (ImageButton) getView().findViewById(R.id.btn_coin);
        ImageButton btnWheel = (ImageButton) getView().findViewById(R.id.btn_wheel);
        btnSetting.setOnClickListener(this);
        btnShop.setOnClickListener(this);
        btnCoin.setOnClickListener(this);
        btnWheel.setOnClickListener(this);
        UIEffect.createButtonEffect(btnSetting);
        UIEffect.createButtonEffect(btnShop);
        UIEffect.createButtonEffect(btnCoin);
        UIEffect.createButtonEffect(btnWheel);
        initLevelButton();
        initLevelStar();
        loadCoin();

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mWheelTimer.isWheelReady()) {
                    showWheelDialogAndShowLevel();
                } else {
                    showLevelDialog(mCurrentLevel);
                }
            }
        }, 1200);
    }

    private View findViewByName(String name) {
        int id = getResources().getIdentifier(name, "id", getGameActivity().getPackageName());
        return getView().findViewById(id);
    }

    private void initLevelButton() {
        for (int i = 1; i <= TOTAL_LEVEL; i++) {
            TextView txtLevel = (TextView) findViewByName("btn_level_" + i);
            if (i <= mCurrentLevel) {
                int level = i;
                txtLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLevelDialog(level);
                        getGameActivity().getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
                    }
                });
                txtLevel.setBackgroundResource(R.drawable.btn_level);
                txtLevel.setTextColor(getResources().getColor(R.color.brown));
                UIEffect.createButtonEffect(txtLevel);
            } else {
                txtLevel.setBackgroundResource(R.drawable.btn_level_lock);
                txtLevel.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    private void initLevelStar() {
        for (int i = 1; i <= TOTAL_LEVEL; i++) {
            ImageView imageStar = (ImageView) findViewByName("image_level_star_" + i);
            if (i < mCurrentLevel) {
                int star = mLevelStar.get(i - 1);
                switch (star) {
                    case 1:
                        imageStar.setImageResource(R.drawable.star_set_01);
                        break;
                    case 2:
                        imageStar.setImageResource(R.drawable.star_set_02);
                        break;
                    case 3:
                        imageStar.setImageResource(R.drawable.star_set_03);
                        break;
                }
                imageStar.setVisibility(View.VISIBLE);
            } else {
                imageStar.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void loadCoin() {
        TextView textCoin = (TextView) getView().findViewById(R.id.txt_coin);
        int coin = mDatabaseHelper.getItemNum(Item.COIN);
        textCoin.setText(String.valueOf(coin));
    }

    private void showLevelDialog(int level) {
        if (level > TOTAL_LEVEL) {
            return;
        }
        LevelDialog levelDialog = new LevelDialog(getGameActivity(), level) {
            @Override
            public void navigateToGame() {
                if (mLivesTimer.isEnoughLives()) {
                    super.navigateToGame();
                }
            }

            @Override
            public void startGame() {
                getGameActivity().navigateToFragment(MyGameFragment.newInstance(level));
                getGameActivity().getSoundManager().unloadMusic();
            }
        };
        showDialog(levelDialog);
    }
    private void showWheelDialog() {
        WheelDialog wheelDialog = new WheelDialog(getGameActivity(), mWheelTimer) {
            @Override
            public void updateCoin() {
                loadCoin();
            }
        };
        showDialog(wheelDialog);
    }

    private void showWheelDialogAndShowLevel() {
        WheelDialog wheelDialog = new WheelDialog(getGameActivity(), mWheelTimer) {
            @Override
            public void showLevel() {
                showLevelDialog(mCurrentLevel);
            }

            @Override
            public void updateCoin() {
                loadCoin();
            }
        };
        showDialog(wheelDialog);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_setting) {
            getGameActivity().getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            SettingDialog settingDialog = new SettingDialog(getGameActivity());
            showDialog(settingDialog);
        } else if (id == R.id.btn_shop || id == R.id.btn_coin) {
            getGameActivity().getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            ShopDialog shopDialog = new ShopDialog(getGameActivity()) {
                @Override
                public void updateMapCoin() {
                    loadCoin();
                }
            };
            showDialog(shopDialog);
        } else if (id == R.id.btn_wheel) {
            getGameActivity().getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            showWheelDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mLivesTimer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLivesTimer.stop();
    }

    @Override
    public boolean onBackPressed() {
        mTransitionEffect.show();
        return true;
    }

    @Override
    public void onTransition() {
        getGameActivity().navigateToFragment(new MenuFragment());
    }
}
