package com.wwflgames.fury.item;

import com.google.inject.ImplementedBy;

@ImplementedBy(ItemFactoryImpl.class)
public interface ItemFactory {
    Item getItemByName(String name);

    Item randomItem(int points);
}
