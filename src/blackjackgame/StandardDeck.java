package src.blackjackgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StandardDeck {

    public final int DECKSIZE = 52;
    protected final List<PlayingCard> DECK_OF_CARDS = makeDeck();

    private List<PlayingCard> makeDeck() {
        List<PlayingCard> playingCards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                playingCards.add(new PlayingCard(rank, suit));
            }
        }
        return playingCards;
    }

    public List getDeck() {
        return DECK_OF_CARDS;
    }

    public void sortDeck() {
        Collections.sort(DECK_OF_CARDS, Comparator.comparing(PlayingCard::getSuit).thenComparing(PlayingCard::getRank));
    }

    public void shuffleDeck() {
        Collections.shuffle(DECK_OF_CARDS);
    }
}
