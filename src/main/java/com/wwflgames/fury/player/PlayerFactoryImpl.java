package com.wwflgames.fury.player;

import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.card.CardFactory;
import com.wwflgames.fury.card.Deck;
import com.wwflgames.fury.util.Log;

public class PlayerFactoryImpl implements PlayerFactory {

    private ProfessionFactory professionFactory;
    private CardFactory cardFactory;

    public PlayerFactoryImpl(ProfessionFactory professionFactory, CardFactory cardFactory) {
        this.professionFactory = professionFactory;
        this.cardFactory = cardFactory;
    }

    @Override
    public Player createForProfession(Profession profession) {
        Log.debug("Profession chosen: " + profession);
        Player player = new Player(profession.getName(), profession);
        profession.installStarterStatsOnPlayer(player);

        //TEMP TEMP TEMP TEMP
        //TODO: create xml thingy for cards
        // create a some cards and add them to a deck
        Card c1 = cardFactory.getCardByName("Mace of hurting");
        Card c2 = cardFactory.getCardByName("Sword of stabbing");
        Card c3 = cardFactory.getCardByName("Wand of zapping");

        Deck d = new Deck();
        d.addCard(c1);
        d.addCard(c2);
        d.addCard(c3);

        player.installDeck(d);
        
        return player;
    }

}