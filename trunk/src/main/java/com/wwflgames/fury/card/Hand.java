package com.wwflgames.fury.card;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private int maxHandSize = 2;
    private Deck deck;
    private List<Card> hand = new ArrayList<Card>();

    public Hand(Deck deck) {
        this.deck = deck;
    }

    //TODO: handle shuffling different, maybe. Should shuffling cost you a battle round?
    public void drawToMax() {
        while (hand.size() < maxHandSize ) {
            if ( !deck.hasNextCard() ) {
                deck.shuffle();
            }
            hand.add(deck.nextCard());
        }
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setMaxHandSize(int max) {
        maxHandSize = max;
    }

    public int getMaxHandSize() {
        return maxHandSize;
    }

    public void playCard(Card card) {
        hand.remove(card);
    }


}
