package my.TNTBuilder.services;

import my.TNTBuilder.TNTException;
import my.TNTBuilder.model.Faction;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.userModels.AuthenticatedUser;
import my.TNTBuilder.model.Team;
import my.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class TeamService {

    private final String BASE_API_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;


    public TeamService(String baseUrl){
        this.BASE_API_URL = baseUrl;
    }

    public Team createTeam(Team teamInput) throws TNTException{
        String url = BASE_API_URL + "/teams";
        Team newTeam = null;
        HttpEntity<Team> entity = getTeamHttpEntity(teamInput);
        try {
            newTeam = restTemplate.postForObject(url, entity, Team.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            throw new TNTException("Unable to create team");
        }

        return newTeam;
    }

    public List<Team> getTeamsForUser() throws TNTException{
        List<Team> teamsForUser = null;
        String url = BASE_API_URL + "/teams";

        try {
            ResponseEntity<Team[]> response = restTemplate.exchange(url, HttpMethod.GET, getVoidHttpEntity(), Team[].class);
            Team[] teamArray = response.getBody();
            if (teamArray == null){
                throw new TNTException("No teams returned");
            }
            teamsForUser = Arrays.asList(teamArray);
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
            throw new TNTException("Unable to access resource: " + e.getMessage());
        }
        return teamsForUser;
    }
    public List<Faction> getFactions() throws TNTException{
        List<Faction> factionlist = null;
        String url = BASE_API_URL + "/factions";
        HttpEntity<Void> entity = getVoidHttpEntity();

        try {
            ResponseEntity<Faction[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Faction[].class);
            Faction[] factionArr = response.getBody();
            if (factionArr == null){
                throw new TNTException("Faction list returned null");
            } else {
                factionlist = Arrays.asList(factionArr);
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            throw new TNTException("Unable to access server");
        }

        return factionlist;
    }

    public List<Unit> getUnitsForPurchase(int factionId) throws TNTException{
        List<Unit> unitsForPurchase = null;
        String url = BASE_API_URL + "factions/" + factionId;
        try {
            ResponseEntity<Unit[]> response = restTemplate.exchange(url, HttpMethod.GET, getVoidHttpEntity(), Unit[].class);
            Unit[] units = response.getBody();
            if (units == null){
                throw new TNTException("No valid units for this team");
            }
            unitsForPurchase = Arrays.asList(units);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            throw new TNTException("Unable to access server");
        }

        return unitsForPurchase;
    }







    /*
    Private methods
     */

    private HttpEntity<Team> getTeamHttpEntity(Team teamInput) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(teamInput, httpHeaders);
    }
    private HttpEntity<Void> getVoidHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return entity;
    }

    /*
    Getters and setters
     */

    public AuthenticatedUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(AuthenticatedUser user) {
        this.currentUser = user;
    }
}
