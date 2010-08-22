package com.wwflgames.fury.gamestate.battle;

import com.wwflgames.fury.battle.BattleResult;
import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.battle.ResultType;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.util.TextUtils;
import com.wwflgames.slick.entity.SimpleRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleTextRenderer extends SimpleRenderer {

    private Font font;
    List<ItemLogMessage> battleEffectStack = new ArrayList<ItemLogMessage>();

    public BattleTextRenderer(String id, Font font) {
        super(id);
        this.font = font;
    }

    @Override
    public void render(Graphics gr) {
        int effectX = (int) owner.getPosition().x;
        int effectY = (int) owner.getPosition().y;
        if (battleEffectStack.isEmpty()) {
            return;
        }
        int max = battleEffectStack.size() > 27 ? 27 : battleEffectStack.size();
        for (int idx = 0; idx < max; idx++) {
            ItemLogMessage effectStr = battleEffectStack.get(idx);
            font.drawString((int) effectX, effectY, effectStr.getString(), effectStr.getColor());
            effectY += 14;
        }
    }

    private void drawBattleText(int effectX, List<ItemLogMessage> effects) {

    }

    public void addBattleRoundResults(BattleRound lastBattleRound, List<Mob> mobs) {
        // grey out any existing messages
        greyOutItemMessages();
        
        // add new messages
        for (Mob mob : mobs) {
            List<BattleResult> battleResults = lastBattleRound.getResultsFor(mob);
            for (BattleResult battleResult : battleResults) {
                addDescToEffects(battleEffectStack, battleResult);
            }
        }
    }

    private void greyOutItemMessages() {
        for (ItemLogMessage str : battleEffectStack) {
            str.setColor(Color.darkGray);
        }
    }


    private void addDescToEffects(List<ItemLogMessage> effects, BattleResult result) {
        String desc = createDescString(result);
        Color color = determineColor(result);
        addStringToEffects(0, effects, desc, color);
    }

    private String createDescString(BattleResult effectResult) {
        String s0 = effectResult.getEffectedMob().possessiveName();
        String s1 = effectResult.getEffectedMob().name();
        return MessageFormat.format(effectResult.getDesc(), s0, s1);
    }

    private void addStringToEffects(int pos, List<ItemLogMessage> effects, String string, Color color) {
        List<String> parts = TextUtils.maybeSplitString(string, 160, font);
        Collections.reverse(parts);
        for (String part : parts) {
            effects.add(pos, new ItemLogMessage(part, color));
        }
    }


    private Color determineColor(BattleResult effectResult) {
        ResultType effectType = effectResult.getResultType();
        if (effectType == ResultType.DAMAGE) {
            return Color.red;
        }
        if (effectType == ResultType.BUFF) {
            return Color.green;
        }
        if (effectType == ResultType.DEATH) {
            return Color.yellow;
        }
        return Color.white;
    }

    private class ItemLogMessage {
        private String string;
        private Color color;

        private ItemLogMessage(String string, Color color) {
            this.string = string;
            this.color = color;
        }

        public String getString() {
            return string;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }
}
