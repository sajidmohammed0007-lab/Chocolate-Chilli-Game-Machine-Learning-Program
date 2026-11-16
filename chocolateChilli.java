/*
 * TITLE: Chocolate Chilli - Mini Project Level 6
 * AUTHOR: Sajid Mohammed
 * STUDENT NUMBER: 260490876
 * DATE: 16 November 2026
 * VERSION: 6.0
 * DESCRIPTION: Plays multiple games of Chocolate Chilli against a human.
 * The computer learns by disabling moves that lead to losses and
 * resigns when there are no permitted moves remaining.
 */

import java.util.Scanner;

// Stores the three possible moves for a given number of chocolates.
class GameState {

    boolean takeOne;
    boolean takeTwo;
    boolean takeThree;

} // END class GameState


public class chocolateChilli {

    // Starts the program and runs the games.
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String playerName;
        int startingChocolates;
        int numberOfGames;

        displayWelcomeMessage();
        explainRules();
        playerName = getPlayerName(input);
        startingChocolates = getStartingChocolates(input);
        displayGameSetup(playerName, startingChocolates);
        numberOfGames = getNumberOfGames(input);
        playMultipleGames(input, playerName, startingChocolates, numberOfGames);

        return;
    } // END main


    // Displays the welcome message.
    public static void displayWelcomeMessage() {

        System.out.println("Welcome to Chocolate Chilli!");

        return;
    } // END displayWelcomeMessage


    // Explains the rules of Chocolate Chilli.
    public static void explainRules() {

        System.out.println();
        System.out.println("The rules of Chocolate Chilli are as follows:");
        System.out.println("Players take turns eating 1, 2 or 3 chocolates.");
        System.out.println("Whoever takes the last chocolate wins.");
        System.out.println("The losing player must eat the chilli!");
        System.out.println();

        return;
    } // END explainRules


    // Asks for the player's name and returns it.
    public static String getPlayerName(Scanner input) {

        String playerName;

        System.out.print("Enter name for Player 1: ");
        playerName = input.nextLine();

        while (playerName.isBlank()) {
            System.out.print("Please enter a non-blank name: ");
            playerName = input.nextLine();
        }

        return playerName;
    } // END getPlayerName


    // Asks for a positive starting number of chocolates.
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
    } // END getStartingChocolates


    // Displays the starting game information.
    public static void displayGameSetup(String playerName, int startingChocolates) {

        System.out.println();
        System.out.println("Thanks " + playerName + "!");
        System.out.println("You have " + startingChocolates + " "
                + chocolateWord(startingChocolates) + " to start with.");
        System.out.println("I will go first!");

        return;
    } // END displayGameSetup


    // Plays one game and records the moves and winner.
    public static int[] playGame(Scanner input, String playerName, int startingChocolates,
            GameState[] states, int[] numOfWins, int game) {

        final int NO_CHOCOLATES_LEFT = 0;
        final int COMPUTER_RESIGNS = 0;
        final int COMPUTER_WON = 1;
        final int HUMAN_WON = 0;
        int[] moves = new int[startingChocolates];
        int[] roundMoves;
        int moveCount = 0;
        int chocolatesRemaining = startingChocolates;
        int computerMove;
        int beforeHumanTurn;
        boolean gameFinished = false;

        numOfWins[game] = HUMAN_WON;

        while (!gameFinished && chocolatesRemaining > NO_CHOCOLATES_LEFT) {
            computerMove = chooseComputerMove(chocolatesRemaining, states);

            if (computerMove == COMPUTER_RESIGNS) {
                System.out.println("Computer resigns: no enabled moves remain at "
                        + chocolatesRemaining + " chocolates.");
                System.out.println(playerName + " wins! Computer must eat the chilli!");
                gameFinished = true;
            } else {
                moves[moveCount] = computerMove;
                moveCount = moveCount + 1;
                chocolatesRemaining = chocolatesRemaining - computerMove;
                System.out.println("Computer takes " + computerMove + " "
                        + chocolateWord(computerMove) + ".");
                System.out.println("Remaining chocolates: " + chocolatesRemaining);

                if (checkEndOfGame(chocolatesRemaining, "Computer", playerName)) {
                    numOfWins[game] = COMPUTER_WON;
                    gameFinished = true;
                } else {
                    beforeHumanTurn = chocolatesRemaining;
                    chocolatesRemaining = humanTurn(input, playerName, chocolatesRemaining);
                    moves[moveCount] = beforeHumanTurn - chocolatesRemaining;
                    moveCount = moveCount + 1;
                    gameFinished = checkEndOfGame(chocolatesRemaining, playerName, "Computer");
                }
            }
        }

        roundMoves = new int[moveCount];

        for (int moveIndex = 0; moveIndex < moveCount; moveIndex++) {
            roundMoves[moveIndex] = moves[moveIndex];
        }

        removeLosingMoves(states, roundMoves, numOfWins[game], startingChocolates);

        return roundMoves;
    } // END playGame


    // Creates one record per chocolate count, including zero.
    public static GameState[] createGameStates(int startingChocolates) {

        final int NO_CHOCOLATES_LEFT = 0;
        final int MIN_CHOCOLATES_PER_TURN = 1;
        final int TWO_CHOCOLATES = 2;
        final int THREE_CHOCOLATES = 3;
        GameState[] states = new GameState[startingChocolates + 1];

        for (int count = NO_CHOCOLATES_LEFT; count < states.length; count++) {
            GameState state = new GameState();
            state.takeOne = count >= MIN_CHOCOLATES_PER_TURN;
            state.takeTwo = count >= TWO_CHOCOLATES;
            state.takeThree = count >= THREE_CHOCOLATES;
            states[count] = state;
        }

        return states;
    } // END createGameStates


    // Chooses a legal, enabled computer move, or zero to resign.
    public static int chooseComputerMove(int chocolatesRemaining, GameState[] states) {

        final int MAX_CHOCOLATES_PER_TURN = 3;
        final int COMPUTER_RESIGNS = 0;
        int[] availableMoves = new int[MAX_CHOCOLATES_PER_TURN];
        int availableCount = 0;
        int maximumMove = Math.min(MAX_CHOCOLATES_PER_TURN, chocolatesRemaining);
        int computerMove = COMPUTER_RESIGNS;

        for (int move = 1; move <= maximumMove; move++) {
            if (isMoveAllowed(states[chocolatesRemaining], move)) {
                availableMoves[availableCount] = move;
                availableCount = availableCount + 1;
            }
        }

        if (availableCount > 0) {
            computerMove = availableMoves[rollDice(availableCount) - 1];
        }

        return computerMove;
    } // END chooseComputerMove


    // Reports whether a move is enabled in a game-state record.
    public static boolean isMoveAllowed(GameState state, int move) {

        final int TAKE_ONE = 1;
        final int TAKE_TWO = 2;
        final int TAKE_THREE = 3;
        boolean allowed = false;

        if (move == TAKE_ONE) {
            allowed = state.takeOne;
        } else if (move == TAKE_TWO) {
            allowed = state.takeTwo;
        } else if (move == TAKE_THREE) {
            allowed = state.takeThree;
        }

        return allowed;
    } // END isMoveAllowed


    // Disables the last computer move when the computer loses.
    public static void removeLosingMoves(GameState[] states, int[] moves,
            int computerWon, int startingChocolates) {

        final int COMPUTER_WON = 1;
        final int COMPUTER_TURN = 0;
        final int PLAYERS_PER_ROUND = 2;
        final int TAKE_ONE = 1;
        final int TAKE_TWO = 2;
        final int TAKE_THREE = 3;
        int remaining = startingChocolates;
        int lastComputerState = 0;
        int lastComputerMove = 0;
        GameState state;

        if (computerWon != COMPUTER_WON && moves.length > 0) {
            for (int turn = 0; turn < moves.length; turn++) {
                if (turn % PLAYERS_PER_ROUND == COMPUTER_TURN) {
                    lastComputerState = remaining;
                    lastComputerMove = moves[turn];
                }
                remaining = remaining - moves[turn];
            }

            state = states[lastComputerState];

            if (lastComputerMove == TAKE_ONE) {
                state.takeOne = false;
            } else if (lastComputerMove == TAKE_TWO) {
                state.takeTwo = false;
            } else if (lastComputerMove == TAKE_THREE) {
                state.takeThree = false;
            }
        }

        return;
    } // END removeLosingMoves


    // Performs the human's move and returns the remaining chocolates.
    public static int humanTurn(Scanner input, String playerName, int chocolatesRemaining) {

        final int MAX_CHOCOLATES_PER_TURN = 3;
        int maximumMove = Math.min(MAX_CHOCOLATES_PER_TURN, chocolatesRemaining);
        int playerMove = getPlayerMove(input, playerName, maximumMove);

        chocolatesRemaining = chocolatesRemaining - playerMove;
        System.out.println("You took " + playerMove + " " + chocolateWord(playerMove) + ".");
        System.out.println("Remaining chocolates: " + chocolatesRemaining);

        return chocolatesRemaining;
    } // END humanTurn


    // Asks for a legal number of chocolates to remove.
    public static int getPlayerMove(Scanner input, String playerName, int maximumMove) {

        final int MIN_CHOCOLATES_PER_TURN = 1;
        String inputText;
        int playerMove;

        System.out.print(playerName + ", it's your turn. How many chocolates will you take (1 to "
                + maximumMove + ")? ");
        inputText = input.nextLine();

        while (!isValidNumberInRange(inputText, MIN_CHOCOLATES_PER_TURN, maximumMove)) {
            System.out.println("Invalid move. Enter a whole number between "
                    + MIN_CHOCOLATES_PER_TURN + " and " + maximumMove + ".");
            System.out.print("How many chocolates will you take? ");
            inputText = input.nextLine();
        }

        playerMove = Integer.parseInt(inputText);

        return playerMove;
    } // END getPlayerMove


    // Checks whether an integer entered as text is within a range.
    public static boolean isValidNumberInRange(String inputText, int minimum, int maximum) {

        boolean valid = isValidWholeNumber(inputText);
        int number;

        if (valid) {
            number = Integer.parseInt(inputText);
            valid = number >= minimum && number <= maximum;
        }

        return valid;
    } // END isValidNumberInRange


    // Checks whether text represents a whole integer without an exception.
    public static boolean isValidWholeNumber(String inputText) {

        Scanner numberChecker;
        boolean valid = inputText.matches("[+-]?[0-9]+");

        if (valid) {
            numberChecker = new Scanner(inputText);
            valid = numberChecker.hasNextInt();
        }

        return valid;
    } // END isValidWholeNumber


    // Returns a random computer move within the permitted range.
    public static int rollDice(int maximumMove) {

        final int MIN_CHOCOLATES_PER_TURN = 1;
        int computerMove = (int) (Math.random() * maximumMove) + MIN_CHOCOLATES_PER_TURN;

        return computerMove;
    } // END rollDice


    // Announces a winner when no chocolates remain.
    public static boolean checkEndOfGame(int chocolatesRemaining,
            String lastPlayer, String losingPlayer) {

        final int NO_CHOCOLATES_LEFT = 0;
        boolean gameEnded = chocolatesRemaining == NO_CHOCOLATES_LEFT;

        if (gameEnded) {
            System.out.println("Game over! No more chocolates left.");
            System.out.println(lastPlayer + " wins! " + losingPlayer + " must eat the chilli!");
        }

        return gameEnded;
    } // END checkEndOfGame


    // Returns the correct singular or plural word for chocolate.
    public static String chocolateWord(int chocolateCount) {

        final int SINGLE_CHOCOLATE = 1;
        String word;

        if (chocolateCount == SINGLE_CHOCOLATE) {
            word = "chocolate";
        } else {
            word = "chocolates";
        }

        return word;
    } // END chocolateWord


    // Asks how many games to play.
    public static int getNumberOfGames(Scanner input) {

        final int MIN_NUMBER_OF_GAMES = 1;
        String inputText;
        int numberOfGames;

        System.out.print("How many games would you like to play? ");
        inputText = input.nextLine();

        while (!isValidNumberInRange(inputText, MIN_NUMBER_OF_GAMES, Integer.MAX_VALUE)) {
            System.out.print("Please enter a positive whole number of games: ");
            inputText = input.nextLine();
        }

        numberOfGames = Integer.parseInt(inputText);

        return numberOfGames;
    } // END getNumberOfGames


    // Plays multiple games while retaining the learned moves.
    public static void playMultipleGames(Scanner input, String playerName,
            int startingChocolates, int numberOfGames) {

        int[][] moveHistory = new int[numberOfGames][];
        int[] numOfWins = new int[numberOfGames];
        int totalWins = 0;
        GameState[] states = createGameStates(startingChocolates);

        for (int game = 0; game < numberOfGames; game++) {
            System.out.println("Starting game " + (game + 1) + " of " + numberOfGames);
            moveHistory[game] = playGame(input, playerName, startingChocolates,
                    states, numOfWins, game);
            totalWins = totalWins + numOfWins[game];
            displayWinStatistics(totalWins, game + 1);
        }

        System.out.println("Computer won " + totalWins + " out of " + numberOfGames + " games.");
        displayGameHistory(moveHistory, numOfWins);

        return;
    } // END playMultipleGames


    // Displays win totals and whole-number win percentages for both players.
    public static void displayWinStatistics(int computerWins, int gamesPlayed) {

        int humanWins = gamesPlayed - computerWins;
        int computerWinPercentage = (int) (100.0 * computerWins / gamesPlayed);
        int humanWinPercentage = (int) (100.0 * humanWins / gamesPlayed);

        System.out.println("Games played: " + gamesPlayed);
        System.out.println("Computer wins: " + computerWins + " (" + computerWinPercentage + "%)");
        System.out.println("Human wins: " + humanWins + " (" + humanWinPercentage + "%)");

        return;
    } // END displayWinStatistics


    // Shows each game's moves and result.
    public static void displayGameHistory(int[][] moveHistory, int[] numOfWins) {

        final int COMPUTER_WON = 1;

        System.out.println("Moves in previous games:");

        for (int game = 0; game < moveHistory.length; game++) {
            System.out.print("Game " + (game + 1) + ": ");

            for (int turn = 0; turn < moveHistory[game].length; turn++) {
                System.out.print(moveHistory[game][turn] + " ");
            }

            if (numOfWins[game] == COMPUTER_WON) {
                System.out.println("(Win)");
            } else {
                System.out.println("(Loss)");
            }
        }

        return;
    } // END displayGameHistory

} // END class chocolateChilli
