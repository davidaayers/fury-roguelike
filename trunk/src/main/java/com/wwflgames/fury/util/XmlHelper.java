package com.wwflgames.fury.util;

import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.mob.StatHolder;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public class XmlHelper {

    // given this snippet of XML:
    //    <itemDeck>
    //        <item name="Mace of Crushing"/>
    //    </itemDeck>
    // creates a itemDeck for it, using the provided factory. Used by the
    // Monster factory and the Profession factory
/*
    public static ItemDeck createDeck(XMLElement childNode, ItemFactory itemFactory) {
        XMLElementList list = childNode.getChildrenByName("itemDeck");
        if (list.size() == 0) {
            return null;
        }
        XMLElement deckNode = list.get(0);
        return createDeckFromNode(deckNode, itemFactory);
    }

    public static ItemDeck createDeckFromNode(XMLElement deckNode, ItemFactory itemFactory) {
        XMLElementList items = deckNode.getChildren();
        ItemDeck deck = new ItemDeck();
        for (int idx = 0; idx < items.size(); idx++) {
            XMLElement item = items.get(idx);
            deck.addItem(itemFactory.getItemByName(item.getAttribute("name")));
        }
        Log.debug("itemDeck is " + deck);

        return deck;
    }
    */

    public static void addStats(XMLElement childNode, StatHolder statHolder) throws SlickXMLException {
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
            statHolder.setStatValue(stat, value);
        }
    }

    public static String extractContentFromNodeValueByName(XMLElement node, String subNodeName) {
        return node.getChildrenByName(subNodeName).get(0).getContent();
    }

    // used when you are sure there is only one child
    public static XMLElement extractSubNodeByName(XMLElement node, String subNodeName) {
        return node.getChildrenByName(subNodeName).get(0);
    }

}
