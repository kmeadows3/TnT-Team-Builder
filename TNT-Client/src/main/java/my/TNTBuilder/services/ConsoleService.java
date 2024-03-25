package my.TNTBuilder.services;


import my.TNTBuilder.model.Faction;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.userModels.UserCredentials;

import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private final int BOX_WIDTH = 73;

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
        return promptForMenuSelection("Please choose an option: ");
    }

    public void editTeamMenu(){
        System.out.println();
        System.out.println("(1) Change Team Name");
        System.out.println("(2) Edit Unit");
        System.out.println("(3) Add New Unit");
        System.out.println("(4) Manage Money");
        System.out.println("(5) Manage Inventory");
        System.out.println("(6) Save and Exit Menu");
        System.out.println("(0) Exit Menu");
        System.out.println();
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

    public Team setTeamNameAndStartingMoney(Team newTeam) {
        newTeam.setMoney(promptForInt("Starting Money: "));
        newTeam.setName(promptForString("Team Name: "));
        return newTeam;
    }

    public void displayTeamOptions(List<Team> teams){

        for (int i = 0; i < teams.size(); i++){
            Team team = teams.get(i);
            System.out.printf("(%d) %s (%s) - %d%n", i + 1, team.getName(), team.getFaction(), team.getMoney());
        }
        System.out.println();

    }

    public void displayTeam(Team team){

        System.out.println();
        printFullTopLine();
        paddedDisplay(team.getName(), BOX_WIDTH, true, true);
        printFullMiddleLine();

        paddedDisplay("FACTION", 24, true, false);
        paddedDisplay("BS COST", 11, false,false);
        paddedDisplay("UNSPENT BARTER SCRIP", 25, false, false);
        paddedDisplay("UPKEEP", 10, false, true);

        paddedDisplay(team.getFaction(), 24, true, false);
        paddedDisplay(Integer.toString(team.getBSCost()), 11, false, false);
        paddedDisplay(Integer.toString(team.getMoney()), 25, false, false);
        paddedDisplay(Integer.toString(team.getUpkeep()), 10, false, true);

        printFullMiddleLine();
        printTeamBox(team);
        printFullMiddleLine();

        System.out.printf("║ %-71s ║%n", "INVENTORY");
        System.out.printf("║ %-71s ║%n", "- Admittedly, inventory isn't implemented, so I can't print it.");
        printFullBottomLine();

    }

    public int getFactionSelection(List<Faction> factionList) {
        System.out.println();
        for (int i = 0; i < factionList.size(); i++){
            System.out.printf("(%d) %s%n", i + 1, factionList.get(i).getFactionName());
        }
        System.out.println();
        int selection = promptForMenuSelection("Select your faction (0 to exit): ");
        return selection;
    }

    public int validateSelectionFromList(int selection, int listSize){
        if (selection > 0 && selection <= listSize) {
            return selection;
        } else {
            printErrorMessage("Invalid Selection. Please pick a selection from the list above.");
            return -1;
        }
    }

    /*
    PRIVATE METHODS
     */

    private void printTeamBox(Team team) {
        System.out.printf("║ %-71s ║%n", "MEMBERS");
        if (team.getUnitList().isEmpty()){
            System.out.printf("║ %-71s ║%n", "- How sad, this warband has no members :(");
        } else {
            for (int i = 0; i < team.getUnitList().size(); i++){
                Unit unit = team.getUnitList().get(i);
                String unitInfo = "(" + (i + 1) + ") " + unit.getUnitNickname() + " - " + unit.getName() + " - " +
                        unit.getBSCost() + " BS";
                int paddingAmount = BOX_WIDTH - unitInfo.length();
                System.out.printf("║ %s%" + paddingAmount + "s ║%n", unitInfo, "");
            }

        }
    }

    private void paddedDisplay(String string, int boxWidth, boolean isStart, boolean isEnd){
        int startPadding = (boxWidth - string.length()) / 2;
        int endPadding = boxWidth - startPadding - string.length();
        String formatString = "";

        if (isStart){
            formatString += "║";
        } else {
            formatString += "│";
        }

        formatString += "%" + startPadding + "s%s%" + endPadding + "s";

        if (isEnd){
            formatString += "║%n";
        }
        System.out.printf(formatString, "", string, "");
    }

    private void printFullTopLine() {
        System.out.println("╔═════════════════════════════════════════════════════════════════════════╗");
    }

    private void printFullBottomLine() {
        System.out.println("╚═════════════════════════════════════════════════════════════════════════╝");
    }

    private void printFullMiddleLine() {
        System.out.println("╟─────────────────────────────────────────────────────────────────────────╢");
    }




}
