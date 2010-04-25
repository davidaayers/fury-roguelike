package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.ItemUsage;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.util.Shuffler;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class ItemDeckTest {
    private ItemDeck itemDeck;
    private TrackingShuffler shuffler;

    @Before
    public void setUp() throws Exception {
        shuffler = new TrackingShuffler();
        Shuffler.installShuffleProvider(shuffler);
        itemDeck = new ItemDeck();
    }

    @Test
    public void testNextItemReturnsNextItemIfThereAreItemsInList() throws Exception {
        Item i1 = createItem("item1");
        Item i2 = createItem("item2");
        itemDeck.addItem(i1);
        itemDeck.addItem(i2);

        itemDeck.shuffle();
        Item nextItem = itemDeck.nextItem();
        assertEquals(1, shuffler.getTimesShuffled());
        assertEquals(i1, nextItem);
    }

    @Test
    public void testNextItemReShufflesAfterLastCard() throws Exception {
        Item i1 = createItem("item1");
        Item i2 = createItem("item2");
        itemDeck.addItem(i1);
        itemDeck.addItem(i2);

        itemDeck.shuffle();
        itemDeck.nextItem();
        itemDeck.nextItem();

        assertEquals(1, shuffler.getTimesShuffled());
        itemDeck.nextItem();
        assertEquals(2, shuffler.getTimesShuffled());

    }


    private Item createItem(final String name) {
        return new Item() {

            @Override
            public String name() {
                return name;
            }

            @Override
            public Item usedBy(Mob mob, ItemUsage result) {
                return null;
            }

            @Override
            public Item usedAgainst(Mob mob, Mob mob2, ItemUsage result) {
                return null;
            }
        };
    }

    class TrackingShuffler implements Shuffler.ShuffleProvider {

        int timesShuffled = 0;

        @Override
        public void shuffle(List list) {
            timesShuffled++;
        }

        public int getTimesShuffled() {
            return timesShuffled;
        }
    }


}
