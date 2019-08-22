package src.blackjackgame;

//TODO allow for ace == 1 or ace == 11
public enum Rank {
    ACE(1, "Ace"), TWO(2, "Two"), THREE(3, "Three"), FOUR(4, "Four"), FIVE(5, "Five"), SIX(6, "Six"), SEVEN(7, "Seven"),
    EIGHT(8, "Eight"), NINE(9, "Nine"), TEN(10, "Ten"), JACK(10, "Jack"), QUEEN(10, "Queen"), KING(10, "King");

    private final int value;
    private final String text;

    Rank(int value, String text) {
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