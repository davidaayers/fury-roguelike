package com.wwflgames.fury.card.generator;

import com.wwflgames.fury.card.Card;

import java.util.ArrayList;
import java.util.List;

public class MeleeCardGenerator implements CardGenerator {

    static List<String> crushWeaponNames;
    static List<String> slashWeaponNames;
    static List<String> pierceWeaponNames;

    static {
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

    @Override
    public Card generateCard(int points) {
        return null;
    }

    private Card generatePierceCard(int points) {
        return null;
    }

    private Card generateCrushCard(int points) {
        return null;
    }

    private Card generateSlashCard(int points) {
        return null;
    }

}
