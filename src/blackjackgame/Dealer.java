package blackjackgame;

public class Dealer extends Player {

    public Dealer() {
        this.playerName = "Dealer";
        totalMoney = 1000;
    }

    public PlayingCard showFaceUpCard() {
        return showHand().get(1);
    }
}
