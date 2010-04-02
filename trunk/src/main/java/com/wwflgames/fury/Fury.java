package com.wwflgames.fury;

import com.wwflgames.fury.entity.SpriteSheetFactory;
import com.wwflgames.fury.entity.SpriteSheetProvider;
import com.wwflgames.fury.gamestate.BattleGameState;
import com.wwflgames.fury.gamestate.DungeonGameState;
import com.wwflgames.fury.gamestate.ManageDeckGameState;
import com.wwflgames.fury.gamestate.TitleGameState;
import com.wwflgames.fury.item.ItemFactoryImpl;
import com.wwflgames.fury.main.AppStateImpl;
import com.wwflgames.fury.monster.MonsterFactoryImpl;
import com.wwflgames.fury.player.PlayerFactoryImpl;
import com.wwflgames.fury.player.ProfessionFactoryImpl;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.ResourceLoader;

import java.util.HashSet;
import java.util.Set;

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
    private ProfessionFactoryImpl professionFactory;
    private PlayerFactoryImpl playerFactory;
    private SpriteSheetFactory spriteSheetFactory;
    private MonsterFactoryImpl monsterFactory;
    private AppStateImpl appState;
    private ItemFactoryImpl itemFactory;

    public Fury() {
        super("Fury");
    }

//    @Override
//    public void initStatesList(GameContainer gameContainer) throws SlickException {
//        Injector injector = Guice.createInjector(new FuryModule());
//        addState(injector.getInstance(TitleGameState.class));
//        addState(injector.getInstance(DungeonGameState.class));
//        addState(injector.getInstance(BattleGameState.class));
//        addState(injector.getInstance(ManageDeckGameState.class));
//    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        createDependencies();
        addState(createTitleGameState());
        addState(createDungeonGameState());
        addState(createBattleGameState());
        addState(createManageDeckGameState());
    }

    private void createDependencies() throws SlickException {
        itemFactory = new ItemFactoryImpl();
        professionFactory = new ProfessionFactoryImpl(itemFactory);
        playerFactory = new PlayerFactoryImpl(professionFactory);
        monsterFactory = new MonsterFactoryImpl(itemFactory);
        Set<SpriteSheetProvider> providers = new HashSet<SpriteSheetProvider>();
        providers.add(monsterFactory);
        providers.add(professionFactory);
        spriteSheetFactory = new SpriteSheetFactory(providers);
        appState = new AppStateImpl();

    }

    private TitleGameState createTitleGameState() {
        return new TitleGameState(professionFactory,playerFactory,spriteSheetFactory,
                monsterFactory,appState);
    }

    private DungeonGameState createDungeonGameState() {
        return new DungeonGameState(appState,spriteSheetFactory);
    }

    private BattleGameState createBattleGameState() {
        return new BattleGameState(appState,spriteSheetFactory,itemFactory);
    }

    private ManageDeckGameState createManageDeckGameState() {
        return new ManageDeckGameState(appState);
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
