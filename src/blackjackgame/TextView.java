package blackjackgame;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class TextView {

    public void dealingCards() {
        System.out.println("Dealing cards...");
    }

    public String humanAction(Player player) {
        System.out.println("Choose your move: Hit or Stay");
        Scanner choice;
        Character result;

        while (true) {
            choice = new Scanner(System.in);
            result = choice.next().charAt(0);
            result = Character.toLowerCase(result);
            if (result == 'h' || result == 's') {
                break;
            }
            System.out.println("Please try again: Hit or stay");
        }
        return (result == 'h') ? "Hit" : "Stay";
    }

    public float getPlayerBet(Player player) {
        if (player instanceof HumanPlayer) {
            System.out.println("Place your bets. Enter '0' for no bet. ");
            Scanner choice = new Scanner(System.in);
            while (true) {
                try {
                    float amount = Float.valueOf(choice.next());
                    if (amount > player.getTotalMoney()) {
                        System.out.println("Bet must not exceed current money.");
                    } else {
                        return amount;
                    }
                } catch (NumberFormatException n) {
                    System.out.println("Bet must be a number. Try again.");
                    choice = new Scanner(System.in);
                }
            }
        } else {
            // TODO allow for bot bets
            return 0;
        }
    }

    public void showDealersFaceUpCard(Dealer dealer) {
        System.out.println("Dealer's hand is one face down card and " + dealer.showHand().get(1) + ", Shown Total: "
                + dealer.showHand().get(1).getRank().getValue());
    }

    public void showNonDealerHands(List<Player> players) {
        players.stream().filter(p -> !(p instanceof Dealer)).forEach(this::displayPlayerHand);
    }

    public void printActivePlayers(List<Player> activePlayerList) {
        System.out.println("Active players: " + activePlayerList);
    }

    public void displayPlayerHand(Player player) {
        System.out.println(player + "'s hand: " + player.showHand() + ", Total: " + player.getCardTotals());
    }

    public void playerBusts(Player player) {
        System.out.println(player + " has a total of " + player.getCardTotals() + " and busts.");
    }

    public void displayPlayer(Player player) {
        System.out.println("\n" + player + " is currently playing");
        if (!(player instanceof Dealer)) {
            System.out.println(player + "'s" + " bet is " + player.getBetAmount());
        }
    }

    public void printMoneyPerPlayer(Map<Player, Float> showMoneyPerPlayer) {
        System.out.println("Money per player: " + showMoneyPerPlayer);
    }

    public void printMoneyInPot(double money) {
        System.out.println("Total money in pot: " + money);
    }

    public void playerLosesBets(Player p) {
        System.out.println(p + " loses their bet of " + p.getBetAmount());
    }

    public void printPlayerAction(String action, Player p) {
        if (action.equals("Stay")) {
            System.out.println(p + " stays.");
        } else {
            System.out.println(p + " draws a card.");
        }
    }

    public void printPlayersLosingBets(List<Player> players) {
        players.forEach((p) -> {
            System.out.println("Dealer takes " + p + " 's bet of " + p.getBetAmount());
        });
    }

    public void dealerPays(Map<Player, Float> map) {
        BiConsumer<Player, Float> biConsumer = (key, value) -> System.out
                .println(key + " receives " + value + " from Dealer.");
        map.forEach(biConsumer);
    }

    public void endOfRound(List<Player> players) {
        System.out.println("End of round" + "\n");
    }
}
