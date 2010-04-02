package com.wwflgames.fury.item;

import com.wwflgames.fury.item.effect.*;
import com.wwflgames.fury.item.effect.damage.Damage;
import com.wwflgames.fury.item.effect.damage.MagicDamage;
import com.wwflgames.fury.item.effect.damage.MeleeDamage;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Shuffler;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFactoryImpl implements ItemFactory {

    private Map<String, Item> itemNameMap = new HashMap<String, Item>();

    public ItemFactoryImpl() throws SlickException {
        parseXml();
    }

    private void parseXml() throws SlickException {
        // read in the monsters xml file
        XMLParser parser = new XMLParser();
        XMLElement element = parser.parse("items.xml");
        XMLElementList children = element.getChildren();
        for (int idx = 0; idx < children.size(); idx++) {
            XMLElement childNode = children.get(idx);
            Log.debug("childNode = " + childNode.getName());
            String itemName = childNode.getAttribute("name");
            Log.debug("itemName = " + itemName);
            createItemFromXml(childNode);
        }
    }

    private void createItemFromXml(XMLElement itemNode) throws SlickXMLException {
        // grab the usedByEffects
        String itemName = itemNode.getAttribute("name");
        ItemEffect[] usedByEffects = getEffectsForNode(itemNode, "usedByEffects");
        ItemEffect[] usedAgainstEffects = getEffectsForNode(itemNode, "usedAgainstEffects");
        Item item = createItem(itemName, usedByEffects, usedAgainstEffects);
        itemNameMap.put(item.name(), item);
        Log.debug("created item " + item);
    }

    private ItemEffect[] getEffectsForNode(XMLElement itemNode, String nodeName) throws SlickXMLException {
        List<ItemEffect> effectsList = new ArrayList<ItemEffect>();
        XMLElementList usedByParent = itemNode.getChildrenByName(nodeName);
        XMLElement usedBy = usedByParent.get(0);
        Log.debug(nodeName + " = " + usedBy.getName());
        XMLElementList usedByChildren = usedBy.getChildren();
        Log.debug(nodeName + "Children.size() = " + usedByChildren.size());
        if (usedByChildren.size() != 0) {
            for (int idx = 0; idx < usedByChildren.size(); idx++) {
                XMLElement usedByChild = usedByChildren.get(idx);
                String name = usedByChild.getName();
                Log.debug(nodeName + "Child.name = " + name);
                ItemEffect effect = createItemEffectFromNode(usedByChild);
                effectsList.add(effect);
            }
        }
        ItemEffect[] effects = null;
        if (!effectsList.isEmpty()) {
            effects = effectsList.toArray(new ItemEffect[effectsList.size()]);
        }
        return effects;

    }

    private ItemEffect createItemEffectFromNode(XMLElement childNode) throws SlickXMLException {
        if (childNode.getName().equals("damage")) {
            String type = childNode.getAttribute("type");
            int value = childNode.getIntAttribute("value");
            Log.debug("Looking for damage matching: " + type);
            Damage dmg = Damage.forType(type);
            Log.debug("type was " + dmg);
            if (dmg instanceof MeleeDamage) {
                MeleeDamageEffect damageEffect = new MeleeDamageEffect((MeleeDamage) dmg, value);
                Log.debug("Created meleeDamageEffect = " + damageEffect.getDesc());
                return damageEffect;
            } else if (dmg instanceof MagicDamage) {
                MagicDamageEffect damageEffect = new MagicDamageEffect((MagicDamage) dmg, value);
                Log.debug("Created magicDamageEffect = " + damageEffect.getDesc());
                return damageEffect;

            }
        } else if (childNode.getName().equals("stat-buff")) {
            Log.debug("Creating stat-buff");
            String statName = childNode.getAttribute("stat");
            int value = childNode.getIntAttribute("value");
            Stat stat = Stat.valueOf(statName.toUpperCase());
            Log.debug("stat was " + stat);
            StatBuffEffect statBuff = new StatBuffEffect(stat, value);
            return statBuff;
        } else if (childNode.getName().equals("attack-buff")) {
            Log.debug("Creating attack-buff");
            String attackType = childNode.getAttribute("type");
            int value = childNode.getIntAttribute("value");
            Damage damage = Damage.forType(attackType);
            AttackBuffEffect attackBuff = new AttackBuffEffect(damage, value);
            return attackBuff;
        }
        return null;
    }


    private Item createItem(String name, ItemEffect[] usedByEffects, ItemEffect[] usedAgainstEffects) {
        return new ItemImpl(name, usedByEffects, usedAgainstEffects);
    }

    @Override
    public Item getItemByName(String name) {
        return itemNameMap.get(name);
    }

    @Override
    public Item randomItem(int points) {
        List<Item> allItems = new ArrayList<Item>(itemNameMap.values());
        Shuffler.shuffle(allItems);
        return allItems.get(0);
    }

}
