import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        displayWelcomeMessage();
        explainRules();
        setupGame(input);

        
    }

    //method to display welcome message
    public static void displayWelcomeMessage() {
        System.out.println("Welcome to Chocolate Chilli!");
    }

    //method to explain the rules of the game
    public static void explainRules() {
        System.out.println("");
        System.out.println("The rules of Chocolate Chilli are as follows:");
        System.out.println("Players take turns eating 1, 2 or 3 chocolates.");
        System.out.println("Whoever takes the last chocolate wins.");
        System.out.println("The losing player must eat the chilli!");
        System.out.println("");
    }

    //method to set up the game
    public static void setupGame(Scanner input){
        String player1;
        System.out.print("Enter name for Player 1: ");
        player1 = input.nextLine();

        System.out.print("How many chocolates would you like to start with? ");
        String chocolates1 = input.nextLine();
        int chococlates = Integer.parseInt(chocolates1);

        System.out.println("");
        System.out.println("Thanks " + player1) ;
        System.out.println("You have " + chococlates + " chocolates to start with.");
        System.out.println("I will go first!");
    }
}

    