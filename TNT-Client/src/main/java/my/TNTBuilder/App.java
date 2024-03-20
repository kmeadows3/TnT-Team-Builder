package my.TNTBuilder;

import my.TNTBuilder.model.AuthenticatedUser;
import my.TNTBuilder.model.UserCredentials;
import my.TNTBuilder.services.AuthenticationService;
import my.TNTBuilder.services.ConsoleService;

public class App {

    public static final int MENU_EXIT = 0;
    public static final int LOGIN_MENU_REGISTER = 1;
    public static final int LOGIN_MENU_LOGIN = 2;
    public static final int MAIN_MENU_NEW_WARBAND = 1;
    public static final int MAIN_MENU_LOAD_WARBAD = 2;
    public static final int MAIN_MENU_LOOKUP_RULE = 3;
    

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
//    private final TNTService tntService = new TNTService(API_BASE_URL);
    private AuthenticatedUser currentUser;
    private TNTException generalError = new TNTException("Unknown Error, check log for details.");

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
//            tntService.setCurrentUser(currentUser);
            mainMenu();
        }
        System.out.println("This line in the run method stops the debugger before program exits");
    }
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
    private void handleRegister() {
        consoleService.printMessage("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            consoleService.printMessage("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage(generalError);
        }
    }
    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage(generalError);
        }
    }


    private void mainMenu(){
        int selection = -1;
        while (selection != MENU_EXIT) {
            selection = consoleService.printMainMenu();
            if (selection == MAIN_MENU_NEW_WARBAND) {
                //createTeam();
                consoleService.printMessage("whoo");
            } else if (selection == MAIN_MENU_LOAD_WARBAD) {
                consoleService.printErrorMessage("This functionality is not implemented");
            } else if (selection == MAIN_MENU_LOOKUP_RULE) {
                consoleService.printErrorMessage("This functionality is not implemented");
            } else if (selection != MENU_EXIT){
                consoleService.printErrorMessage("That is not a valid selection");
            }
        }
        consoleService.goodbyeMessage();
    }


//    private void editTeamMenu(){
//        while (true) {
//            consoleService.displayTeam(tntService.getCurrentTeam());
//            try {
//                String userSelection = consoleService.editTeamMenu();
//                if (userSelection.equals("1")){
//                    renameTeam();
//                } else if (userSelection.equals("2")){
//                    editUnit();
//                } else if (userSelection.equals("3")){
//                    createUnit();
//                } else if (userSelection.equals("4")){
//                    manageMoneyMenu();
//                } else if (userSelection.equals("5")){
//                    throw new TNTException("This functionality is not implemented");
//                } else if (userSelection.equals("6")){
//                    // TODO implement save functionality
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
//
//
//
//    private void createTeam() throws TNTException{
//        try {
//            TeamInputHelper teamData = consoleService.initializeNewTeam(tntService.getReference().getTeamOptions());
//            tntService.newTeam(teamData.getName(), teamData.getFaction(), teamData.getMoney());
//            editTeamMenu();
//        } catch (TNTException e) {
//            consoleService.printErrorMessage(e);
//        }
//    }
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
