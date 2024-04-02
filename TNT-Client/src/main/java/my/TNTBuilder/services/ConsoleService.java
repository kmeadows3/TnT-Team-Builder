package my.TNTBuilder.services;


import my.TNTBuilder.model.*;
import my.TNTBuilder.model.userModels.UserCredentials;

import java.util.ArrayList;
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
        System.out.println("(0) Finish Editing Team");
        System.out.println();
    }

    public void editUnitMenu(){
        System.out.println();
        System.out.println("(1) Change Unit Name");
        System.out.println("(2) Manage Experience and Leveling");
        System.out.println("(3) Manage Inventory");
        System.out.println("(4) Manage Injuries");
        System.out.println("(0) Finish Editing Unit");
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

    public void displayUnit(Unit unit){
        System.out.println();
        printFullTopLine();
        paddedDisplay(unit.getName(), BOX_WIDTH, true, true);
        printBasicInformation(unit);
        printStatBlock(unit);
        printInventory();
        printSkillList(unit);
        printExperinceAndUpkeep(unit);
        printAvailableSkillsets(unit);
        printFullBottomLine();
    }

    private void printAvailableSkillsets(Unit unit) {
        paddedDisplay("AVAILABLE SKILLSETS", BOX_WIDTH, true,true);
        StringBuilder appendString = new StringBuilder(new StringBuilder());
        for (Skillset skillset : unit.getAvailableSkillsets()){
            appendString.append(skillset.getName()).append(" ");
        }
        String skillsetString = appendString.toString();
        paddedDisplay(skillsetString, BOX_WIDTH, true, true);
    }

    private void printExperinceAndUpkeep(Unit unit) {
        paddedDisplay("SPENT EXP", 17, true, false);
        paddedDisplay("UNSPENT EXP", 18, false, false);
        paddedDisplay("COST TO ADVANCE", 18, false, false);
        paddedDisplay("UPKEEP", 17, false, true);
        paddedDisplay(Integer.toString(unit.getSpentExperience()), 17, true, false);
        paddedDisplay(Integer.toString(unit.getUnspentExperience()), 18, false, false);
        paddedDisplay(Integer.toString(unit.getCostToAdvance()), 18, false, false);
        paddedDisplay(Integer.toString(unit.getUnitUpkeep()), 17, false, true);
        printFullMiddleLine();
    }

    private void printInventory() {
        paddedDisplay("INVENTORY", BOX_WIDTH, true, true);
        System.out.printf("║ %-71s ║%n", "Not Implemented");
        printFullMiddleLine();
    }

    private void printSkillList(Unit unit) {
        paddedDisplay("SPECIAL ABILITIES", BOX_WIDTH, true, true);
        for (int i = 0; i < unit.getSkills().size(); i++){
            Skill skill = unit.getSkills().get(i);
            printSkill(skill);
            if(i < unit.getSkills().size()-1) {
                System.out.printf("║ %-71s ║%n", "");
            }
        }
        printFullMiddleLine();
    }

    private void printBasicInformation(Unit unit) {
        printFullMiddleLine();
        paddedDisplay("TITLE", 23, true, false);
        paddedDisplay("RANK", 24, false, false);
        paddedDisplay("TYPE", 24, false, true);
        paddedDisplay(unit.getUnitClass(), 23, true, false);
        paddedDisplay(unit.getRank(), 24, false, false);
        paddedDisplay(unit.getSpecies(), 24, false, true);
        printFullMiddleLine();
        paddedDisplay("METTLE", 23, true, false);
        paddedDisplay("WOUNDS", 24, false, false);
        paddedDisplay("BS COST", 24, false, true);
        paddedDisplay(Integer.toString(unit.getMettle()), 23, true, false);
        paddedDisplay(Integer.toString(unit.getWounds()), 24, false, false);
        paddedDisplay(Integer.toString(unit.getBSCost()), 24, false, true);
        printFullMiddleLine();

    }

    private void printStatBlock(Unit unit) {
        paddedDisplay("MOVE", 14, true, false);
        paddedDisplay("MELEE", 14, false, false);
        paddedDisplay("RANGED", 14, false, false);
        paddedDisplay("STRENGTH", 14, false, false);
        paddedDisplay("DEFENSE", 13, false, true);
        paddedDisplay(Integer.toString(unit.getMove()), 14, true, false);
        paddedDisplay(Integer.toString(unit.getMelee()), 14, false, false);
        paddedDisplay(Integer.toString(unit.getRanged()), 14, false, false);
        paddedDisplay(Integer.toString(unit.getStrength()), 14, false, false);
        paddedDisplay(Integer.toString(unit.getDefense()), 13, false, true);
        printFullMiddleLine();
    }

    private void printSkill(Skill skill) {
        if (skill.getDescription().length() <= 54){
            System.out.printf("║ %-15s | %-53s ║%n", skill.getName(), skill.getDescription());
        } else {
            List<String> brokenDesc = new ArrayList<>();
            for (int i = 0; i < skill.getDescription().length() - 53; i += 53){
                String substring = skill.getDescription().substring(i, i + 53);
                brokenDesc.add(substring);
            }
            int brokenLength = brokenDesc.size() * 53;
            brokenDesc.add(skill.getDescription().substring(brokenLength));
            for (int i = 0; i < brokenDesc.size(); i++){
                if (i == 0){
                    System.out.printf("║ %-15s | %-53s ║%n", skill.getName(), brokenDesc.get(i));
                } else {
                    System.out.printf("║ %-15s | %-53s ║%n", "", brokenDesc.get(i));
                }
            }
        }
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

    public int getUnitSelectionForNewUnit(List<Unit> unitsForPurchase){
        for (int i = 0; i < unitsForPurchase.size(); i++){
            System.out.printf("(%d) %s - %s%n", i + 1, unitsForPurchase.get(i).getUnitClass(), unitsForPurchase.get(i).getRank());
        }
        int selection = promptForMenuSelection("Select your unit (0 to exit): ");
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
                String unitInfo = "(" + (i + 1) + ") " + unit.getName() + " - " + unit.getUnitClass() + " - " +
                        unit.getBSCost() + " BS";
                int paddingAmount = BOX_WIDTH - unitInfo.length() - 1;
                System.out.printf("║ %s%" + paddingAmount + "s║%n", unitInfo, "");
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
