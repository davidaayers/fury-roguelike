package com.wwflgames.fury.player;

import com.wwflgames.fury.entity.SpriteSheetProvider;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import java.util.ArrayList;
import java.util.List;

import static com.wwflgames.fury.util.XmlHelper.*;

public class ProfessionFactoryImpl implements SpriteSheetProvider, ProfessionFactory {
    private List<Profession> allProfessions = new ArrayList<Profession>();
    private List<Perk> allPerks = new ArrayList<Perk>();
    List<String> allSpriteSheetNames = new ArrayList<String>();

    public ProfessionFactoryImpl() throws SlickException {
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
            if (childNode.getName().equals("perks")) {
                readPerks(childNode);
            } else {
                String name = childNode.getAttribute("name");
                String spriteSheet = childNode.getAttribute("sprite-sheet");
                Profession profession = new Profession(name, spriteSheet);
                addStats(childNode, profession);
                addPerks(childNode, profession);
                allProfessions.add(profession);
                allSpriteSheetNames.add(spriteSheet);
                Log.debug("profession created = " + profession);
            }
        }
    }

    private void addPerks(XMLElement childNode, Profession profession) {
        XMLElement startingPerks = extractSubNodeByName(childNode,"starting-perks");
        XMLElementList startingPerkChildren = startingPerks.getChildren();
        for ( int idx = 0 ; idx < startingPerkChildren.size() ; idx ++ ) {
            XMLElement perk = startingPerkChildren.get(idx);
            profession.addStartingPerk(findPerkByName(perk.getContent()));
        }
        XMLElement availablePerks = extractSubNodeByName(childNode,"available-perks");
        XMLElementList availablePerkChildren = availablePerks.getChildren();
        for ( int idx = 0 ; idx < availablePerkChildren.size() ; idx ++ ) {
            XMLElement perk = availablePerkChildren.get(idx);
            profession.addAvailablePerk(findPerkByName(perk.getContent()));
        }

    }

    private void readPerks(XMLElement perkNode) {
        XMLElementList children = perkNode.getChildren();
        for (int idx = 0; idx < children.size(); idx++) {
            XMLElement perk = children.get(idx);
            String name = perk.getAttribute("name");
            String prereq = perk.getAttribute("prereq");
            Log.debug("Name = " + name + " prereq = " + prereq);
            String description = extractContentFromNodeValueByName(perk, "description");
            Log.debug("description = " + description);
            createPerk(name, description, prereq, extractSubNodeByName(perk, "type"));
        }
    }

    private void createPerk(String name, String description, String prereq, XMLElement xmlElement) {
        String typeName = xmlElement.getAttribute("name");
        if ("HandIncreasePerk".equals(typeName)) {
            Integer numCards = new Integer(extractContentFromNodeValueByName(xmlElement, "numCards"));
            Log.debug("numCards = " + numCards);
            Perk prerequisitePerk = findPerkByName(prereq);
            allPerks.add(new HandIncreasePerk(name, description, prerequisitePerk, numCards));
        } else if ("StatPerk".equals(typeName)) {
            String statName = extractContentFromNodeValueByName(xmlElement, "stat");
            Integer statValue = new Integer(extractContentFromNodeValueByName(xmlElement, "amount"));
            Perk prerequisitePerk = findPerkByName(prereq);
            allPerks.add(new StatPerk(name, description, prerequisitePerk, Stat.valueOf(statName.toUpperCase()), statValue));
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

    @Override
    public Profession getProfessionByName(String name) {
        for (Profession profession : allProfessions) {
            if (profession.getName().equals(name)) {
                return profession;
            }
        }
        return null;
    }

    private Perk findPerkByName(String name) {
        for (Perk perk : allPerks) {
            if (perk.getName().equals(name)) {
                return perk;
            }
        }
        return null;
    }
}
