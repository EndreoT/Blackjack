package blackjackgame;

import java.util.Arrays;
import java.util.Comparator;


public class PlayingCard {

    private final Rank RANK;
    private final Suit SUIT;
       
    public PlayingCard(Rank rank, Suit suit){
        assert(Arrays.asList(Rank.values()).contains(rank)) == true; 
        assert(Arrays.asList(Suit.values()).contains(suit)) == true;
        this.RANK = rank;
        this.SUIT = suit;
    }
    
    public Rank getRank() {return RANK;}
    
    public Suit getSuit() {return SUIT;}
    
    public int cardvalue(){return this.getRank().getValue();}
    
    public int compareCards(
            PlayingCard other, Comparator<PlayingCard> comparator) {
        return comparator.compare(this, other);
    }
        
    public static int compareValues(PlayingCard card1, PlayingCard card2) {
        if (card1.getRank().getValue() > card2.getRank().getValue()) {
            return 1;
        } else if (card1.getRank().getValue() < card2.getRank().getValue()) {
            return -1; 
        } else {
            if (card1.getSuit().getValue() > card2.getSuit().getValue()) {
                return 1;
            } else if (card1.getSuit().getValue() < card2.getSuit().getValue()) {
                return -1;
            }
            return 0;  
        }     
    }
    
    @Override
    public String toString() {
        return RANK.getText() + " of " + SUIT.getText();
    }
}
