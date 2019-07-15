package Tests;

import blackjackgame.PlayingCard;
import blackjackgame.HumanPlayer;
import blackjackgame.Rank;
import blackjackgame.Suit;

import static org.junit.Assert.*;
import org.junit.*;

public class TestPlayer {

    HumanPlayer player;

    @Before
    public void setUp() {
        player = new HumanPlayer("Player");
        PlayingCard p1 = new PlayingCard(Rank.EIGHT, Suit.DIAMONDS);
        PlayingCard p2 = new PlayingCard(Rank.JACK, Suit.SPADES);
        player.addToHand(p1, p2);
    }

    @Test
    public void testAddToBet() {
        player.addToBet(5.0f);
        assertEquals(5.0f, player.getBetAmount(), 0);
        assertEquals(player.getTotalMoney(), 95.0f, 0);
    }

    @Test
    public void testGetCardTotals() {
        float cardTotal = player.getCardTotals();
        assertEquals(cardTotal, 18, 0);
    }

    @Test
    public void testclearHand() {
        player.clearHand();
        assertEquals(player.showHand().size(), 0, 0);
    }
}
