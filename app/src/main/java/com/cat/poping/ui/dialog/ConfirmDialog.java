package com.cat.poping.ui.dialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cat.poping.MainActivity;
import com.cat.poping.R;
import com.cat.poping.database.DatabaseHelper;
import com.cat.poping.item.Item;
import com.cat.poping.item.product.Product;
import com.cat.poping.sound.MySoundEvent;
import com.cat.poping.ui.UIEffect;
import com.nativegame.nattyengine.ui.GameActivity;
import com.nativegame.nattyengine.ui.GameDialog;
public class ConfirmDialog extends GameDialog implements View.OnClickListener {
    private final Product mProduct;
    private final DatabaseHelper mDatabaseHelper;
    private int mSelectedId;
    public ConfirmDialog(GameActivity activity, Product product) {
        super(activity);
        setContentView(R.layout.dialog_confirm);
        setRootLayoutId(R.layout.dialog_container);
        setEnterAnimationId(R.anim.enter_from_center);
        setExitAnimationId(R.anim.exit_to_center);
        mProduct = product;
        mDatabaseHelper = ((MainActivity) activity).getDatabaseHelper();
        init();
    }
    private void init() {
        ImageView imageProduct = (ImageView) findViewById(R.id.image_product);
        imageProduct.setImageResource(mProduct.getDrawableId());
        ImageButton btnCancel = (ImageButton) findViewById(R.id.btn_cancel);
        ImageButton btnConfirm = (ImageButton) findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        UIEffect.createButtonEffect(btnCancel);
        UIEffect.createButtonEffect(btnConfirm);
        UIEffect.createPopUpEffect(btnConfirm);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_cancel) {
            mParent.getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            dismiss();
        } else if (id == R.id.btn_confirm) {
            mParent.getSoundManager().playSound(MySoundEvent.BUTTON_CLICK);
            mSelectedId = id;
            buy();
            dismiss();
        }
    }
    private void buy() {
        int price = mProduct.getPrice();
        int saving = mDatabaseHelper.getItemNum(Item.COIN);
        if (saving < price) {
            mSelectedId = R.id.btn_watch_ad;
            return;
        }
        mDatabaseHelper.updateItemNum(Item.COIN, saving - price);
        int num = mDatabaseHelper.getItemNum(mProduct.getName());
        mDatabaseHelper.updateItemNum(mProduct.getName(), num + 1);
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
            AdCoinDialog adCoinDialog = new AdCoinDialog(mParent);
            mParent.showDialog(adCoinDialog);
        } else if (mSelectedId == R.id.btn_confirm) {
            NewBoosterDialog newBoosterDialog = new NewBoosterDialog(mParent, mProduct.getDrawableId());
            mParent.showDialog(newBoosterDialog);
            updateCoin();
        }
    }
    public void updateCoin() {

    }
}
