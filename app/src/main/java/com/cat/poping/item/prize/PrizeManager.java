package com.cat.poping.item.prize;
import com.cat.poping.R;
import com.cat.poping.item.Item;

import java.util.HashMap;

public class PrizeManager {

    private final HashMap<String, Prize> mPrizeMap = new HashMap<>();
    public static final String PRIZE_COIN_50 = "prize_coin_50";
    public static final String PRIZE_COIN_150 = "prize_coin_150";
    public static final String PRIZE_COLOR_BALL = "prize_color_ball";
    public static final String PRIZE_FIREBALL = "prize_fireball";
    public static final String PRIZE_BOMB = "prize_bomb";

    public PrizeManager() {
        Prize prizeCoin50 = new Prize(Item.COIN, 50);
        Prize prizeCoin150 = new Prize(Item.COIN, 150);
        Prize prizeColorBall = new Prize(Item.COLOR_BALL, 1);
        Prize prizeFireball = new Prize(Item.FIREBALL, 1);
        Prize prizeBomb = new Prize(Item.BOMB, 1);
        prizeCoin50.setView(R.drawable.product_coin_50);
        prizeCoin150.setView(R.drawable.product_coin_150);
        prizeColorBall.setView(R.drawable.product_color_ball);
        prizeFireball.setView(R.drawable.product_fireball);
        prizeBomb.setView(R.drawable.product_bomb);
        mPrizeMap.put(PRIZE_COIN_50, prizeCoin50);
        mPrizeMap.put(PRIZE_COIN_150, prizeCoin150);
        mPrizeMap.put(PRIZE_COLOR_BALL, prizeColorBall);
        mPrizeMap.put(PRIZE_FIREBALL, prizeFireball);
        mPrizeMap.put(PRIZE_BOMB, prizeBomb);
    }

    public Prize getPrize(String key) {
        return mPrizeMap.get(key);
    }

}
