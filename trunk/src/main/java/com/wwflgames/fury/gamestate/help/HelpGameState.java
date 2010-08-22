package com.wwflgames.fury.gamestate.help;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.main.AppState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class HelpGameState extends BasicGameState {
    private Image helpImage;
    private GameContainer gameContainer;
    private StateBasedGame stateBasedGame;
    private AppState appState;

    public HelpGameState(AppState appState) {
        this.appState = appState;
    }

    @Override
    public int getID() {
        return Fury.HELP_STATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameContainer = gameContainer;
        this.stateBasedGame = stateBasedGame;
        helpImage = new Image("help-screen.png");
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(helpImage,0,0);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }

    @Override
    public void keyPressed(int key, char c) {
        stateBasedGame.enterState(appState.getHelpReturnScreen());    
    }
}
