package blackjackgame;

public enum Suit {
    DIAMONDS(4, "Diamonds"), CLUBS(3, "Clubs"), HEARTS(2, "Hearts"), SPADES(1, "Spades");

    private final int value;
    private final String text;

    Suit(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
