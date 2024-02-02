package com.cat.poping.game;
import com.nativegame.nattyengine.event.GameEvent;
public enum MyGameEvent implements GameEvent {
    SHOOT_BUBBLE,
    SWITCH_BUBBLE,
    BUBBLE_SHOT,
    BUBBLE_HIT,
    BUBBLE_POP,
    BUBBLE_CONSUMED,
    COLLECT_ITEM,
    BOOSTER_ADDED,
    BOOSTER_REMOVED,
    BOOSTER_SHOT,
    BOOSTER_CONSUMED,
    GAME_WIN,
    GAME_OVER,
    EMIT_CONFETTI,
    SHOW_WIN_DIALOG,
    ADD_EXTRA_MOVE
}
