package com.ade;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import com.ade.model.Player;
import com.ade.model.PlayerStats;
import com.ade.service.PlayerService;

public class App {
    
    public static PlayerService playerService = new PlayerService();
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------------------------");
        System.out.println("Welcome to the 1-on-1 Basketball Game Simulator!");
        System.out.println("--------------------------------------------------");

        initializeData(); // Add some sample data

        boolean running = true;
        while (running) {
            displayMenu();
            System.out.print("Enter your choice (0-3): ");

            if (!input.hasNextLine()) {
                break;
            }

            String userInput = input.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(userInput);
            } catch (NumberFormatException exception) {
                displayInvalidChoice();
                continue;
            }

            switch (choice) {
                case 0 -> {
                    System.out.println("Exiting program. Goodbye!");
                    running = false;
                }
                case 1 -> manageRosterMenu(input);
                case 2 -> simulateGame();
                case 3 -> exportPlayerRoster();
                default -> displayInvalidChoice();
            }
        }
        input.close();
    }

    private static void displayMenu() {
        System.out.println("\n--- 1on1 Sim Menu ---");
        System.out.println("1. View & manage player roster");
        System.out.println("2. Simulate game");
        System.out.println("3. Export current player roster");
        System.out.println("0. Exit program");
    }

    private static void displayInvalidChoice() {
        System.out.println("Invalid choice. Please enter an integer between 0 and 3.");
    }

    private static void manageRosterMenu(Scanner input) {
        boolean inRosterMenu = true;

        while (inRosterMenu) {
            displayRosterMenu();
            System.out.print("Enter your choice (0-3): ");

            if (!input.hasNextLine()) {
                return;
            }

            String userInput = input.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(userInput);
            } catch (NumberFormatException exception) {
                displayInvalidChoice();
                continue;
            }

            switch (choice) {
                case 0 -> inRosterMenu = false;
                case 1 -> viewPlayerRoster();
                case 2 -> addNewPlayer(input);
                case 3 -> deletePlayer();
                default -> displayInvalidChoice();
            }
        }
    }

    private static void displayRosterMenu() {
        System.out.println("\n--- Player Roster Menu ---");
        System.out.println("1. View player roster");
        System.out.println("2. Add new player");
        System.out.println("3. Delete player");
        System.out.println("0. Back to main menu");
    }

    private static void viewPlayerRoster() {
        System.out.println("\n--- Player Roster ---");
        for (Player player : playerService.getAllPlayers()) {
            System.out.println(player);
        }
    }

    private static void addNewPlayer(Scanner input) {
        System.out.println("\n--- Adding New Player ---");
        // Add new player based on user input for player number and name
        System.out.print("Enter the player information in the format 'player number, name, height(in.)': \n");
        // Read the user input and create a new player object
        String playerInfo = input.nextLine().trim();
        System.out.println("Enter the player's stats in the format:\n" 
            + "'threePointShot, midRangeShot, layup, ballHandling, insideDefense, "
            + "outsideDefense, block, steal, speed, agility, stamina, vertical'" );
        String statsInput = input.nextLine().trim();
        
        // Parse the player info and create a new player object
        String[] info = playerInfo.split(",");
        String[] stats = statsInput.split(",");
        if (info.length == 3) {
            try {
                int playerNumber = Integer.parseInt(info[0].trim());
                String playerName = info[1].trim();
                int playerHeight = Integer.parseInt(info[2].trim());
                if (stats.length != 12) {
                    System.out.println("Invalid stats input. Please enter exactly 12 stats.");
                    return;
                }
                int[] statsValues = new int[12];
                for (int i = 0; i < 12; i++) {
                    statsValues[i] = Integer.parseInt(stats[i].trim());
                }
                PlayerStats playerStats = new PlayerStats(
                    statsValues[0], statsValues[1], statsValues[2], statsValues[3],
                    statsValues[4], statsValues[5], statsValues[6], statsValues[7],
                    statsValues[8], statsValues[9], statsValues[10], statsValues[11]
                );
                Player newPlayer = new Player(playerNumber, playerName, playerHeight, playerStats);
                playerService.addPlayer(newPlayer);
                System.out.println("-- Player added successfully!\n\n" + newPlayer);
            } catch (NumberFormatException exception) {
                System.out.println("Invalid player information format.");
            } catch (IllegalArgumentException exception) {
                System.out.println("Error adding player: " + exception.getMessage());
            }

        } else {
            System.out.println("Invalid input format. Please enter the information in the correct format.");
        }

    }

    private static void deletePlayer() {
        // Delete player based on player number input from the user
        System.out.println("\n--- Delete Player By Number ---");

        System.out.print("Enter the player number to delete: ");
        Scanner input = new Scanner(System.in);
        String playerNumber = input.nextLine().trim();  
        try {
            System.out.println("Player #" + playerNumber + " (" + playerService.getPlayerByNumber(playerNumber).getName() + ")" + " has been deleted.");
            playerService.deletePlayer(playerNumber);
        } catch (IllegalArgumentException exception) {
            System.out.println("Error deleting player: " + exception.getMessage());
        }        

    }

    private static void simulateGame() {
        System.out.println("\n--- Simulate Game ---");
    }

    private static void exportPlayerRoster() {
        System.out.println("\n--- Exporting Player Roster ---\n");
    }

    private static void initializeData() {
        // Create sample players and add them to the roster
        Player player1 = new Player(1, "John Doe", 75, new PlayerStats(80, 70, 85, 90, 88, 75, 80, 70, 85, 90, 88, 75));
        Player player2 = new Player(2, "Jane Smith", 70, new PlayerStats(75, 80, 70, 85, 90, 88, 75, 80, 70, 85, 90, 88));
        playerService.addPlayer(player1);
        playerService.addPlayer(player2);
    }

}
