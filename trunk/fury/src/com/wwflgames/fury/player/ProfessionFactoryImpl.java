package com.wwflgames.fury.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wwflgames.fury.entity.SpriteSheetProvider;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import java.util.ArrayList;
import java.util.List;

import static com.wwflgames.fury.item.ItemDeckXmlHelper.createDeck;

@Singleton
public class ProfessionFactoryImpl implements SpriteSheetProvider, ProfessionFactory {
    private List<Profession> allProfessions = new ArrayList<Profession>();
    List<String> allSpriteSheetNames = new ArrayList<String>();
    private ItemFactory itemFactory;

    @Inject
    public ProfessionFactoryImpl(ItemFactory itemFactory) throws SlickException {
        this.itemFactory = itemFactory;
        parseXml();
    }

    private void parseXml() throws SlickException {
        // read in the professions file
        XMLParser parser = new XMLParser();
        XMLElement element = parser.parse("professions.xml");
        XMLElementList children = element.getChildren();
        for (int idx = 0; idx < children.size(); idx++) {
            XMLElement childNode = children.get(idx);
            Log.debug("childNode = " + childNode.getName());
            String name = childNode.getAttribute("name");
            String spriteSheet = childNode.getAttribute("sprite-sheet");
            ItemDeck deck = createDeck(childNode, itemFactory);
            Profession profession = new Profession(name, spriteSheet, deck);
            addStats(childNode, profession);
            allProfessions.add(profession);
            allSpriteSheetNames.add(spriteSheet);
            Log.debug("profession created = " + profession);
        }
    }

    private void addStats(XMLElement childNode, Profession profession) throws SlickXMLException {
        XMLElementList list = childNode.getChildrenByName("stats");
        if (list.size() == 0) {
            return;
        }
        XMLElement statNode = list.get(0);
        XMLElementList stats = statNode.getChildren();
        for (int idx = 0; idx < stats.size(); idx++) {
            XMLElement xmlStat = stats.get(idx);
            Stat stat = Stat.valueOf(xmlStat.getName().toUpperCase());
            int value = xmlStat.getIntAttribute("value");
            profession.addStarterStat(stat, value);
        }
    }

    @Override
    public List<String> getAllSpriteSheetNames() {
        return allSpriteSheetNames;
    }

    @Override
    public List<Profession> getAllProfessions() {
        return allProfessions;
    }
}
