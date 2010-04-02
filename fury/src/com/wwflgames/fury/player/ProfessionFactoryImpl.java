package com.wwflgames.fury.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wwflgames.fury.entity.SpriteSheetProvider;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.util.Log;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import java.util.ArrayList;
import java.util.List;

import static com.wwflgames.fury.util.XmlHelper.addStats;
import static com.wwflgames.fury.util.XmlHelper.createDeck;

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

    @Override
    public List<String> getAllSpriteSheetNames() {
        return allSpriteSheetNames;
    }

    @Override
    public List<Profession> getAllProfessions() {
        return allProfessions;
    }
}
