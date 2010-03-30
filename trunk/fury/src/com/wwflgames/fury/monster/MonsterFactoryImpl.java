package com.wwflgames.fury.monster;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wwflgames.fury.entity.SpriteSheetProvider;
import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Shuffler;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import java.util.ArrayList;
import java.util.List;

import static com.wwflgames.fury.util.XmlHelper.createDeck;

@Singleton
public class MonsterFactoryImpl implements MonsterFactory, SpriteSheetProvider {

    protected List<MonsterTemplate> allMonsters = new ArrayList<MonsterTemplate>();
    private List<String> allSpriteSheetNames = new ArrayList<String>();
    private ItemFactory itemFactory;

    @Inject
    public MonsterFactoryImpl(ItemFactory itemFactory) throws SlickException {
        this.itemFactory = itemFactory;
        parseXml();
    }

    // package private to override in tests
    void parseXml() throws SlickException {
        // read in the monsters xml file
        XMLParser parser = new XMLParser();
        XMLElement element = parser.parse("monsters.xml");
        XMLElementList children = element.getChildren();
        for (int idx = 0; idx < children.size(); idx++) {
            XMLElement childNode = children.get(idx);
            Log.debug("childNode = " + childNode.getName());
            String name = childNode.getAttribute("name");
            String spriteSheet = childNode.getAttribute("sprite-sheet");
            int low = childNode.getIntAttribute("points-low");
            int high = childNode.getIntAttribute("points-high");
            MonsterTemplate monster = new MonsterTemplate(name, spriteSheet, low, high);
            // create the monster's deck
            addNameModifiers(childNode, monster);
            monster.setDeck(createDeck(childNode, itemFactory));
            addStats(childNode, monster);
            allMonsters.add(monster);
            allSpriteSheetNames.add(spriteSheet);
            Log.debug("monster created = " + monster);
        }
    }

    private void addNameModifiers(XMLElement childNode, MonsterTemplate monster) {
        XMLElementList list = childNode.getChildrenByName("name-modifiers");
        if (list.size() == 0) {
            return;
        }
        XMLElement nameModifiersNode = list.get(0);
        XMLElementList nameModifiers = nameModifiersNode.getChildren();
        for (int idx = 0; idx < nameModifiers.size(); idx++) {
            XMLElement mod = nameModifiers.get(idx);
            String pointsStr = mod.getAttribute("points");
            String nameMods = mod.getContent();
            String modType = mod.getName();

            if ("boss".equals(pointsStr)) {
                // use -1 to signify boss
                //TODO: I dont like this very much
                addNameModsToMonster(monster, nameMods, modType, "-1");
            } else {
                for (String point : pointsStr.split(",")) {
                    addNameModsToMonster(monster, nameMods, modType, point);
                }
            }
        }
    }

    private void addNameModsToMonster(MonsterTemplate monster, String nameMods, String modType, String point) {
        for (String modStr : nameMods.split(",")) {
            monster.addNameModifier(modType, Integer.valueOf(point), modStr);
        }
    }

    void installAllMonsters(List<MonsterTemplate> list) {
        allMonsters = list;
    }

    private static void addStats(XMLElement childNode, MonsterTemplate template) throws SlickXMLException {
        XMLElementList list = childNode.getChildrenByName("stats");
        if (list.size() == 0) {
            return;
        }
        XMLElement statNode = list.get(0);
        XMLElementList stats = statNode.getChildren();
        for (int idx = 0; idx < stats.size(); idx++) {
            XMLElement xmlStat = stats.get(idx);
            Stat stat = Stat.valueOf(xmlStat.getName().toUpperCase());
            int low = xmlStat.getIntAttribute("low");
            int high = xmlStat.getIntAttribute("high");
            StatRange range = new StatRange(low, high);
            template.setStatRange(stat, range);
        }
    }


    @Override
    public List<String> getAllSpriteSheetNames() {
        return allSpriteSheetNames;
    }

    @Override
    public Monster createMonster(int points) {
        List<MonsterTemplate> matchingMonsters = new ArrayList<MonsterTemplate>();
        for (MonsterTemplate template : allMonsters) {
            if (template.matchesPoints(points)) {
                matchingMonsters.add(template);
            }
        }

        Shuffler.shuffle(matchingMonsters);
        MonsterTemplate template = matchingMonsters.get(0);
        return template.createForPoints(points);
    }


}
