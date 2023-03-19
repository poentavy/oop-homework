package implementation;

import implementation.cards.Card;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> deck = null;
    private ArrayList<Card> hand = new ArrayList<>();
    private Card hero = null;
    private int wins = 0;
    private int mana = 1;

    public final void setHero(final Card hero) {
        this.hero = hero;
    }

    public final void setDeck(final ArrayList<Card> deck) {
        this.deck = deck;
    }

    public final ArrayList<Card> getDeck() {
        return deck;
    }

    public final int getWins() {
        return wins;
    }

    public final Card getHero() {
        return hero;
    }

    public final int getMana() {
        return mana;
    }

    public final void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Take card from deck
     */
    public final void takeCardFromDeck() {
        if (!deck.isEmpty()) {
            Card c = deck.get(0);
            deck.remove(0);
            hand.add(c);
        }
    }

    /**
     * Get card from hand
     * @param handIdx index in hand
     * @return card from hand
     */
    public final Card getCardFromHand(final int handIdx) {
        return hand.get(handIdx);
    }

    public final ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Remove card from hand
     * @param handIdx index in hand
     * @return removed card
     */
    public Card removeCardFromHand(final int handIdx) {
        Card c = hand.get(handIdx);
        hand.remove(handIdx);

        return c;
    }

    /**
     * Empty the hand
     */
    public final void emptyHand() {
        hand = new ArrayList<>();
    }

    /**
     * Win
     */
    public void win() {
        wins++;
    }
}
