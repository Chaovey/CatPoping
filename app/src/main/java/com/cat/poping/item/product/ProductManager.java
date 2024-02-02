package com.cat.poping.item.product;
import android.app.Activity;

import com.cat.poping.R;
import com.cat.poping.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {

    private final List<Product> mProductList = new ArrayList<>();

    public ProductManager(Activity activity) {
        Product productColorBall = new Product(Item.COLOR_BALL, 50);
        Product productFireball = new Product(Item.FIREBALL, 60);
        Product productBomb = new Product(Item.BOMB, 70);

        productColorBall.setView(R.drawable.product_color_ball, R.drawable.btn_price_50);
        productFireball.setView(R.drawable.product_fireball, R.drawable.btn_price_60);
        productBomb.setView(R.drawable.product_bomb, R.drawable.btn_price_70);

        productColorBall.setDescription(activity.getString(R.string.txt_color_ball));
        productFireball.setDescription(activity.getString(R.string.txt_fireball));
        productBomb.setDescription(activity.getString(R.string.txt_bomb));

        mProductList.add(productColorBall);
        mProductList.add(productFireball);
        mProductList.add(productBomb);
    }

    public List<Product> getAllProducts() {
        return mProductList;
    }

}
