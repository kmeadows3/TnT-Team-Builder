package my.TNTBuilder;

import my.TNTBuilder.model.Faction;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.UnitStarterDTO;
import my.TNTBuilder.model.userModels.AuthenticatedUser;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.userModels.UserCredentials;
import my.TNTBuilder.services.AuthenticationService;
import my.TNTBuilder.services.ConsoleService;
import my.TNTBuilder.services.TeamService;
import my.TNTBuilder.services.UnitService;

import java.util.List;

public class App {

    public static final int MENU_EXIT = 0;
    public static final int LOGIN_MENU_REGISTER = 1;
    public static final int LOGIN_MENU_LOGIN = 2;
    public static final int MAIN_MENU_NEW_WARBAND = 1;
    public static final int MAIN_MENU_LOAD_WARBAD = 2;
    public static final int MAIN_MENU_LOOKUP_RULE = 3;
    public static final int EDIT_TEAM_MENU_CHANGE_NAME = 1;
    public static final int EDIT_TEAM_MENU_EDIT_UNIT = 2;
    public static final int EDIT_TEAM_MENU_ADD_NEW_UNIT = 3;
    public static final int EDIT_TEAM_MENU_MANAGE_MONEY = 4;
    public static final int EDIT_TEAM_MENU_MANAGE_INVENTORY = 5;
    public static final int EDIT_TEAM_MENU_SAVE_AND_EXIT = 6;
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final ConsoleService consoleService = new ConsoleService();
    private final TeamService teamService = new TeamService(API_BASE_URL);
    private final UnitService unitService = new UnitService(API_BASE_URL);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AuthenticatedUser currentUser;
    private TNTException generalError = new TNTException("Unknown Error, check log for details.");
    private Team currentTeam;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    /*
    These methods handle the initial registration/login process. They were obtained from the module-2 capstone
     */
    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            teamService.setCurrentUser(currentUser);
            unitService.setCurrentUser(currentUser);
            mainMenu();
        }
        System.out.println("This line in the run method stops the debugger before program exits");
    }

    /*
    MENUS
     */
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != MENU_EXIT && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == LOGIN_MENU_REGISTER) {
                handleRegister();
            } else if (menuSelection == LOGIN_MENU_LOGIN) {
                handleLogin();
            } else if (menuSelection != MENU_EXIT) {
                consoleService.printMessage("Invalid Selection");
                consoleService.pause();
            }
        }
    }
    private void mainMenu(){
        int selection = -1;
        while (selection != MENU_EXIT) {
            selection = consoleService.printMainMenu();
            if (selection == MAIN_MENU_NEW_WARBAND) {
                createTeam();
            } else if (selection == MAIN_MENU_LOAD_WARBAD) {
                loadTeam();
            } else if (selection == MAIN_MENU_LOOKUP_RULE) {
                consoleService.printErrorMessage("This functionality is not implemented");
            } else if (selection != MENU_EXIT){
                consoleService.printErrorMessage("That is not a valid selection");
            }
        }
        consoleService.goodbyeMessage();
    }
    private void editTeamMenu(Team team){
        currentTeam = team;
        while (true) {
            consoleService.displayTeam(currentTeam);
            consoleService.editTeamMenu();

            int userSelection = consoleService.promptForMenuSelection("Select option from menu: ");
            if (userSelection == MENU_EXIT){
                return;
            } else if (userSelection == EDIT_TEAM_MENU_CHANGE_NAME){
                consoleService.printErrorMessage("Not yet implemented");
            } else if (userSelection == EDIT_TEAM_MENU_EDIT_UNIT){
                consoleService.printErrorMessage("Not yet implemented");
            } else if (userSelection == EDIT_TEAM_MENU_ADD_NEW_UNIT){
                addNewUnit();
            } else if (userSelection == EDIT_TEAM_MENU_MANAGE_MONEY){
                consoleService.printErrorMessage("Not yet implemented");
            } else if (userSelection == EDIT_TEAM_MENU_MANAGE_INVENTORY){
                consoleService.printErrorMessage("Not yet implemented");
            } else if (userSelection == EDIT_TEAM_MENU_SAVE_AND_EXIT) {
                // TODO implement save functionality
                return;
            } else {
                consoleService.printErrorMessage("Not a valid selection");
            }
        }
    }

    /*
    MAIN MENU OPTIONS
     */
    private void loadTeam() {
        List<Team> savedTeams = null;
        try {
            savedTeams = teamService.getTeamsForUser();
            if (savedTeams == null){
                throw new TNTException("No teams returned");
            }
        } catch (TNTException e) {
            consoleService.printErrorMessage(e);
            return;
        }
        int selection = -1;
        while (selection == -1) {
            consoleService.displayTeamOptions(savedTeams);
            selection = consoleService.promptForMenuSelection("Select team from list (0 to return): ");
            if (selection == MENU_EXIT) {
                return;
            }
            selection = consoleService.validateSelectionFromList(selection, savedTeams.size());
        }
        editTeamMenu(savedTeams.get(selection -1));
    }
    private void createTeam() {
        List<Faction> factionList = null;
        try {
            factionList = teamService.getFactions();
        } catch (TNTException e) {
            consoleService.printErrorMessage(e);
            return;
        }
        try {
            Team newTeamInfo = gatherNewTeamInfoFromUser(factionList);
            if (newTeamInfo.getFactionId() == MENU_EXIT){
                return;
            } else {
                Team newTeam = teamService.createTeam(newTeamInfo);
                editTeamMenu(newTeam);
            }
        } catch (TNTException e) {
            consoleService.printErrorMessage(e);
        }
    }

    /*
    EDIT TEAM OPTIONS
     */
    private void addNewUnit(){


        Unit returnedUnit = null;
        List<Unit> potentialUnits = null;
        try {
            potentialUnits = teamService.getUnitsForPurchase(currentTeam.getFactionId());
        }catch (TNTException e){
            consoleService.printErrorMessage(e);
        }

        UnitStarterDTO newUnit = getNewUnitInformation(potentialUnits);
        if (newUnit == null) return;

        try {
            returnedUnit = unitService.addNewUnitToDatabase(newUnit);
        } catch (TNTException e){
            consoleService.printErrorMessage(e);
        }
        System.out.println("STOP");
    }

    private UnitStarterDTO getNewUnitInformation(List<Unit> potentialUnits) {
        UnitStarterDTO newUnit = new UnitStarterDTO();
        newUnit.setTeamId(currentTeam.getId());
        int selection = -1;
        while(selection == -1) {
            selection = consoleService.getUnitSelectionForNewUnit(potentialUnits);
            if (selection == MENU_EXIT){
                return null;
            }
            selection = consoleService.validateSelectionFromList(selection, potentialUnits.size());
        }
        newUnit.setId(selection);
        newUnit.setName(consoleService.promptForString("Name the new unit: "));
        return newUnit;
    }



    /*
    HELPER METHODS
     */

    private Team gatherNewTeamInfoFromUser(List<Faction> factionList){
        Team newTeamInfo = new Team();
        int factionId = -1;
        while (factionId == -1){
            factionId = consoleService.getFactionSelection(factionList);
            if (factionId == MENU_EXIT){
                newTeamInfo.setFactionId(MENU_EXIT);
                return newTeamInfo;
            }
            factionId = consoleService.validateSelectionFromList(factionId, factionList.size());
        }
        newTeamInfo.setFactionId(factionId);
        return consoleService.setTeamNameAndStartingMoney(newTeamInfo);
    }




    /*
    LOGIN METHODS
     */

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage(generalError);
        }
    }

    private void handleRegister() {
        consoleService.printMessage("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            consoleService.printMessage("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage(generalError);
        }
    }

//
//    private void createUnit(){
//        try {
//            String faction = tntService.getCurrentTeam().getFaction();
//            NewUnitInputHelper userInput = consoleService.getNewUnitInformationFromUser(tntService.getReference().getUnitOptions(
//                    faction));
//            tntService.newUnit(userInput.getName(), userInput.getUnit());
//            editUnitMenu();
//        }  catch (NumberFormatException e) {
//            TNTException ex = new TNTException("Please enter a valid number", e);
//            consoleService.printErrorMessage(ex);
//        } catch (TNTException e) {
//            consoleService.printErrorMessage(e);
//        }
//    }
//
//    private void editUnit() throws TNTException {
//        if (tntService.getCurrentTeam().getUnitList().isEmpty()){
//            throw new TNTException("This team has no units to edit");
//        }
//        int unitIndex = consoleService.promptForInt("Select unit number to edit: ") - 1;
//        try {
//            tntService.setCurrentUnit(tntService.getCurrentTeam().getUnitList().get(unitIndex));
//        } catch (IndexOutOfBoundsException e) {
//            throw new TNTException("Please select a valid unit number", e);
//        }
//        editUnitMenu();
//    }
//
//    private void manageMoneyMenu(){
//        while (true){
//            String userSelection = consoleService.manageMoneyMenu();
//            try {
//                if (userSelection.equals("1")) {
//                    gainMoney();
//                } else if (userSelection.equals("2")) {
//                    loseMoney();
//                } else if (userSelection.equals("3")) {
//                    payUpkeep();
//                } else if (userSelection.equals("4")) {
//                    return;
//                } else {
//                    throw new TNTException("Please enter a valid selection");
//                }
//            } catch (TNTException e) {
//                consoleService.printErrorMessage(e);
//            }
//        }
//    }
//
//    private void gainMoney() throws TNTException {
//        String userInput = consoleService.getMoneyGained();
//        int startingFunds = tntService.getCurrentTeam().getMoney();
//        try {
//            int gainedMoney = Integer.parseInt(userInput);
//            tntService.gainMoney(gainedMoney);
//        } catch (FailedMoneyTransaction e) {
//            throw new TNTException(e.getMessage(), e);
//        }
//        int endingFunds = tntService.getCurrentTeam().getMoney();
//        consoleService.moneyFeedback(startingFunds, endingFunds);
//    }
//
//    private void loseMoney () throws TNTException{
//        String userInput = consoleService.getMoneyLost();
//        int startingFunds = tntService.getCurrentTeam().getMoney();
//        try {
//            int spentMoney = Integer.parseInt(userInput);
//            tntService.spendMoney(spentMoney);
//        } catch (FailedMoneyTransaction e) {
//            throw new TNTException(e.getMessage(), e);
//        }
//        int endingFunds = tntService.getCurrentTeam().getMoney();
//        consoleService.moneyFeedback(startingFunds, endingFunds);
//    }
//
//    private void payUpkeep() throws FailedMoneyTransaction {
//        int startingFunds = tntService.getCurrentTeam().getMoney();
//        if (tntService.getCurrentTeam().getUpkeep() > 0) {
//            tntService.spendMoney(tntService.getCurrentTeam().getUpkeep());
//        } else {
//            throw new FailedMoneyTransaction("Your upkeep is " + tntService.getCurrentTeam().getUpkeep());
//        }
//        int endingFunds = tntService.getCurrentTeam().getMoney();
//        consoleService.moneyFeedback(startingFunds, endingFunds);
//    }
//
//
//    private void editUnitMenu(){
//        while (true) {
//            consoleService.displayUnit(tntService.getCurrentUnit());
//            try {
//                String userSelection = consoleService.editUnitMenu();
//                if (userSelection.equals("1")) {
//                    renameUnit();
//                } else if (userSelection.equals("2")) {
//                    manageExp();
//                } else if (userSelection.equals("3")) {
//                    throw new TNTException("This functionality is not implemented");
//                } else if (userSelection.equals("4")) {
//                    throw new TNTException("This functionality is not implemented");
//                } else if (userSelection.equals("5")) {
//                    return;
//                } else {
//                    throw new TNTException("Please enter a valid selection");
//                }
//            } catch (TNTException e) {
//                consoleService.printErrorMessage(e);
//            }
//        }
//    }
//
//    public void renameTeam(){
//        String newName = consoleService.rename();
//        tntService.getCurrentTeam().setName(newName);
//    }
//
//    public void renameUnit(){
//        String newName = consoleService.rename();
//        tntService.getCurrentUnit().setUnitNickname(newName);
//    }
//
//    public void manageExp(){
//        while (true) {
//            try {
//                String userSelection = consoleService.manageExperienceMenu();
//                if (userSelection.equals("1")) {
//                    gainExp();
//                } else if (userSelection.equals("2")) {
//                    spendExp();
//                } else if (userSelection.equals("3")) {
//                    return;
//                } else {
//                    throw new TNTException("Please enter a valid selection");
//                }
//            } catch (TNTException e) {
//                consoleService.printErrorMessage(e);
//            }
//        }
//    }
//
//    private void gainExp() throws TNTException {
//        int gainedExp = consoleService.promptForInt("Enter amount of experience gained: ");
//        int newExpTotal = tntService.gainExp(gainedExp);
//        consoleService.printMessage("Unit has gained " + gainedExp + " experience. Current unspent experience is: " + newExpTotal);
//    }
//
//    private void spendExp() throws TNTException{
//        if (tntService.getCurrentUnit().getUnspentExperience() < tntService.getCurrentUnit().costToAdvance()){
//            throw new TNTException("You do not have enough experience to advance.");
//        }
//        while (true) {
//            try {
//                String userSelection = consoleService.gainAdvanceOptions();
//                if (userSelection.equals("1")) {
//                    throw new TNTException("This functionality is not implemented");
//                } else if (userSelection.equals("2")) {
//                    //TODO start here, easier to do set methods than roll random ones
//                    throw new TNTException("This functionality is not implemented");
//                } else if (userSelection.equals("3")) {
//                    return;
//                } else {
//                    throw new TNTException("Please enter a valid selection");
//                }
//            } catch (TNTException e) {
//                consoleService.printErrorMessage(e);
//            }
//        }
//    }


}
