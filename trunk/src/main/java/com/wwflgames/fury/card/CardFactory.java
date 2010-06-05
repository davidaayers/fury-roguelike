package com.wwflgames.fury.card;

import com.wwflgames.fury.card.applier.*;
import com.wwflgames.fury.card.statuseffect.DotStatusEffect;
import com.wwflgames.fury.card.statuseffect.StatusEffect;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Rand;
import com.wwflgames.fury.util.Shuffler;

import java.util.ArrayList;
import java.util.List;

public class CardFactory {

    List<String> crushWeaponNames;
    List<String> slashWeaponNames;
    List<String> pierceWeaponNames;

    public CardFactory() {
        initWeaponNames();
    }

    private void initWeaponNames() {
        crushWeaponNames = new ArrayList<String>();
        crushWeaponNames.add("Mace");
        crushWeaponNames.add("Morningstar");
        crushWeaponNames.add("Club");
        slashWeaponNames = new ArrayList<String>();
        slashWeaponNames.add("Sword");
        slashWeaponNames.add("Axe");
        slashWeaponNames.add("Claymore");
        pierceWeaponNames = new ArrayList<String>();
        pierceWeaponNames.add("Dagger");
        pierceWeaponNames.add("Rapier");
        pierceWeaponNames.add("Main Gauche");
    }

    public Card getCardByName(String name) {
        return newDamageCard(name,DamageType.CRUSH,5);
    }

    public Card getBuffCardByName(String name) {
        return newBattleStatBuffCard(name);
    }

    private Card newBattleStatBuffCard(String name) {
        BattleStatChangeApplier bsc = new BattleStatChangeApplier(Stat.ARMOR,10,true);
        return new Card(name,new Applier[] { bsc } , null , 0 );
    }

    public Card newBattleStatDebuffCard(String name) {
        BattleStatChangeApplier bsc = new BattleStatChangeApplier(Stat.ARMOR,-10,false);
        return new Card(name,null, new Applier[] { bsc } , 1 );
    }

    public Card newDotCard(String name) {
        StatusEffect dot = new DotStatusEffect("Burn",3,5);
        StatusEffectApplier app = new StatusEffectApplier(dot,false);
        return new Card(name,null, new Applier[] { app } , 1 );
    }


    private Card newDamageCard(String name, DamageType damageType, int damageAmt) {
        DamageApplier da = new DamageApplier(damageType,damageAmt);
        return new Card(name,null,new Applier[] { da } , 1);
    }

    public Card createMeleeWeaponCard(int points) {
        DamageType type = randomMeleeDamageType();
        String startNamePart = generateMeleeWeaponName(type);
        String endNamePart = null;
        List<Applier> usedByAppliersList = new ArrayList<Applier>();
        List<Applier> usedAgainstAppliersList = new ArrayList<Applier>();
        int dmgAmt = Rand.between(5,10);
        usedAgainstAppliersList.add(new DamageApplier(type,dmgAmt));
        points -= dmgAmt;
        if ( points > 0 ) {
            // we have some points left, either add a buff or a debuff
            if ( Rand.get().nextBoolean() ) {
                BattleStatChangeApplier buff = randomBattleStatBuff(points);
                if ( buff.getBattleStat() == Stat.ARMOR ) {
                    endNamePart = "Protection";
                } else {
                    endNamePart = "Willpower";
                }
                usedByAppliersList.add(buff);
            } else {
                BattleStatChangeApplier debuff = randomBattleStatDebuff(points);
                if ( debuff.getBattleStat() == Stat.ARMOR ) {
                    endNamePart = "Destruction";
                } else {
                    endNamePart = "Cowering";
                }
                usedAgainstAppliersList.add(debuff);
            }
        }
        Applier[] usedByAppliers = null;
        Applier[] usedAgainstAppliers = null;
        if ( usedByAppliersList.size() > 0 ) {
            usedByAppliers = usedByAppliersList.toArray(new Applier[usedByAppliersList.size()]);
        }
        if ( usedAgainstAppliersList.size() > 0 ) {
            usedAgainstAppliers = usedAgainstAppliersList.toArray(new Applier[usedAgainstAppliersList.size()]);
        }
        String name = startNamePart + ( endNamePart != null ? " of " + endNamePart : "");
        return new Card(name,usedByAppliers,usedAgainstAppliers,1);
    }

    public Card createRandomCard(int points) {
        List<Applier> usedByAppliersList = new ArrayList<Applier>();
        List<Applier> usedAgainstAppliersList = new ArrayList<Applier>();
        while ( points > 0 ) {
            int rand = Rand.between(0,4);
            switch(rand) {
                case 0:
                    points -= addRandomBuff(usedByAppliersList,points);
                    break;
                case 1:
                    points -= addRandomDebuff(usedAgainstAppliersList,points);
                    break;
                case 2:
                    points -= addRandomMeleeAttack(usedAgainstAppliersList,points);
                    break;
                case 3:
                    points -= addRandomDot(usedAgainstAppliersList,points);
            }
            
        }
        Applier[] usedByAppliers = null;
        Applier[] usedAgainstAppliers = null;
        if ( usedByAppliersList.size() > 0 ) {
            usedByAppliers = usedByAppliersList.toArray(new Applier[usedByAppliersList.size()]);
        }
        if ( usedAgainstAppliersList.size() > 0 ) {
            usedAgainstAppliers = usedAgainstAppliersList.toArray(new Applier[usedAgainstAppliersList.size()]);
        }
        String name = createName(usedByAppliers,usedAgainstAppliers);
        return new Card(name,usedByAppliers,usedAgainstAppliers,usedAgainstAppliers==null?0:1);
    }

    private int addRandomDot(List<Applier> usedAgainstAppliersList,int points) {
        int dotAmt = calcPoints(points,.5);
        int numRounds = Rand.between(2,5);
        int dmgPerRound = dotAmt/numRounds;
        if ( dmgPerRound < 1 ) {
            dmgPerRound = 1;
        }
        DotStatusEffect statusEffect = new DotStatusEffect("Burn", numRounds, dmgPerRound);
        usedAgainstAppliersList.add(new StatusEffectApplier(statusEffect,false));
        return dotAmt;
    }

    private int addRandomMeleeAttack(List<Applier> usedAgainstAppliersList, int points) {
        DamageType type = randomMeleeDamageType();
        int dmgAmt = calcPoints(points,.7);
        usedAgainstAppliersList.add(new DamageApplier(type,dmgAmt));
        return dmgAmt;
    }

    private int addRandomDebuff(List<Applier> usedAgainstAppliersList, int points) {
        int debuffAmt = calcPoints(points,.7);
        usedAgainstAppliersList.add(randomBattleStatDebuff(debuffAmt));
        return debuffAmt;
    }

    private int addRandomBuff(List<Applier> usedByAppliersList, int points) {
        int buffAmt = calcPoints(points,.6);
        usedByAppliersList.add(randomBattleStatBuff(buffAmt));
        return buffAmt;
    }

    private int calcPoints(int points,double multiplier) {
        int debuffAmt;
        if ( points < 5 ) {
            debuffAmt = points;
        } else {
            int min = (int)((double)points* multiplier);
            debuffAmt = Rand.between(min,points+1);
        }
        return debuffAmt;
    }
    
    private String createName(Applier[] usedByAppliers, Applier[] usedAgainstAppliers) {
        return "Foo";
    }

    private BattleStatChangeApplier randomBattleStatDebuff(int points) {
        List<Stat> battleStats = Stat.battleStats();
        Shuffler.shuffle(battleStats);
        return new BattleStatChangeApplier(battleStats.get(0),-points,false);
    }

    private BattleStatChangeApplier randomBattleStatBuff(int points) {
        List<Stat> battleStats = Stat.battleStats();
        Shuffler.shuffle(battleStats);
        return new BattleStatChangeApplier(battleStats.get(0),points,true);
    }

    private String generateMeleeWeaponName(DamageType type) {
        switch ( type ) {
            case CRUSH:
                Shuffler.shuffle(crushWeaponNames);
                return crushWeaponNames.get(0);
            case SLASH:
                Shuffler.shuffle(slashWeaponNames);
                return slashWeaponNames.get(0);
            case PIERCE:
                Shuffler.shuffle(pierceWeaponNames);
                return pierceWeaponNames.get(0);
        }
        throw new IllegalStateException("Shouldn't get here");
    }

    private DamageType randomMeleeDamageType() {
        List<DamageType> meleeTypes = DamageType.findAllChildrenOfType(DamageType.MELEE);
        Shuffler.shuffle(meleeTypes);
        return meleeTypes.get(0);
    }

}
