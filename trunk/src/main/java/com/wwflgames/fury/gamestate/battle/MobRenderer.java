package com.wwflgames.fury.gamestate.battle;

import com.wwflgames.fury.gamestate.common.SpriteSheetRenderer;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class MobRenderer extends SpriteSheetRenderer {
    private Mob mob;

    public MobRenderer(Mob mob, SpriteSheet spriteSheet) {
        super(mob.name() + "Renderer", spriteSheet);
        this.mob = mob;
    }

    @Override
    public void render(Graphics gr) {
        // render sprite first
        super.render(gr);

        Vector2f pos = owner.getPosition();

        // overlay stuff
        // health
        int currentHealth = mob.getStatValue(Stat.HEALTH);
        // the player's starting health at the beginning of the battle
        int maxHealth = mob.getBattleStatValue(Stat.HEALTH);
        float factor = 60 / (float) maxHealth;
        drawStatBar(gr, currentHealth, factor, pos.x, pos.y, Color.red);

        // armor
        int currentArmor = mob.getBattleStatValue(Stat.ARMOR);
        drawStatBar(gr, currentArmor, 2f, pos.x, pos.y + 12, Color.blue);


        // will
        int currentWill = mob.getBattleStatValue(Stat.WILL);
        drawStatBar(gr, currentWill, 2f, pos.x, pos.y + 24, Color.green);

    }

    private void drawStatBar(Graphics gr, int current, float drawFactor, float x, float y, Color color) {
        if (current < 0) {
            current = 0;
        }
        gr.setColor(color);
        float barWidth = current * drawFactor;
        gr.drawString("" + current, x, y - 5);
        gr.fillRect(x + 30, y, barWidth, 8);
    }

}
