package com.wwflgames.fury.monster;

import com.wwflgames.fury.util.Shuffler;

import java.util.ArrayList;
import java.util.List;

import static com.wwflgames.fury.monster.NamePart.newNamePart;

public class MonsterNameGenerator {

    public static void main(String[] args) {
        MonsterNameGenerator generator = new MonsterNameGenerator();
        System.out.println(generator.generateName(4));
        System.out.println(generator.generateName(5));
        System.out.println(generator.generateName(6));
        System.out.println(generator.generateName(10));
        System.out.println(generator.generateName(12));
        System.out.println(generator.generateName(15));
    }

    NamePart[] prefixes = new NamePart[]{
            newNamePart("Strong", 10, 15),
            newNamePart("Hulking", 10, 15),
            newNamePart("Slavering", 10, 15)
    };

    NamePart[] names = new NamePart[]{
            newNamePart("Rat", 0, 5),
            newNamePart("Snake", 0, 5),
            newNamePart("Dog", 0, 5),
            newNamePart("Kobold", 6, 10),
            newNamePart("Giant Rat", 4, 12),
            newNamePart("Orc", 5, 25),
            newNamePart("Vampire", 15, 35)
    };

    public String generateName(int points) {
        return choosePrefix(points) + chooseName(points);
    }

    private String chooseName(int points) {
        List<String> nameList = createListForPoints(names, points);
        if (nameList.isEmpty()) {
            throw new IllegalStateException("name list was empty when it shouldn't have been");
        }
        Shuffler.shuffle(nameList);
        return nameList.get(0);
    }

    private String choosePrefix(int points) {
        List<String> prefixList = createListForPoints(prefixes, points);
        if (prefixList.isEmpty()) {
            return "";
        } else {
            Shuffler.shuffle(prefixList);
            return prefixList.get(0) + " ";
        }
    }

    private List<String> createListForPoints(NamePart[] parts, int points) {
        List<String> possibilities = new ArrayList<String>();
        for (NamePart part : parts) {
            if (part.validFor(points)) {
                possibilities.add(part.getPart());
            }
        }
        return possibilities;
    }


}
