package com.wwflgames.fury;

import com.wwflgames.fury.card.CardFactory;
import com.wwflgames.fury.entity.SpriteSheetFactory;
import com.wwflgames.fury.entity.SpriteSheetProvider;
import com.wwflgames.fury.gamestate.*;
import com.wwflgames.fury.main.AppStateImpl;
import com.wwflgames.fury.monster.MonsterFactoryImpl;
import com.wwflgames.fury.player.PlayerFactoryImpl;
import com.wwflgames.fury.player.ProfessionFactoryImpl;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
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

    public static final int GAME_WIDTH = 750;
    public static final int GAME_HEIGHT = 600;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int MAP_WIDTH = GAME_WIDTH/TILE_WIDTH;
    public static final int MAP_HEIGHT = GAME_HEIGHT/TILE_HEIGHT;

    public static final int TITLE_STATE = 1;
    public static final int DUNGEON_GAME_STATE = 2;
    public static final int BATTLE_STATE = 3;
    public static final int MANAGE_DECK_STATE = 4;
    public static final int GAME_WON_STATE = 5;
    public static final int HELP_STATE = 6;

    private static AppGameContainer container;
    private ProfessionFactoryImpl professionFactory;
    private PlayerFactoryImpl playerFactory;
    private SpriteSheetFactory spriteSheetFactory;
    private MonsterFactoryImpl monsterFactory;
    private AppStateImpl appState;
    private CardFactory cardFactory;

    public Fury() {
        super("Fury");
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        // why do we have to do this here? ugh
        setupGame(gameContainer);

        createDependencies();
        addState(createTitleGameState());
        addState(createDungeonGameState());
        addState(createBattleGameState());
        addState(createManageDeckGameState());
        addState(createGameWonState());
        addState(createHelpState());
    }

    private void setupGame(GameContainer gameContainer) {
        gameContainer.setShowFPS(false);
    }

    private void createDependencies() throws SlickException {
        cardFactory = new CardFactory();
        professionFactory = new ProfessionFactoryImpl();
        playerFactory = new PlayerFactoryImpl(professionFactory,cardFactory);
        monsterFactory = new MonsterFactoryImpl(cardFactory);
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
        return new BattleGameState(appState,spriteSheetFactory);
    }

    private ManageDeckGameState createManageDeckGameState() {
        return new ManageDeckGameState();
    }

    private GameWonGameState createGameWonState() {
        return new GameWonGameState();
    }

    private HelpGameState createHelpState() {
        return new HelpGameState(appState);
    }

    public static void main(String[] args) {
        ResourceLoader.removeAllResourceLocations();
        ResourceLoader.addResourceLocation(new ClasspathLocation());

        try {
            container = new AppGameContainer(new Fury());
            container.setDisplayMode(GAME_WIDTH, GAME_HEIGHT, false);
            container.setShowFPS(true);
            container.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
