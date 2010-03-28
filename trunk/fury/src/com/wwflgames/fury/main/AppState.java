package com.wwflgames.fury.main;

import com.google.inject.ImplementedBy;
import com.wwflgames.fury.map.Dungeon;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.player.Player;

@ImplementedBy(AppStateImpl.class)
public interface AppState {
    Player getPlayer();

    void setPlayer(Player player);

    DungeonMap getMap();

    boolean doesPlayerHaveInitiative();

    void setPlayerInitiative(boolean flag);

    Dungeon getDungeon();

    void setDungeon(Dungeon dungeon);
}
