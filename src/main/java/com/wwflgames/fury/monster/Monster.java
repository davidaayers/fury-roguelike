package com.wwflgames.fury.monster;

import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.ai.AI;

import java.util.ArrayList;
import java.util.List;

public class Monster extends Mob {

    private String spriteSheet;
    private int monsterValue;
    private boolean isBoss;
    private List<MonsterDeathActivity> deathActivities = new ArrayList<MonsterDeathActivity>();
    private AI ai;

    public Monster(String name, String spriteSheet, int monsterValue) {
        super(name);
        this.spriteSheet = spriteSheet;
        this.monsterValue = monsterValue;
    }

    public Monster(Monster other, int monsterValue) {
        this(other.name(), other.getSpriteSheet(), monsterValue);
        setItemDeck(new ItemDeck(other.getItemDeck()));
        this.stats.putAll(other.stats);
    }


    public String getSpriteSheet() {
        return spriteSheet;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "name='" + name() + '\'' + " " +
                "spriteSheet='" + spriteSheet + '\'' +
                '}';
    }

    public int monsterValue() {
        return monsterValue;
    }

    public boolean isBoss() {
        return isBoss;
    }

    public void setBoss(boolean boss) {
        isBoss = boss;
    }

    public AI getAi() {
        return ai;
    }

    public void setAi(AI ai) {
        this.ai = ai;
    }

    public void think() {
        if ( ai != null ) {
            ai.think();
        }
    }

    public Card chooseCardToPlay() {
        // just choose the first card in the hand for now.
        //TODO: add some AI stuff here that determines what card to play
        return hand.getHand().get(0);
    }

    // called when this monster is killed.
    public void died() {
        for (MonsterDeathActivity activity : deathActivities) {
            activity.doActivity();
        }
    }

    public void addMonsterDeathActivity(MonsterDeathActivity activity) {
        deathActivities.add(activity);
    }

    //TODO: decide how we're going to do this
    public int computeExp() {
        // bosses earn 2x experience!
        if ( isBoss ) {
            return monsterValue * 2;
        } else {
            return monsterValue;
        }
    }

}
