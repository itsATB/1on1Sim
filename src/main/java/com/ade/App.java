package com.ade;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------------------------");
        System.out.println("Welcome to the 1-on-1 Basketball Game Simulator!");
        System.out.println("--------------------------------------------------");

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
                case 2 -> addNewPlayer();
                case 3 -> deletePlayer();
                default -> displayInvalidChoice();
            }
        }
    }

    private static void viewPlayerRoster() {
        System.out.println("\nViewing the player roster is not implemented yet.");
    }

    private static void addNewPlayer() {
        System.out.println("\nAdding a new player is not implemented yet.");
    }

    private static void deletePlayer() {
        System.out.println("\nDeleting a player is not implemented yet.");
    }

    private static void simulateGame() {
        System.out.println("Game simulation is not implemented yet.");
    }

    private static void exportPlayerRoster() {
        System.out.println("Player roster export is not implemented yet.");
    }

    private static void displayMenu() {
        System.out.println("\n--- 1on1 Sim Menu ---");
        System.out.println("1. View & manage player roster");
        System.out.println("2. Simulate game");
        System.out.println("3. Export current player roster");
        System.out.println("0. Exit program");
    }

    private static void displayRosterMenu() {
        System.out.println("\n--- Player Roster Menu ---");
        System.out.println("1. View player roster");
        System.out.println("2. Add new player");
        System.out.println("3. Delete player");
        System.out.println("0. Back to main menu");
    }

    private static void displayInvalidChoice() {
        System.out.println("Invalid choice. Please enter an integer between 0 and 3.");
    }

}
