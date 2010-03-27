package com.wwflgames.fury.item;

import com.wwflgames.fury.util.Log;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public class ItemDeckXmlHelper {

    // given this snippet of XML:
    //    <deck>
    //        <item name="Mace of Crushing"/>
    //    </deck>
    // creates a deck for it, using the provided factory. Used by the
    // Monster factory and the Profession factory
    public static ItemDeck createDeck(XMLElement childNode, ItemFactory itemFactory) {
        XMLElementList list = childNode.getChildrenByName("deck");
        if (list.size() == 0) {
            return null;
        }
        XMLElement deckNode = list.get(0);
        XMLElementList items = deckNode.getChildren();
        ItemDeck deck = new ItemDeck();
        for (int idx = 0; idx < items.size(); idx++) {
            XMLElement item = items.get(idx);
            deck.addItem(itemFactory.getItemByName(item.getAttribute("name")));
        }
        Log.debug("deck is " + deck);

        return deck;
    }

}
