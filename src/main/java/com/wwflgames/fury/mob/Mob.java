package com.wwflgames.fury.mob;

import com.wwflgames.fury.card.Deck;
import com.wwflgames.fury.card.Hand;
import com.wwflgames.fury.card.statuseffect.StatusEffect;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.item.effect.BuffEffect;
import com.wwflgames.fury.item.effect.DebuffEffect;
import com.wwflgames.fury.map.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mob implements StatHolder {

    protected Map<Stat, Integer> stats = new HashMap<Stat, Integer>();
    protected Map<Stat, Integer> battleStats;
    protected ItemDeck itemDeck;
    protected Deck deck;
    protected Hand hand;
    private List<BuffEffect> buffs = new ArrayList<BuffEffect>();
    private List<DebuffEffect> debuffs = new ArrayList<DebuffEffect>();
    private List<OldStatusEffect> statusEffects = new ArrayList<OldStatusEffect>();
    private List<StatusEffect> newStatusEffects = new ArrayList<StatusEffect>();
    private String name;
    private Tile currentMapTile;
    private Integer mapX;
    private Integer mapY;

    public Mob(String name) {
        this.name = name;
        resetBattleStats();
    }

    public Integer getStatValue(Stat stat) {
        Integer statValue = stats.get(stat);
        if (statValue == null) {
            return 0;
        }
        return statValue;
    }

    public void setStatValue(Stat stat, Integer value) {
        stats.put(stat, value);
    }

    public void modifyStatValue(Stat stat, Integer value) {
        int oldValue = getStatValue(stat);
        int newValue = oldValue + value;
        stats.put(stat, newValue);
    }

    public Integer getBattleStatValue(Stat stat) {
        Integer statValue = battleStats.get(stat);
        if (statValue == null) {
            return 0;
        }
        return statValue;
    }

    public void setBattleStatValue(Stat stat, Integer value) {
        battleStats.put(stat, value);
    }

    public void modifyBattleStatValue(Stat stat, Integer value) {
        int oldValue = getBattleStatValue(stat);
        int newValue = oldValue + value;
        battleStats.put(stat, newValue);
    }

    public void resetBattleStats() {
        battleStats = new HashMap<Stat, Integer>();
    }

    public void prepareForBattle() {
        // clear out anything that's already in battle stats
        battleStats.clear();

        // clear out any buffs that may be left over
        buffs.clear();

        // copy all of the mobs stats into battle stats. Any temporary buffs that
        // change stats will apply to these, rather than the original stats.
        for (Stat stat : stats.keySet()) {
            battleStats.put(stat, getStatValue(stat));
        }
    }


    public Deck getDeck() {
        return deck;
    }

    public void installDeck(Deck deck) {
        this.deck = deck;
        // when a new deck is set, shuffle it, and create a hand
        deck.shuffle();
        hand = new Hand(deck);
        hand.drawToMax();
    }

    public Hand getHand() {
        return hand;
    }


    public ItemDeck getItemDeck() {
        return itemDeck;
    }

    public void setItemDeck(ItemDeck deck) {
        this.itemDeck = deck;
    }

    public boolean isDead() {
        return getStatValue(Stat.HEALTH) <= 0;
    }

    public String name() {
        return name;
    }

    public String possessiveName() {
        String possessiveName = name + "'";
        if (!name.endsWith("s")) {
            possessiveName += "s";
        }
        return possessiveName;
    }

    public Tile getCurrentMapTile() {
        return currentMapTile;
    }

    public void setCurrentMapTile(Tile currentMapTile) {
        this.currentMapTile = currentMapTile;
        if (currentMapTile != null) {
            setMapX(currentMapTile.getX());
            setMapY(currentMapTile.getY());
        } else {
            setMapX(null);
            setMapY(null);
        }
    }

    public Integer getMapX() {
        return mapX;
    }

    public void setMapX(Integer mapX) {
        this.mapX = mapX;
    }

    public Integer getMapY() {
        return mapY;
    }

    public void setMapY(Integer mapY) {
        this.mapY = mapY;
    }

    public void addBuff(BuffEffect buff) {
        buffs.add(buff);
    }

    public void removeBuff(BuffEffect buff) {
        buffs.remove(buff);
    }

    public List<BuffEffect> getBuffs() {
        return buffs;
    }

    public void addDebuff(DebuffEffect debuff) {
        debuffs.add(debuff);
    }

    public void removeDebuff(DebuffEffect debuff) {
        debuffs.remove(debuff);
    }

    public List<DebuffEffect> getDebuffs() {
        return debuffs;
    }

    public void addStatusEffect(OldStatusEffect statusEffect) {
        statusEffects.add(statusEffect);
    }

    public void removeStatusEffect(OldStatusEffect statusEffect) {
        statusEffects.remove(statusEffect);
    }

    public List<OldStatusEffect> getStatusEffects() {
        return statusEffects;
    }

    public void addStatusEffect(StatusEffect statusEffect) {
        newStatusEffects.add(statusEffect);
    }

    public void removeStatusEffect(StatusEffect statusEffect) {
        newStatusEffects.remove(statusEffect);
    }

    public List<StatusEffect> getNewStatusEffects() {
        return newStatusEffects;
    }


    @Override
    public String toString() {
        return "Mob{" +
                "name='" + name + '\'' +
                "health='" + stats.get(Stat.HEALTH) + '\'' +
                '}';
    }
}
