package blackjackgame;

import java.util.Scanner;

public class Main {
    
    public static int getNumberOfBots() {
        System.out.println("How many bots do you want to play with?");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                int answer = Integer.valueOf(scanner.next());
                if (answer < 0 || answer > 7) {
                    System.out.println("Pick a number between 0 and 7");
                } else {
                    return answer;
                }  
            } catch (NumberFormatException n) {
                System.out.println("Value must be an integer");
            }
            scanner = new Scanner(System.in);
        }
    }

    public static void main(String[] args) {
        int numberOfBots = getNumberOfBots();
        HumanPlayer human = new HumanPlayer("Human");
        BlackjackGame game = new BlackjackGame(numberOfBots);
     
        while (true) {
            game.playRound();
            if (human.getTotalMoney() >= 0) {
                System.out.println("Sorry, you have no money, goodbye.");
                break;
            }
            System.out.println("Continue p playing? Yes or No");
            Scanner continuePlaying = new Scanner(System.in);
            char answer = continuePlaying.next().charAt(0);
            if (Character.toLowerCase(answer) != 'y') {
                break;
            }
        }
        System.out.println("Thanks for playing!");
    }    
}
