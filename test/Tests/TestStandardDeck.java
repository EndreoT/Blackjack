package Tests;

import blackjackgame.StandardDeck;
import blackjackgame.PlayingCard;

import static org.junit.Assert.*;
import org.junit.*;


public class TestStandardDeck {
    StandardDeck deck = new StandardDeck();
    
    @Test
    public void testDeckSize(){
        assertEquals(deck.getDeck().size(), 52);
    }
    
    @Test
    public void testDeckOrder() {  
        deck.shuffleDeck();
        deck.sortDeck();
        
        PlayingCard card = (PlayingCard)deck.getDeck().get(0);
        int value = card.getRank().getValue();
        assertEquals(value, 1);   
    }   
}
