package com.cat.poping.ui.dialog;
import android.widget.ImageView;
import com.cat.poping.MainActivity;
import com.cat.poping.R;
import com.cat.poping.sound.MySoundEvent;
import com.cat.poping.ui.TransitionEffect;
import com.cat.poping.ui.UIEffect;
import com.nativegame.nattyengine.ui.GameActivity;
import com.nativegame.nattyengine.ui.GameDialog;

public class LossDialog extends GameDialog implements TransitionEffect.OnTransitionListener {

    private final TransitionEffect mTransitionEffect;

    public LossDialog(GameActivity activity) {
        super(activity);
        setContentView(R.layout.dialog_loss);
        setRootLayoutId(R.layout.dialog_game_container);
        setEnterAnimationId(R.anim.enter_from_top);
        setExitAnimationId(R.anim.exit_to_bottom);
        mTransitionEffect = new TransitionEffect(activity);
        mTransitionEffect.setListener(this);
        init();
    }

    private void init() {
        ImageView imageFox = (ImageView) findViewById(R.id.image_fox);
        UIEffect.createPopUpEffect(imageFox);
        ((MainActivity) mParent).getLivesTimer().reduceLive();
        imageFox.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 1500);
    }

    @Override
    protected void onShow() {
        mParent.getSoundManager().playSound(MySoundEvent.PLAYER_LOSS);
    }

    @Override
    protected void onDismiss() {
        mParent.getSoundManager().playSound(MySoundEvent.SWEEP_OUT);
    }

    @Override
    protected void onHide() {
        mTransitionEffect.show();
    }

    @Override
    public void onTransition() {
        stopGame();
        mParent.navigateBack();
    }
    public void stopGame() {
    }
}
