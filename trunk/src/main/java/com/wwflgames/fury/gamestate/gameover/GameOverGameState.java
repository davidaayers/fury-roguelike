package com.wwflgames.fury.gamestate.gameover;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.util.TextUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameOverGameState extends BasicGameState {

    private AppState appState;

    public GameOverGameState(AppState appState) {
        this.appState = appState;
    }

    @Override
    public int getID() {
        return Fury.GAME_OVER_STATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        if ( appState.didPlayerWin() ) {
            TextUtils.centerText(gameContainer,graphics,"You won :)",25);
        } else {
            TextUtils.centerText(gameContainer,graphics,"You lost :(",25);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }
}
