package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoService;

import java.util.List;

public class App {

    public static final int MENU_EXIT = 0;
    public static final int LOGIN_MENU_REGISTER = 1;
    public static final int LOGIN_MENU_LOGIN = 2;
    public static final int MAIN_MENU_VIEW_BALANCE = 1;
    public static final int MAIN_MENU_VIEW_TRANSFER_HISTORY = 2;
    public static final int MAIN_MENU_VIEW_PENDING_REQUESTS = 3;
    public static final int MAIN_MENU_SEND_TE_BUCKS = 4;
    public static final int MAIN_MENU_REQUEST_TE_BUCKS = 5;

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TenmoService tenmoService = new TenmoService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            tenmoService.setCurrentUser(currentUser);
            mainMenu();
        }
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
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != MENU_EXIT) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == MAIN_MENU_VIEW_BALANCE) {
                viewCurrentBalance();
            } else if (menuSelection == MAIN_MENU_VIEW_TRANSFER_HISTORY) {
                try {
                    viewTransferHistory();
                } catch (TenmoException e) {
                    consoleService.printMessage("You have no transfers.");
                }
            } else if (menuSelection == MAIN_MENU_VIEW_PENDING_REQUESTS) {
                try {
                    viewPendingRequests();
                } catch (TenmoException e) {
                    consoleService.printMessage("You have no transfers.");
                }
            } else if (menuSelection == MAIN_MENU_SEND_TE_BUCKS) {
                sendBucks();
            } else if (menuSelection == MAIN_MENU_REQUEST_TE_BUCKS) {
                requestBucks();
            } else if (menuSelection == MENU_EXIT) {
                continue;
            } else {
                consoleService.printMessage("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		double balance = tenmoService.getBalance();
        consoleService.displayBalance(balance);
	}

	private void viewTransferHistory() throws TenmoException {
        int userSelection = consoleService.displaysListOfTransfers(tenmoService.displayAllTransfers(), currentUser.getUser().getUsername());
        Transfer transfer = tenmoService.getTransferById(userSelection);
        if (userSelection == 0){
            return;
        } else if (transfer == null){
            consoleService.printMessage("Not a valid transfer selection");
        } else {
            consoleService.displayTransferDetails(transfer);
        }
	}

	private void viewPendingRequests() throws TenmoException {
		int userSelection = consoleService.displayPendingRequests(tenmoService.displayRequests());
        Transfer transfer = tenmoService.getTransferById(userSelection);
        if (userSelection == 0) {
            return;
        } else if (transfer == null){
            consoleService.printMessage("Not a valid transfer selection");
        } else {
            consoleService.displayTransferDetails(transfer);
        }
        userSelection = consoleService.approveOrRejectTransfer();
        if (userSelection == 0) {
            return;
        } else if (userSelection == 1) {
            transfer.setTransferStatusId(2);
        } else if (userSelection ==2) {
            transfer.setTransferStatusId(3);
        } else {
            consoleService.printMessage("Not a valid selection, please select an option from the list above.");
            return;
        }
        try {
            Transfer returnTransfer = tenmoService.updateTransferStatus(transfer);
            consoleService.printMessage("Update Complete.");
        } catch (TenmoException e) {
            consoleService.printErrorMessage();
        }
	}

	private void sendBucks() {
        List<User> account = tenmoService.getAccounts();
        int recipientAccount = consoleService.getRecipientAccountId(account);
        if (recipientAccount == 0) {
            return;
        }
        double amountToSend = consoleService.promptForDouble("Enter amount: ");
        boolean transferComplete = tenmoService.createTransfer(amountToSend, recipientAccount, 2);
        if (!transferComplete) {
            consoleService.printErrorMessage();
        } else {
            consoleService.printMessage("Transfer Complete.");
        }
	}

	private void requestBucks() {
		int userSelection = consoleService.selectAccountToRequestMoney(tenmoService.getAccounts());
        if (userSelection == 0) {
            return;
        }
        double amountToSend = consoleService.promptForDouble("Enter amount: ");
        boolean transferComplete = tenmoService.createTransfer(amountToSend, userSelection, 1);
        if (!transferComplete) {
            consoleService.printErrorMessage();
        } else {
            consoleService.printMessage("Transfer Requested.");
        }
	}

}
