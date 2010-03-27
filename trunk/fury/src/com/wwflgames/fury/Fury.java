package com.wwflgames.fury;

import com.wwflgames.fury.entity.SpriteSheetCache;
import com.wwflgames.fury.gamestate.BattleGameState;
import com.wwflgames.fury.gamestate.DungeonGameState;
import com.wwflgames.fury.gamestate.ManageDeckGameState;
import com.wwflgames.fury.gamestate.TitleGameState;
import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.main.AppStateImpl;
import com.wwflgames.fury.monster.MonsterFactory;
import com.wwflgames.fury.player.PlayerFactory;
import com.wwflgames.fury.player.ProfessionFactory;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.ResourceLoader;

public class Fury extends StateBasedGame {

    public static final int TITLE_STATE = 1;
    public static final int DUNGEON_GAME_STATE = 2;
    public static final int BATTLE_STATE = 3;
    public static final int MANAGE_DECK_STATE = 4;

    private static AppGameContainer container;

    private AppStateImpl appState;
    private SpriteSheetCache spriteSheetCache;
    private MonsterFactory monsterFactory;
    private ProfessionFactory professionFactory;
    private PlayerFactory playerFactory;
    private ItemFactory itemFactory;

    public Fury() {
        super("Fury - 7DRL");
        initAppState();
    }

    private void initAppState() {
        appState = new AppStateImpl();
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {

        // this is cheesy, but this is the only place we can hook into the init of game
        // and do anything.
        initItemFactory();
        initMonsterFactory();
        installPlayerFactory();
        initSpriteSheetCache();

        // now, actually create the game states like we're supposed to in this method. ugh.
        installGameStates();
    }

    private void initItemFactory() throws SlickException {
        itemFactory = new ItemFactory();
    }

    private void installGameStates() {
        addState(createTitleGameState());
        addState(createDungeonState());
        addState(createBattleState());
        addState(createManageDeckGameState());
    }

    private void installPlayerFactory() throws SlickException {
        professionFactory = new ProfessionFactory(itemFactory);
        playerFactory = new PlayerFactory(professionFactory);
    }

    private void initMonsterFactory() throws SlickException {
        monsterFactory = new MonsterFactory(itemFactory);
    }

    private void initSpriteSheetCache() throws SlickException {
        spriteSheetCache = new SpriteSheetCache();
        for (String spriteSheetName : monsterFactory.getAllSpriteSheetNames()) {
            spriteSheetCache.loadSprite(spriteSheetName);
        }
        for (String spriteSheetName : professionFactory.getAllSpriteSheetNames()) {
            spriteSheetCache.loadSprite(spriteSheetName);
        }
    }

    private TitleGameState createTitleGameState() {
        return new TitleGameState(professionFactory, playerFactory, spriteSheetCache, monsterFactory, appState);
    }

    private GameState createDungeonState() {
        return new DungeonGameState(appState, spriteSheetCache);
    }

    private BattleGameState createBattleState() {
        return new BattleGameState(appState, spriteSheetCache, itemFactory);
    }

    private GameState createManageDeckGameState() {
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
