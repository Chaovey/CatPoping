package com.cat.poping.level;

import com.nativegame.nattyengine.level.Level;
public class MyLevel extends Level {

    public LevelType mLevelType;
    public LevelTutorial mLevelTutorial;
    public String mBubble;
    public String mPlayer;
    public int mTarget;

    public int mScore;
    public int mStar;
    public int mMove;

    public MyLevel(int level) {
        super(level);
    }

    public void setLevelType(String type) {
        switch (type) {
            case "pop":
                mLevelType = LevelType.POP_BUBBLE;
                break;
            case "collect":
                mLevelType = LevelType.COLLECT_ITEM;
                break;
        }
    }

    public void setLevelTutorial(String tutorial) {
        switch (tutorial) {
            case "shoot":
                mLevelTutorial = LevelTutorial.SHOOT_BUBBLE;
                break;
            case "bounce":
                mLevelTutorial = LevelTutorial.BOUNCE_BUBBLE;
                break;
            case "switch":
                mLevelTutorial = LevelTutorial.SWITCH_BUBBLE;
                break;
            case "collect_items":
                mLevelTutorial = LevelTutorial.COLLECT_ITEMS;
                break;
            case "color_bubble":
                mLevelTutorial = LevelTutorial.COLOR_BUBBLE;
                break;
            case "fire_bubble":
                mLevelTutorial = LevelTutorial.FIRE_BUBBLE;
                break;
            case "bomb_bubble":
                mLevelTutorial = LevelTutorial.BOMB_BUBBLE;
                break;
            case "locked_bubble":
                mLevelTutorial = LevelTutorial.LOCKED_BUBBLE;
                break;
            case "obstacle_bubble":
                mLevelTutorial = LevelTutorial.OBSTACLE_BUBBLE;
                break;
        }
    }

}
