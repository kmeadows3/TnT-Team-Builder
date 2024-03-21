package my.TNTBuilder.services;


import my.TNTBuilder.model.User;
import my.TNTBuilder.model.UserCredentials;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private final int BOX_WIDTH = 71;

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println();
        System.out.println("********************************");
        System.out.println("Welcome to the TNT Team Builder!");
        System.out.println("********************************");
        System.out.println();
    }

    public void goodbyeMessage(){
        System.out.println();
        System.out.println("--- Exiting Program, Goodbye ---");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("(1) Register");
        System.out.println("(2) Login");
        System.out.println("(0) Exit");
        System.out.println();
    }

    public int printMainMenu() {
        System.out.println();
        System.out.println("(1) New Warband");
        System.out.println("(2) Load Warband");
        System.out.println("(3) Lookup Rule");
        System.out.println("(0) Exit Program");
        System.out.println();
        return promptForMenuSelection("Please choose an option:");
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage(String prompt) {
        System.out.println();
        System.out.println("--- ERROR: " + prompt + " ---");
        System.out.println("--- Hit Enter to Continue ---");
        scanner.nextLine();
    }

    public void printErrorMessage(Exception e) {
        System.out.println();
        System.out.println("--- ERROR: " + e.getMessage() + " ---");
        System.out.println("--- Hit Enter to Continue ---");
        scanner.nextLine();
    }


}
