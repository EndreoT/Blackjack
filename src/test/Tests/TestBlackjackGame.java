package src.test.Tests;

import src.blackjackgame.BlackjackGame;
import src.blackjackgame.PlayingCard;
import src.blackjackgame.Player;
import src.blackjackgame.Rank;
import src.blackjackgame.Suit;

import java.util.List;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.*;

public class TestBlackjackGame {
    BlackjackGame game;
    int numBotPlayers;
    PlayingCard card1;
    PlayingCard card2;
    PlayingCard card3;
    PlayingCard card4;

    @Before
    public void setUP() {
        card1 = new PlayingCard(Rank.JACK, Suit.DIAMONDS);
        card2 = new PlayingCard(Rank.SIX, Suit.HEARTS);
        card3 = new PlayingCard(Rank.FIVE, Suit.HEARTS);
        card4 = new PlayingCard(Rank.NINE, Suit.CLUBS);
        numBotPlayers = 2;
        game = new BlackjackGame(numBotPlayers);
        game.activatePlayers();
    }

    @Test
    public void testConstructor() {
        assertEquals(game.getPlayers().size(), 4, 0);
        assertEquals(game.getPlayers().get(game.getPlayers().size() - 1).getClass().getName(), "src.blackjackgame.Dealer");
        assertEquals(game.getPlayingDeck().size(), 52, 0);
        assertEquals(game.getDiscardPile().size(), 0, 0);
    }

    @Test
    public void testInitialBet() {
        float playerBet = 10f;
        String input = Float.toString(playerBet);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        game.initialBet();
        assertEquals(game.getHuman().getBetAmount(), playerBet + game.BUY_IN, 0);
        assertEquals(game.getBetSum(game.getPlayers()), game.BUY_IN * (numBotPlayers + 1) + playerBet, 0);
    }

    @Test
    public void testInitialDeal() {
        game.Initialdeal();
        game.getPlayers().forEach((p) -> {
            assertEquals(p.showHand().size(), 2);
        });
        assertEquals(game.getPlayingDeck().size(), 52 - (numBotPlayers + 2) * 2);
        assertEquals(game.getDiscardPile().size(), 0);
    }

    @Test
    public void testaAddDiscardPileToDeck() {
        PlayingCard card = game.getPlayingDeck().remove(51);
        assertEquals(game.getPlayingDeck().size(), 51);
        game.getDiscardPile().add(card);
        game.addDiscardPileToDeck();
        assertEquals(game.getPlayingDeck().size(), 52);
    }

    @Test
    public void testDealCard() {
        assertEquals(game.getPlayingDeck().size(), 52);
        PlayingCard card = game.getPlayingDeck().get(game.getPlayingDeck().size() - 1);
        assertEquals(game.getHuman().showHand().size(), 0);
        game.dealCard(game.getHuman());
        assertEquals(game.getHuman().showHand().size(), 1);
        assertEquals(game.getPlayingDeck().size(), 51);
        assertFalse(game.getPlayingDeck().contains(card));
        assertTrue(game.getHuman().showHand().contains(card));
    }

    @Test
    public void testPlayerAction() {
        Player dealer = game.getDealer();
        dealer.addToHand(card1, card2);
        String action = game.playerAction(dealer);
        assertEquals(action, "Hit");
    }

    @Test
    public void testBotAction() {
        Player dealer = game.getDealer();
        dealer.addToHand(card1, card2);
        Player bot = game.getPlayers().get(1);
        bot.addToHand(card3, card4);
        String action = game.playerAction(bot);
        assertEquals(action, "Stay");
    }

    public List<Player> helper() {
        Player human = game.getHuman();
        Player bot1 = game.getPlayers().get(1);
        Player bot2 = game.getPlayers().get(2);
        human.addToBet(10f);
        bot1.addToBet(5f);
        bot2.addToBet(5f);
        human.addToHand(card1, card2);
        bot1.addToHand(card1, card2, card3);
        bot2.addToHand(card1, card1, card1);
        return Arrays.asList(human, bot1, bot2);
    }

    @Test
    public void testDealerPaysAllNonBustPlayers() {
        List<Player> list = helper();
        Player human = list.get(0);
        Player bot1 = list.get(1);
        Player bot2 = list.get(2);
        game.dealerPaysAllNonBustPlayers();

        assertEquals(game.getDealer().getTotalMoney(), 1000f - ((2 * game.BUY_IN) + 5), 0);
        assertEquals(human.getTotalMoney(), 110f, 0);
        assertEquals(bot1.getTotalMoney(), 105f, 0);
        assertEquals(bot2.getTotalMoney(), 95f, 0);
        game.getPlayers().forEach(p -> assertEquals(p.getBetAmount(), 0, 0));
    }

    @Test
    public void testDealerReceivesBets() {
        List<Player> list = helper();
        Player human = list.get(0);
        Player bot1 = list.get(1);
        Player bot2 = list.get(2);
        game.dealerReceivesBets(game.getPlayers());

        assertEquals(game.getDealer().getTotalMoney(), 1000f + ((3 * game.BUY_IN) + 5), 0);
        assertEquals(human.getTotalMoney(), 90f, 0);
        assertEquals(bot1.getTotalMoney(), 95f, 0);
        assertEquals(bot2.getTotalMoney(), 95f, 0);
        game.getPlayers().forEach(p -> assertEquals(p.getBetAmount(), 0, 0));
    }

    @Test
    public void testSettlement() {
        Player human = game.getHuman();
        Player dealer = game.getDealer();
        Player bot1 = game.getPlayers().get(1);
        Player bot2 = game.getPlayers().get(2);
        human.addToBet(5f);
        bot1.addToBet(5f);
        bot2.addToBet(5f);
        dealer.addToHand(card1, card4);
        human.addToHand(card1, card4);
        bot1.addToHand(card1, card2, card3);
        bot2.addToHand(card2, card3);

        game.settlement();
        assertEquals(game.getDealer().getTotalMoney(), 1000f, 0);
        assertEquals(human.getTotalMoney(), 100f, 0);
        assertEquals(bot1.getTotalMoney(), 105f, 0);
        assertEquals(bot2.getTotalMoney(), 95f, 0);
        game.getPlayers().forEach(p -> assertEquals(p.getBetAmount(), 0, 0));
    }

    @Test
    public void testPlayerHas21ButNotDealer() {
        Player human = game.getHuman();
        human.addToBet(5f);
        game.playerHasNatural21ButNotDealer(human);
        assertEquals(game.getDealer().getTotalMoney(), 1000f - 2.5f, 0);
        assertEquals(human.getTotalMoney(), 102.5f, 0);
    }

    @Test
    public void testDrawPhaseDealerNatural21() {
        Player human = game.getHuman();
        Player dealer = game.getDealer();
        Player bot1 = game.getPlayers().get(1);
        Player bot2 = game.getPlayers().get(2);
        human.addToBet(5f);
        bot1.addToBet(5f);
        bot2.addToBet(5f);
        dealer.addToHand(card1, card2, card3);
        human.addToHand(card1, card2, card3);
        bot1.addToHand(card1, card2);
        bot2.addToHand(card2, card3);
        game.drawPhase();

        assertEquals(game.getDealer().getTotalMoney(), 1000f + 2 * game.BUY_IN, 0);
        assertEquals(human.getTotalMoney(), 100f, 0);
        assertEquals(bot1.getTotalMoney(), 95f, 0);
        assertEquals(bot2.getTotalMoney(), 95f, 0);
        game.getPlayers().forEach(p -> assertEquals(p.getBetAmount(), 0, 0));
    }

    @Test
    public void testhumanDrawsTooMuch() {
        String input = "Hit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Player human = game.getHuman();
        human.addToBet(5f);
        human.addToHand(card1, card2, card3);
        game.cardDrawOutcome(input, human);
        assertEquals(game.getDealer().getTotalMoney(), 1000f + 1 * game.BUY_IN, 0);
        assertEquals(human.getTotalMoney(), 95f, 0);
    }

    @Test
    public void testClearAllHands() {
        game.Initialdeal();
        game.clearAllHands();
        game.getPlayers().forEach(p -> assertEquals(p.showHand().size(), 0, 0));
    }

    @Test
    public void testClearBets() {
        float playerBet = 10f;
        String input = Float.toString(playerBet);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        game.initialBet();
        game.clearBets(game.getPlayers());
        game.getPlayers().forEach(p -> assertEquals(p.getBetAmount(), 0, 0));
    }
}
