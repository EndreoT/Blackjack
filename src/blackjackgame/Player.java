package src.blackjackgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {

    static int numberOfPlayers = -1;
    protected String playerName;
    protected float totalMoney = 100f;

    private final List<PlayingCard> PLAYINGHAND = new ArrayList<>();
    private int numberOfWins;
    private float betAmount = 0;

    Player() {
        numberOfPlayers += 1;
        playerName = "Player " + numberOfPlayers;
    }

    public String getName() {
        return playerName;
    }

    public int getWins() {
        return numberOfWins;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public float getBetAmount() {
        return betAmount;
    }

    public void addToBet(float amount) {
        betAmount += amount;
        decreaseFromTotalMoney(amount);
    }

    public void addToTotalMoney(float amount) {
        totalMoney += amount;
    }

    public void decreaseFromTotalMoney(float amount) {
        totalMoney -= amount;
    }

    public void clearBet() {
        betAmount = 0;
    }

    public void incrementWins() {
        numberOfWins++;
    }

    public List<PlayingCard> showHand() {
        return PLAYINGHAND;
    }

    public int getCardTotals() {
        return PLAYINGHAND.stream().map(p -> p.getRank().getValue()).reduce(0, Integer::sum);
    }

    public void addToHand(PlayingCard... cards) {
        PLAYINGHAND.addAll(Arrays.asList(cards));
    }

    public void clearHand() {
        PLAYINGHAND.clear();
    }

    @Override
    public String toString() {
        return playerName;
    }
}
