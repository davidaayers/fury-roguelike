package com.wwflgames.fury.monster;

import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Shuffler;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import java.util.ArrayList;
import java.util.List;

import static com.wwflgames.fury.item.ItemDeckXmlHelper.createDeck;

public class MonsterFactory {

    private List<Monster> allMonsters = new ArrayList<Monster>();
    private List<String> allSpriteSheetNames = new ArrayList<String>();
    private ItemFactory itemFactory;

    public MonsterFactory(ItemFactory itemFactory) throws SlickException {
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
            setMonsterStats(childNode, monster);
            allMonsters.add(monster);
            allSpriteSheetNames.add(spriteSheet);
            Log.debug("monster created = " + monster);
        }
    }

    private void setMonsterStats(XMLElement childNode, Monster monster) {
        //TODO: put this in the xml
        monster.setStatValue(Stat.HEALTH, 10);
    }

//    private ItemDeck createDeck(XMLElement childNode) {
//
//        XMLElementList list = childNode.getChildrenByName("deck");
//        XMLElement deckNode = list.get(0);
//        XMLElementList items = deckNode.getChildren();
//        ItemDeck deck = new ItemDeck();
//        for (int idx = 0; idx < items.size(); idx++) {
//            XMLElement item = items.get(idx);
//            deck.addItem(itemFactory.getItemByName(item.getAttribute("name")));
//        }
//        Log.debug("deck is " + deck);
//
//        return deck;
//    }

    public List<String> getAllSpriteSheetNames() {
        return allSpriteSheetNames;
    }

    public Monster createMonster(int points) {
        Shuffler.shuffle(allMonsters);

        Monster monster = allMonsters.get(0);
        Log.debug("About to return " + monster);
        Monster clone = new Monster(monster, points);
        return clone;
    }


}
