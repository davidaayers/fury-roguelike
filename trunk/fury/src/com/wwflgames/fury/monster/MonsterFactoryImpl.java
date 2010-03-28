package com.wwflgames.fury.monster;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wwflgames.fury.entity.SpriteSheetProvider;
import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Shuffler;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import java.util.ArrayList;
import java.util.List;

import static com.wwflgames.fury.util.XmlHelper.addStats;
import static com.wwflgames.fury.util.XmlHelper.createDeck;

@Singleton
public class MonsterFactoryImpl implements MonsterFactory, SpriteSheetProvider {

    private List<Monster> allMonsters = new ArrayList<Monster>();
    private List<String> allSpriteSheetNames = new ArrayList<String>();
    private ItemFactory itemFactory;

    @Inject
    public MonsterFactoryImpl(ItemFactory itemFactory) throws SlickException {

        System.out.println("CREATING MONSTERFACTORY");
        this.itemFactory = itemFactory;
        parseXml();
    }

    private void parseXml() throws SlickException {
        // read in the monsters xml file
        XMLParser parser = new XMLParser();
        XMLElement element = parser.parse("monsters.xml");
        XMLElementList children = element.getChildren();
        for (int idx = 0; idx < children.size(); idx++) {
            XMLElement childNode = children.get(idx);
            Log.debug("childNode = " + childNode.getName());
            String name = childNode.getAttribute("name");
            String spriteSheet = childNode.getAttribute("sprite-sheet");
            Monster monster = new Monster(name, spriteSheet, 0);
            // create the monster's deck
            monster.setDeck(createDeck(childNode, itemFactory));
//            setMonsterStats(childNode, monster);
            addStats(childNode, monster);
            allMonsters.add(monster);
            allSpriteSheetNames.add(spriteSheet);
            Log.debug("monster created = " + monster);
        }
    }

//    private void setMonsterStats(XMLElement childNode, Monster monster) {
//        //TODO: put this in the xml
//        monster.setStatValue(Stat.HEALTH, 10);
//    }

    @Override
    public List<String> getAllSpriteSheetNames() {
        return allSpriteSheetNames;
    }

    @Override
    public Monster createMonster(int points) {
        Shuffler.shuffle(allMonsters);

        Monster monster = allMonsters.get(0);
        Log.debug("About to return " + monster);
        Monster clone = new Monster(monster, points);
        return clone;
    }


}
