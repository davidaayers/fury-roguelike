package com.wwflgames.fury.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.util.Log;

@Singleton
public class PlayerFactoryImpl implements PlayerFactory {

    private ProfessionFactory professionFactory;

    @Inject
    public PlayerFactoryImpl(ProfessionFactory professionFactory) {
        this.professionFactory = professionFactory;
    }

    @Override
    public Player createForProfession(Profession profession) {
        Log.debug("Profession chosen: " + profession);
        Player player = new Player(profession.getName(), profession);
        ItemDeck deck = profession.getStarterDeck();
        player.installDeck(1, deck);
        player.setDefaultDeck(1);
        profession.installStarterStatsOnPlayer(player);
        // also add all of the items in the starter deck to the list
        // of items available to the player
        for (Item item : deck.getDeck()) {
            player.addItem(item);
        }
        return player;
    }

}
