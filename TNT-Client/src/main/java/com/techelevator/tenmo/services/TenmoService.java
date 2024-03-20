package com.techelevator.tenmo.services;

import com.techelevator.tenmo.TenmoException;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class TenmoService {

    private final String BASE_API_URL;
    private AuthenticatedUser currentUser;
    private final RestTemplate restTemplate = new RestTemplate();

    //Constructor
    public TenmoService(String BASE_API_URL) {
        this.BASE_API_URL = BASE_API_URL;
    }

    //SETTERS
    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }


    /**
    METHODS
     */

    //returns user balance
    public double getBalance(){
        String url = BASE_API_URL + "users";
        HttpEntity<Void> entity = getHttpEntity();
        ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.GET, entity, Double.class);
        return response.getBody();
    }

    //private method that creates an HttpEntity with the authorization headers, but a void body
    private HttpEntity<Void> getHttpEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(currentUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);
        return entity;
    }

    //returns a list of accounts that aren't the user's account
    public List<User> getAccounts() {
        String url = BASE_API_URL + "accounts";
        ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, getHttpEntity(), User[].class);
        User[] accountArray = response.getBody();

        return Arrays.asList(accountArray);
    }

    //Make a transfer and send it across the internet
    public boolean createTransfer(double transferAmount, int userInputtedAccount, int transferType) {
        // creates new transfer and sets variables equal to parameters
        Transfer transfer = new Transfer();
        transfer.setTransferAmount(transferAmount);
        if (transferType == 2) {
            transfer.setAccountTo(userInputtedAccount);
        } else if (transferType == 1) {
            transfer.setAccountFrom(userInputtedAccount);
        }
        transfer.setTransferTypeId(transferType);

        // create HTTP entity that holds the headers and the created transfer
        HttpEntity<Transfer> entity = getHttpEntityTransfer(transfer);

        String url = BASE_API_URL + "transfers";
        Transfer returnTransfer = null;

        //sends the transfer to the API, which returns the completed transfer
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.POST, entity, Transfer.class);
            returnTransfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        //return true if a completed transaction was returned
        return returnTransfer != null;
    }

    //private method, creates HTTP entity with headers and a transfer in the body
    private HttpEntity<Transfer> getHttpEntityTransfer(Transfer transfer){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(currentUser.getToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(transfer, httpHeaders);
    }

    // returns a list of transfers that involve the user
    public List<Transfer> displayAllTransfers() throws TenmoException {
        String url = BASE_API_URL + "transfers";
        HttpEntity<Void> httpEntity = getHttpEntity();
        List<Transfer> listOfTransfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Transfer[].class);
            Transfer[] transferArray = response.getBody();
            if (transferArray == null) {
                //throws exception if no transfers are returned, preventing a null pointer exception as we try to use the array
                throw new TenmoException("You have no existing transfers.");
            }
            listOfTransfers = Arrays.asList(transferArray);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return listOfTransfers;
    }

    //returns a single transfer, identified by it's ID
    public Transfer getTransferById(int id) {
        String url = BASE_API_URL + "transfers/" + id;
        HttpEntity<Void> entity = getHttpEntity();
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    //returning pending requests involving user
    public List<Transfer> displayRequests() throws TenmoException {
        String url = BASE_API_URL + "transfers?transferType=1";
        HttpEntity<Void> httpEntity = getHttpEntity();
        List<Transfer> listOfRequests = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Transfer[].class);
            Transfer[] transferArray = response.getBody();
            if (transferArray == null) {
                //throws exception if no requests are returned, preventing a null pointer exception as we try to use the array
                throw new TenmoException("You have no pending transfers.");
            }
            listOfRequests = Arrays.asList(transferArray);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return listOfRequests;
    }

    //updating transfer status
    public Transfer updateTransferStatus(Transfer transfer) throws TenmoException {
        String url = BASE_API_URL + "transfers/" + transfer.getTransferId();
        Transfer updatedTransfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.PUT, getHttpEntityTransfer(transfer), Transfer.class);
            updatedTransfer = response.getBody();
            if (updatedTransfer == null) {
                throw new TenmoException("Unable to update transfer.");
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            throw new TenmoException("Unable to update transfer.");
        }
        return updatedTransfer;
    }





}
