package com.wwflgames.fury.item;

import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Shuffler;

import java.util.ArrayList;
import java.util.List;

public class ItemDeck {

    private List<Item> deck = new ArrayList<Item>();
    private int idx;

    public ItemDeck() {
    }

    public ItemDeck(ItemDeck other) {
        deck.addAll(other.deck);
    }

    public void addItem(Item item) {
        deck.add(item);
    }

    public void addAllItems(List<Item> items) {
        deck.addAll(items);
    }

    public void removeItem(Item item) {
        deck.remove(item);
    }

    public void shuffle() {
        Shuffler.shuffle(deck);
        idx = 0;
    }

    public Item nextItem() {
        if (idx == deck.size()) {
            Log.debug("Reshuffling itemDeck");
            shuffle();
        }
        Item item = deck.get(idx);
        idx++;
        return item;
    }

    public List<Item> getDeck() {
        return deck;
    }

    @Override
    public String toString() {
        return "ItemDeck{" +
                "itemDeck=" + deck +
                ", idx=" + idx +
                '}';
    }
}
