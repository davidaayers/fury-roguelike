package com.wwflgames.fury.entity;

import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.player.Player;
import com.wwflgames.slick.entity.EntityMouseHandler;
import com.wwflgames.slick.entity.Renderer;
import org.newdawn.slick.*;

public class DungeonHudRenderer extends Renderer implements EntityMouseHandler {
    private Player player;
    private SpriteSheet playerSpriteSheet;
    private LevelUpHandler handler;
    private Image portrait;
    private Color levelUpColor = Color.white;
    private float levelUpAlpha = 0f;
    private int levelUpDir = 1;
    private Image scaledPortrait;

    public DungeonHudRenderer(String id, Player player, SpriteSheet playerSpriteSheet, LevelUpHandler handler) {
        super(id);
        this.player = player;
        this.playerSpriteSheet = playerSpriteSheet;
        this.handler = handler;
        Image playerImg = playerSpriteSheet.getSprite(1,2);
        createPortrait(playerImg);
    }

    private void createPortrait(Image playerImg) {
        try {
            portrait = new Image(24,21);
            Graphics g = portrait.getGraphics();
            g.drawImage(playerImg,0,0);
            g.flush();
            scaledPortrait = portrait.getScaledCopy(3.0f);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(Graphics gr) {
        gr.drawImage(scaledPortrait,0,0);
        gr.setColor(Color.white);
        gr.setLineWidth(3);
        gr.drawRoundRect(0,0,scaledPortrait.getWidth(),scaledPortrait.getHeight(),10);
        gr.setLineWidth(1);

        int x = scaledPortrait.getWidth() + 4;
        int y = 3;

        gr.drawString(player.name(),x,y);
        y+=20;
        gr.drawString("Health: " + player.getStatValue(Stat.HEALTH),x,y);
        y+=20;
        gr.drawString("Level: " + player.getLevel() + " Exp: " + player.getExp() ,x,y);

        // if the player has leveled up, draw a Level Up! across their portrait
        Color beforeColor = gr.getColor();
        if ( player.hasLeveledUp() ) {
            gr.setColor(levelUpColor);
            x = 12;
            y = 8;
            gr.drawString("Level",x,y);
            y += 15;
            x = 19;
            gr.drawString("UP!",x,y);
        }
        gr.setColor(beforeColor);

    }

    @Override
    public void update(int delta) {
        levelUpAlpha += levelUpDir * .001f * delta;
        if ( levelUpAlpha > 1.0f) {
            levelUpDir = -1;
        }
        if ( levelUpAlpha < 0f ) {
            levelUpDir = 1;            
        }
        Color white = Color.white;
        levelUpColor = new Color(white.r,white.g,white.b,levelUpAlpha);
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    }

    @Override
    public boolean mouseClicked(int button, int x, int y, int clickCount) {
        if ( player.hasLeveledUp() ) {
            // see if the click is in our neighborhood
            if ( x >= 0 && x <= scaledPortrait.getWidth() &&
                    y >= 0 && y <= scaledPortrait.getHeight() ) {
                handler.levelUpClicked();
                return true;
            }
        }
        return false;
    }

    public interface LevelUpHandler {
        void levelUpClicked();
    }
}
