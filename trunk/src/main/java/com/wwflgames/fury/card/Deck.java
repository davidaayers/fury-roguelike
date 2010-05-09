package com.wwflgames.fury.card;

import com.wwflgames.fury.util.Shuffler;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards = new ArrayList<Card>();
    private int idx;

    public void addAllFromDeck(Deck other) {
        cards.addAll(other.cards);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void allAllCards(List<Card> otherCards) {
        cards.addAll(otherCards);
    }

    public void shuffle() {
        Shuffler.shuffle(cards);
        idx = 0;
    }

    public boolean hasNextCard() {
        return idx != cards.size();
    }

    public Card nextCard() {
        if ( idx == cards.size() ) {
            throw new IllegalStateException("Next card called when there were no more cards");
        }

        Card card = cards.get(idx);
        idx++;
        return card;
    }

    public List<Card> getCards() {
        return cards;
    }



}
