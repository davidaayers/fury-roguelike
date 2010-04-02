package com.wwflgames.fury.item;

public interface ItemFactory {
    Item getItemByName(String name);

    Item randomItem(int points);
}
