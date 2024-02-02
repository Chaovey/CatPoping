package com.cat.poping.ui.dialog;
import android.view.View;
import android.widget.ImageButton;
import com.cat.poping.R;
import com.cat.poping.sound.MySoundEvent;
import com.cat.poping.ui.UIEffect;
import com.nativegame.nattyengine.ui.GameActivity;
import com.nativegame.nattyengine.ui.GameDialog;

public class AdCoinDialog extends GameDialog implements View.OnClickListener{

    private int mSelectedId;

    public AdCoinDialog(GameActivity activity) {
        super(activity);
        setRootLayoutId(R.layout.dialog_container);
        setEnterAnimationId(R.anim.enter_from_center);
        setExitAnimationId(R.anim.exit_to_center);
        init();
    }

    private void init() {
        ImageButton btnCancel = (ImageButton) findViewById(R.id.btn_cancel);
        ImageButton btnWatchAd = (ImageButton) findViewById(R.id.btn_watch_ad);
        btnCancel.setOnClickListener(this);
        btnWatchAd.setOnClickListener(this);
        UIEffect.createButtonEffect(btnCancel);
        UIEffect.createButtonEffect(btnWatchAd);
        UIEffect.createPopUpEffect(btnWatchAd);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_cancel) {
            mParent.getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            dismiss();
        } else if (id == R.id.btn_watch_ad) {
            mParent.getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            mSelectedId = id;
            dismiss();
        }
    }

    @Override
    protected void onShow() {
        mParent.getSoundManager().playSound(MySoundEvent.SWEEP_IN);
    }

    @Override
    protected void onDismiss() {
        mParent.getSoundManager().playSound(MySoundEvent.SWEEP_OUT);
    }

    @Override
    protected void onHide() {
        if (mSelectedId == R.id.btn_watch_ad) {

        }
    }

    public void updateCoin() {

    }

}
