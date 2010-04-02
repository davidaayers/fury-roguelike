package com.wwflgames.fury;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.wwflgames.fury.gamestate.BattleGameState;
import com.wwflgames.fury.gamestate.DungeonGameState;
import com.wwflgames.fury.gamestate.ManageDeckGameState;
import com.wwflgames.fury.gamestate.TitleGameState;
import com.wwflgames.fury.guice.FuryModule;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Fury: A Roguelike game
 * For information, contact david@wwflgames.com
 * Copyright (C) 2010 WWFL Games
 * <p/>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
public class Fury extends StateBasedGame {

    public static final int TITLE_STATE = 1;
    public static final int DUNGEON_GAME_STATE = 2;
    public static final int BATTLE_STATE = 3;
    public static final int MANAGE_DECK_STATE = 4;

    private static AppGameContainer container;

    public Fury() {
        super("Fury");
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        Injector injector = Guice.createInjector(new FuryModule());
        addState(injector.getInstance(TitleGameState.class));
        addState(injector.getInstance(DungeonGameState.class));
        addState(injector.getInstance(BattleGameState.class));
        addState(injector.getInstance(ManageDeckGameState.class));
    }

    public static void main(String[] args) {
        ResourceLoader.removeAllResourceLocations();
        ResourceLoader.addResourceLocation(new ClasspathLocation());

        try {
            container = new AppGameContainer(new Fury());
            container.setDisplayMode(800, 600, false);
            container.setShowFPS(true);
            container.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
