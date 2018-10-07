package blackjackgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class BlackjackGame {
    
    public final float BUY_IN = 5f;
    
    private final List<Player> players;
    private final Dealer dealer;
    private final HumanPlayer human = new HumanPlayer("Human");
    private List<PlayingCard> playingDeck = new StandardDeck().getDeck();
    private final List<PlayingCard> discardPile = new ArrayList<>();
    private final int numberOfPlayers;
    private List<Player> activePlayers;
    private final TextView view = new TextView();       
    
    public BlackjackGame(int numberOfBots) { 
        this.players =  new ArrayList<>(Arrays.asList(human));
        for (int i = 0; i < numberOfBots; i++) {
            Player botPlayer = new Player();
            this.players.add(botPlayer);
        }
        dealer = new Dealer();
        this.players.add(dealer);
        numberOfPlayers = this.players.size();
        shuffleDeck();       
    }
    
    public List<Player> getPlayers() {return players;}
    
    public List<Player> getActivePlayers() {return activePlayers;}
    
    public Player getHuman() {return human;}
    
    public Player getDealer() {return dealer;}
    
    public void shuffleDeck() {Collections.shuffle(playingDeck);}
    
    public List<PlayingCard> getPlayingDeck() {return playingDeck;}
    
    public List<PlayingCard> getDiscardPile() {return discardPile;}
    
    public void activatePlayers() {activePlayers = new ArrayList<>(players);}
    
    public Stream<Player> nonDealerPlayers() {
        return activePlayers
                .stream()
                .filter(p -> !(p instanceof Dealer));
    }
      
    public void initialBet() {
        nonDealerPlayers().forEach(p -> p.addToBet(BUY_IN));
        float amount = view.getPlayerBet(activePlayers.get(0));
        activePlayers.get(0).addToBet(amount);       
    }
    
    public void Initialdeal() {    
        view.dealingCards();
        for (int i = 0; i < numberOfPlayers * 2; i++) {
            Player player = players.get(i % numberOfPlayers);
            dealCard(player);
        }
    }
    
    public void addDiscardPileToDeck() {
        playingDeck.addAll(discardPile);
        discardPile.clear();
    }
 
    public void dealCard(Player player) {
        if (playingDeck.isEmpty()) {
            addDiscardPileToDeck();
            shuffleDeck();
        }
        int lastCardPosition = playingDeck.size() - 1;
        PlayingCard drawnCard = playingDeck.get(lastCardPosition);
        player.addToHand(drawnCard);
        playingDeck.remove(lastCardPosition);    
    }
    
     public String playerAction(Player player) {
        if (player instanceof Dealer) {
            return (player.getCardTotals() < 17) ? "Hit" : "Stay";
        } else if (player instanceof HumanPlayer) {
            return humanAction();
        } else {
            return botAction(player);
        }     
    }
     
    public String humanAction() {
        return view.humanAction(human);
    }
    
    public String botAction(Player player) {
        int dealersShownCard = dealer.showFaceUpCard().cardvalue();
        if (player.getCardTotals() < 12) {
            return "Hit";
        } else if (dealersShownCard < 7 && player.getCardTotals() > 11) {
            return "Stay";
        } else if (dealersShownCard > 6 && player.getCardTotals() < 17)
            return "Hit";    
        return "Stay";
    }
    
    public void dealerPaysAllNonBustPlayers() {
        List<Player> nonBustPlayers = playerComparison(p -> p.getCardTotals() <= 21);
        dealerPays(nonBustPlayers);
        clearBets(players);
    }
    
    public List<Player> playerComparison(Predicate<Player> predicate) {
        return nonDealerPlayers()
                .filter(predicate)
                .collect(Collectors.toList());       
    }
    
    public void dealerReceivesBets(List<Player> list) {
        double betsDealerGains = getBetSum(list);
        view.printPlayersLosingBets(list);
        dealer.addToTotalMoney((float)betsDealerGains);
        clearBets(list);
    }
    
    public void dealerPays(List<Player> list) {
        double betsDealerLoses = getBetSum(list);
        dealer.decreaseFromTotalMoney((float)betsDealerLoses);
        Map<Player, Float> map = list
                .stream()
                .collect(Collectors.toMap(p -> p, p -> p.getBetAmount()));
        BiConsumer<Player, Float> biConsumer = (key, value) -> key.addToTotalMoney(value * 2);
        map.forEach(biConsumer);
        view.dealerPays(map);
        clearBets(list);
    }
    
    public void settlement() {
        List<Player> lowerScoringPlayers = playerComparison(
                p -> p.getCardTotals() < dealer.getCardTotals() && p.getCardTotals() < 21);
        List<Player> higherScoringPlayers = playerComparison(
                p -> p.getCardTotals() > dealer.getCardTotals() && p.getCardTotals() <= 21);
        List<Player> sameScoringPlayers = playerComparison(
                p -> p.getCardTotals() == dealer.getCardTotals() && p.getCardTotals() <= 21);
       
        dealerReceivesBets(lowerScoringPlayers);
        dealerPays(higherScoringPlayers);
        sameScoringPlayers.forEach(p -> p.addToTotalMoney(p.getBetAmount()));
        players.stream().forEach(Player::clearBet);
    }
    
    public double getBetSum(List<Player> list) {
        return list.stream()
                .map(Player::getBetAmount)
                .mapToDouble(b -> b.doubleValue())
                .sum();
    }
    
    public void playerHasNatural21ButNotDealer(Player player) {
        float amountGained = player.getBetAmount() * 1.5f - player.getBetAmount();
        dealer.decreaseFromTotalMoney(amountGained);
        player.addToTotalMoney(amountGained + player.getBetAmount());
        player.clearBet();
        activePlayers.remove(player);
    }
    
    public double sumOfBets(List<Player> list) {
        double result =  nonDealerPlayers()
                .map(Player::getBetAmount)
                .mapToDouble(b -> b.doubleValue())
                .sum();
        return result;
    }
    
    public boolean drawPhase() {            
        if (dealer.getCardTotals() == 21) {
            //Dealer beats all players who do not have a natural 21    
            dealerBeatsAllPlayersWhoDoNotHaveNatural_21();
            return false;
        } else {
            for (Player p : players) {
                if (activePlayers.size() == 1) {
                    break; //Dealer wins if all other players bust
                } else {          
                    view.displayPlayer(p);
                    view.displayPlayerHand(p);
                    if (p.getCardTotals() == 21) {
                        playerHasNatural21ButNotDealer(p);
                        continue;
                    }
                    String action = playerAction(p);
                    cardDrawOutcome(action, p);
                }
            }
            return true;
        } 
    }    
    
    public void dealerBeatsAllPlayersWhoDoNotHaveNatural_21() {
         List<Player> playersLosingBets = playerComparison(
                    p -> 21 > p.getCardTotals() && !(p instanceof Dealer)
            );
            view.printPlayersLosingBets(playersLosingBets);
            List<Player> playersKeepingBets = playerComparison(
                    p -> 21 == p.getCardTotals() && !(p instanceof Dealer));
            dealerReceivesBets(playersLosingBets);
            playersKeepingBets.forEach(p -> p.addToTotalMoney(p.getBetAmount()));
            clearBets(players);
    }
    
    public void cardDrawOutcome(String action, Player p) {
        view.printPlayerAction(action, p);
        while (action.equals("Hit")) {                           
            dealCard(p);
            if (p.getCardTotals() == 21) {
                view.displayPlayerHand(p);  
                break;
            } else if (p.getCardTotals() > 21) {
                view.displayPlayerHand(p); 
                view.playerBusts(p);
                if (!(p instanceof Dealer)) {
                    view.playerLosesBets(p);                  
                    dealer.addToTotalMoney(p.getBetAmount());
                    p.clearBet();  
                    activePlayers.remove(p); 
                }
                break;                      
            }
            view.displayPlayerHand(p);     
            action = playerAction(p);
        }
    }
    
    public void clearAllHands() {
        List<PlayingCard> cardsToBeDiscarded = players.stream()
                .map(Player::showHand)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        discardPile.addAll(cardsToBeDiscarded);
        players.forEach(Player::clearHand);
    }
    
    public void clearBets(List<Player> players) {
         players.forEach(Player::clearBet);
    }

    public void determineOutcome() {
        if (dealer.getCardTotals() > 21) {
            dealerPaysAllNonBustPlayers();
        } else {
            settlement();
        }
        view.printMoneyPerPlayer(mapMoneyPerPlayer());          
    }
    
    public Map<Player, Float> mapMoneyPerPlayer() {
        return players.stream().collect(Collectors.toMap(p -> p, Player::getTotalMoney));
    }
     
    public void playRound() {
        view.printMoneyPerPlayer(mapMoneyPerPlayer());
        activatePlayers();
        initialBet();       
        Initialdeal();
        view.showNonDealerHands(players); 
        view.showDealersFaceUpCard(dealer);
        view.printMoneyPerPlayer(mapMoneyPerPlayer());
        view.printMoneyInPot(sumOfBets(players));
        boolean result = drawPhase();
        if (result) {
            determineOutcome();
        }
        clearAllHands();
        activePlayers.clear();
        view.endOfRound(players);
    }    
}
