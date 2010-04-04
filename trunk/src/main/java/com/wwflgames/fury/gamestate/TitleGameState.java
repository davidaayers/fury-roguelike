package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.entity.SpriteSheetFactory;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.map.*;
import com.wwflgames.fury.map.generation.Feature;
import com.wwflgames.fury.monster.MonsterFactory;
import com.wwflgames.fury.player.PlayerFactory;
import com.wwflgames.fury.player.Profession;
import com.wwflgames.fury.player.ProfessionFactory;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.TextUtil;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.util.ArrayList;
import java.util.List;

import static com.wwflgames.fury.Fury.*;
import static com.wwflgames.fury.map.DifficultyLevel.EASY;

public class TitleGameState extends BasicGameState {

    enum State {
        WAITING_FOR_PROFESSION_CHOICE,
        START_GENERATING_MAP,
        GENERATING_MAP,
        GAME_START
    }

    private GameContainer gameContainer;
    private StateBasedGame stateBasedGame;
    private Image titleImage;
    private ProfessionFactory professionFactory;
    private PlayerFactory playerFactory;
    private SpriteSheetFactory spriteSheetFactory;
    private List<MouseOverArea> professionChoices = new ArrayList<MouseOverArea>();
    private State currentState;
    private MonsterFactory monsterFactory;
    private AppState appState;
    private DungeonCreator dungeonCreator;


    public TitleGameState(ProfessionFactory professionFactory, PlayerFactory playerFactory,
                          SpriteSheetFactory spriteSheetFactory, MonsterFactory monsterFactory, AppState appState) {
        this.professionFactory = professionFactory;
        this.playerFactory = playerFactory;
        this.spriteSheetFactory = spriteSheetFactory;
        this.monsterFactory = monsterFactory;
        this.appState = appState;

        DungeonMapCreator randomCreator = new DungeonMapCreatorImpl(monsterFactory);
        dungeonCreator = new DungeonCreatorImpl(randomCreator, monsterFactory,appState);

    }

    @Override
    public int getID() {
        return TITLE_STATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameContainer = gameContainer;
        this.stateBasedGame = stateBasedGame;
        titleImage = new Image("title-image.png");

        // create mouse over areas for the professions
        List<Profession> allProfessions = professionFactory.getAllProfessions();
        int numProfessions = allProfessions.size();
        // figure out how much room we have, and space them out evenly
        float pixelsPerProfession = (float) GAME_WIDTH / (float) numProfessions;
        float x = pixelsPerProfession / 2;
        float y = 250;
        for (final Profession profession : professionFactory.getAllProfessions()) {
            Image img = spriteSheetFactory.spriteSheetForName(profession.getSpriteSheet()).getSprite(1, 2);
            img = img.getScaledCopy(4f);

            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();

            Image realImage = new Image(imgWidth, imgHeight + 20);
            Graphics g1 = realImage.getGraphics();
            g1.drawImage(img, 0, 0, Color.gray);
            g1.flush();

            // create an image for the mouse over Image, which includes the name of
            // the profession
            Image mouseOver = new Image(imgWidth, imgHeight + 20);
            Graphics g = mouseOver.getGraphics();
            g.drawImage(img, 0, 0);
            g.setColor(Color.white);
            String prof = profession.getName();
            int strWidth = g.getFont().getWidth(prof);
            int strX = imgWidth / 2 - strWidth / 2;
            g.drawString(prof, strX, imgHeight);
            g.flush();

            int imgX = (int) x - img.getWidth() / 2;
            MouseOverArea moa = new MouseOverArea(gameContainer, realImage, imgX, (int) y, new ComponentListener() {
                @Override
                public void componentActivated(AbstractComponent source) {
                    professionChosen(profession);
                }
            });
            //moa.setMouseOverColor(Color.red);
            moa.setMouseOverImage(mouseOver);


            professionChoices.add(moa);
            x += pixelsPerProfession;
        }

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        currentState = State.WAITING_FOR_PROFESSION_CHOICE;
    }

    private void professionChosen(Profession profession) {
        Log.debug("Profession chosen: " + profession);
        currentState = State.START_GENERATING_MAP;
        appState.setPlayer(playerFactory.createForProfession(profession));
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics)
            throws SlickException {

        graphics.drawImage(titleImage, 400 - titleImage.getWidth() / 2, 25);
        if (currentState == State.WAITING_FOR_PROFESSION_CHOICE) {
            TextUtil.centerText(gameContainer, graphics, "Choose your profession:", 220);
            TextUtil.centerText(gameContainer, graphics, "Press 'h' for help",520);
        }

        for (MouseOverArea moa : professionChoices) {
            moa.render(gameContainer, graphics);
        }

        if (currentState == State.GENERATING_MAP || currentState == State.START_GENERATING_MAP) {
            TextUtil.centerText(gameContainer, graphics, "Generating dungeon...", 520);
        }



    }

    @Override
    public void keyPressed(int key, char c) {
        if ( c == 'h' && currentState == State.WAITING_FOR_PROFESSION_CHOICE ) {
            appState.setHelpReturnScreen(Fury.TITLE_STATE);
            stateBasedGame.enterState(Fury.HELP_STATE);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        switch (currentState) {
            case WAITING_FOR_PROFESSION_CHOICE:

                break;

            case START_GENERATING_MAP:
                currentState = State.GENERATING_MAP;
                // spin off a thread to generate the dungeonMap, so the game will keep updaing
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        disableMouseOverAreas();
                        generateMap();
                        currentState = State.GAME_START;
                    }
                }).start();
                break;

            case GENERATING_MAP:
                // just wait
                break;

            case GAME_START:
                stateBasedGame.enterState(DUNGEON_GAME_STATE, new FadeOutTransition(Color.black),
                        new FadeInTransition(Color.black));
                break;
        }
    }

    private void disableMouseOverAreas() {
        for (MouseOverArea moa : professionChoices) {
            moa.setAcceptingInput(false);
        }
    }

    private void generateMap() {
        appState.setDungeon(dungeonCreator.createDungeon(EASY));
        putPlayerOnMap();
    }

    private void putPlayerOnMap() {
        // grab the map.
        DungeonMap map = appState.getDungeon().currentLevelMap();
        Feature f = map.getFeatureList().get(0);
        int tile = 0;
        Tile emptyTile = null;
        // grab the first tile that isn't already populated with a monster
        while (emptyTile == null) {
            Tile floor = f.getFloorTiles()[tile];
            if (floor.getMob() == null) {
                emptyTile = floor;
            }
            tile++;
        }
        int x = emptyTile.getX();
        int y = emptyTile.getY();
        Log.debug("player location is " + x + "," + y);
        appState.getMap().addMob(appState.getPlayer(), x, y);
    }


}
