/*
 * TITLE: Chocolate Chilli - Mini Project Level 4
 * AUTHOR: Sajid Mohammed
 * STUDENT NUMBER: 260490876
 * DATE: 5 November 2026
 * VERSION: 4.0
 * DESCRIPTION: Plays Chocolate Chilli against a human player.
 * Both players take one to three chocolates per turn, and the
 * player taking the last chocolate wins. Invalid moves are rejected.
 */

package Levels.Level4;

import java.util.Scanner;

public class Main {

    // Starts the program and runs the game.
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String playerName;
        int startingChocolates;

        displayWelcomeMessage();
        explainRules();
        playerName = getPlayerName(input);
        startingChocolates = getStartingChocolates(input);
        displayGameSetup(playerName, startingChocolates);
        playGame(input, playerName, startingChocolates);

        return;
    }


    // Displays the welcome message.
    public static void displayWelcomeMessage() {

        System.out.println("Welcome to Chocolate Chilli!");

        return;
    }


    // Explains the rules of Chocolate Chilli.
    public static void explainRules() {

        System.out.println();
        System.out.println("The rules of Chocolate Chilli are as follows:");
        System.out.println("Players take turns eating 1, 2 or 3 chocolates.");
        System.out.println("Whoever takes the last chocolate wins.");
        System.out.println("The losing player must eat the chilli!");
        System.out.println();

        return;
    }


    // Requests the player's name and returns it.
    public static String getPlayerName(Scanner input) {

        String playerName;

        System.out.print("Enter name for Player 1: ");
        playerName = input.nextLine();

        while (playerName.isBlank()) {
            System.out.print("Please enter a non-blank name: ");
            playerName = input.nextLine();
        }

        return playerName;
    }


    // Requests a valid starting number of chocolates.
    public static int getStartingChocolates(Scanner input) {

        final int MIN_STARTING_CHOCOLATES = 1;
        String inputText;
        int startingChocolates;

        System.out.print("How many chocolates would you like to start with? ");
        inputText = input.nextLine();

        while (!isValidNumberInRange(inputText, MIN_STARTING_CHOCOLATES, Integer.MAX_VALUE)) {
            System.out.println("Please enter a positive whole number (up to "
                    + Integer.MAX_VALUE + ").");
            System.out.print("How many chocolates would you like to start with? ");
            inputText = input.nextLine();
        }

        startingChocolates = Integer.parseInt(inputText);

        return startingChocolates;
    }


    // Displays the starting game information.
    public static void displayGameSetup(String playerName, int startingChocolates) {

        System.out.println();
        System.out.println("Thanks " + playerName + "!");
        System.out.println("You have " + startingChocolates + " "
                + chocolateWord(startingChocolates) + " to start with.");
        System.out.println("I will go first!");

        return;
    }


    // Runs the game until one player wins.
    public static void playGame(Scanner input, String playerName, int chocolatesRemaining) {

        final int NO_CHOCOLATES_LEFT = 0;

        while (chocolatesRemaining > NO_CHOCOLATES_LEFT) {
            chocolatesRemaining = computerTurn(chocolatesRemaining);

            if (!checkEndOfGame(chocolatesRemaining, "Computer", playerName)) {
                chocolatesRemaining = humanTurn(input, playerName, chocolatesRemaining);
                checkEndOfGame(chocolatesRemaining, playerName, "Computer");
            }
        }

        return;
    }


    // Generates and performs a legal computer move.
    public static int computerTurn(int chocolatesRemaining) {

        final int maxMove = 3;
        int maximumMove;
        int computerMove;

        maximumMove = Math.min(maxMove, chocolatesRemaining);
        computerMove = rollDice(maximumMove);
        chocolatesRemaining = chocolatesRemaining - computerMove;

        System.out.println("Computer takes " + computerMove + " "
                + chocolateWord(computerMove) + ".");
        System.out.println("Remaining chocolates: " + chocolatesRemaining);

        return chocolatesRemaining;
    }


    // Handles the human player's turn and updates the chocolate count.
    public static int humanTurn(Scanner input, String playerName, int chocolatesRemaining) {

        final int maxMove = 3;
        int maximumMove;
        int playerMove;

        maximumMove = Math.min(maxMove, chocolatesRemaining);
        playerMove = getPlayerMove(input, playerName, maximumMove);
        chocolatesRemaining = chocolatesRemaining - playerMove;

        System.out.println("You took " + playerMove + " "
                + chocolateWord(playerMove) + ".");
        System.out.println("Remaining chocolates: " + chocolatesRemaining);

        return chocolatesRemaining;
    }


    // Requests a valid move from the player.
    public static int getPlayerMove(Scanner input, String playerName, int maximumMove) {

        final int minChocolate = 1;
        String inputText;
        int playerMove;

        System.out.print(playerName + ", it's your turn. How many chocolates will you take (1 to "
                + maximumMove + ")? ");
        inputText = input.nextLine();

        while (!isValidNumberInRange(inputText, minChocolate, maximumMove)) {
            System.out.println("Invalid move. Enter a whole number between "
                    + minChocolate + " and " + maximumMove + ".");
            System.out.print("How many chocolates will you take? ");
            inputText = input.nextLine();
        }

        playerMove = Integer.parseInt(inputText);

        return playerMove;
    }


    // Checks whether a number is within the allowed range.
    public static boolean isValidNumberInRange(String inputText, int minimum, int maximum) {

        boolean valid;
        int number;

        valid = isValidWholeNumber(inputText);

        if (valid) {
            number = Integer.parseInt(inputText);
            valid = number >= minimum && number <= maximum;
        }

        return valid;
    }


    // Checks whether the input is a valid whole number.
    public static boolean isValidWholeNumber(String inputText) {

    boolean valid;

    valid = true;

    try {
        Integer.parseInt(inputText);
    } catch (NumberFormatException e) {
        valid = false;
    }

    return valid;
}


    // Generates a random computer move within the allowed range.
    public static int rollDice(int maximumMove) {

        final int minChocolate = 1;
        int computerMove;

        computerMove = (int) (Math.random() * maximumMove) + minChocolate;

        return computerMove;
    }


    // Checks if the game has ended and announces the winner.
    public static boolean checkEndOfGame(int chocolatesRemaining,
            String lastPlayer, String losingPlayer) {

        final int NO_CHOCOLATES_LEFT = 0;
        boolean gameEnded;

        gameEnded = chocolatesRemaining == NO_CHOCOLATES_LEFT;

        if (gameEnded) {
            System.out.println("Game over! No more chocolates left.");
            System.out.println(lastPlayer + " wins! " + losingPlayer + " must eat the chilli!");
        }

        return gameEnded;
    }


    // Returns the correct singular or plural word for chocolates.
    public static String chocolateWord(int chocolateCount) {

        final int singleChocolate = 1;
        String word;

        if (chocolateCount == singleChocolate) {
            word = "chocolate";
        } else {
            word = "chocolates";
        }

        return word;
    }

}
