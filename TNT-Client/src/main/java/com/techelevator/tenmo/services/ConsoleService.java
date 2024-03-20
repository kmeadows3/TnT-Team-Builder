package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

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
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
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

    public double promptForDouble(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    //Use case 3
    public void displayBalance(double balance) {
        System.out.printf("Your current account balance is : $%5.2f", balance);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public int getRecipientAccountId(List<User> accountList){
        printListOfOtherUsers(accountList);
        return promptForInt("Enter ID of user you are sending to (0 to cancel): ");
    }

    private void horizontalLongLine() {
        System.out.println("-------------------------------------------");
    }
    private void horizontalShortLine() {
        System.out.println("---------");
    }

    //Use case 6
    public void displayTransferDetails(Transfer transfer) {
        horizontalLongLine();
        System.out.println("Transfer Details");
        horizontalLongLine();
        System.out.println(" Id: " + transfer.getTransferId());
        System.out.println(" From: " + transfer.getAccountFromUsername());
        System.out.println(" To: " + transfer.getAccountToUsername());
        if (transfer.getTransferTypeId() == 1) {
            System.out.println(" Type: Send");
        } else {
            System.out.println(" Type: Request");
        }
        String status = "Rejected";
        if (transfer.getTransferStatusId() == 1) {
            status = "Pending";
        } else if (transfer.getTransferStatusId() == 2) {
            status = "Approved";
        }
        System.out.println(" Status: " + status);
        System.out.printf(" %-8s$%4.2f", "Amount: ", transfer.getTransferAmount());
        System.out.println();
    }

    //Use case 7
    public int selectAccountToRequestMoney(List<User> accountList){
        printListOfOtherUsers(accountList);
        return promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
    }

    //Use case 4
    private void printListOfOtherUsers(List<User> accountList) {
        horizontalLongLine();
        System.out.println("Users");
        System.out.printf("%-12s%-33s\n", "ID", "Name");
        horizontalLongLine();
        for (User account : accountList) {
            System.out.printf("%-12d%-33s\n", account.getId(), account.getUsername());
        }
        horizontalShortLine();
        System.out.println();
    }

    //Use case 5
    public int displaysListOfTransfers(List<Transfer> transfers, String username) {
        horizontalLongLine();
        System.out.println("Transfers");
        System.out.printf("%-12s%-18s%13s\n", "ID", "From/To", "Amount");
        horizontalLongLine();
        for (Transfer transfer : transfers) {
            String transferType = "From: ";
            String targetUsername = transfer.getAccountFromUsername();
            if (transfer.getTransferTypeId() == 2 && transfer.getAccountFromUsername().equals(username)) {
                transferType = "To: ";
                targetUsername = transfer.getAccountToUsername();
            }
            System.out.printf("%-12d%-6s%-18s$%4.2f\n", transfer.getTransferId(), transferType, targetUsername, transfer.getTransferAmount());
        }
        horizontalShortLine();
        return promptForInt("Please enter transfer ID to view details (0 to cancel): ");
    }

    //Use case 8
    public int displayPendingRequests(List<Transfer> requests){
        horizontalLongLine();
        System.out.println("Pending Transfers");
        System.out.printf("%-12s%-18s%13s\n", "ID", "To", "Amount");
        horizontalLongLine();
        for (Transfer transfer : requests) {
            System.out.printf("%-12d%-24s$%4.2f\n", transfer.getTransferId(), transfer.getAccountToUsername(), transfer.getTransferAmount());
        }
        horizontalShortLine();
        return promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
    }

    //Use case 9
    public int approveOrRejectTransfer() {
        System.out.println();
        System.out.println("1: Approve ");
        System.out.println("2: Reject ");
        System.out.println("0: Don't approve or reject ");
        horizontalShortLine();
        return promptForInt("Please choose an option: ");
    }

}
