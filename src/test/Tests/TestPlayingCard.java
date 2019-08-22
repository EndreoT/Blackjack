package src.test.Tests;

import src.blackjackgame.PlayingCard;
import src.blackjackgame.Rank;
import src.blackjackgame.Suit;

import static org.junit.Assert.*;
import org.junit.*;

public class TestPlayingCard {
    PlayingCard p1 = new PlayingCard(Rank.EIGHT, Suit.DIAMONDS);
    PlayingCard p2 = new PlayingCard(Rank.EIGHT, Suit.SPADES);

    @Test
    public void testSuitAndRank() {
        assertEquals(p1.getRank().getValue(), 8);
        assertEquals(p1.getSuit().getValue(), 4);
    }

    @Test
    public void testToString() {
        assertEquals(p1.toString(), "Eight of Diamonds");
    }

    @Test
    public void compareCards() {
        int comparison = p1.compareCards(p2, PlayingCard::compareValues);
        assertEquals(comparison, 1);
    }
}
