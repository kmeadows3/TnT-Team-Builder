package my.TNTBuilder.services;

import my.TNTBuilder.TNTException;
import my.TNTBuilder.model.AuthenticatedUser;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.dto.TeamInputDTO;
import my.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TeamService {

    private final String BASE_API_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;


    public TeamService(String baseUrl){
        this.BASE_API_URL = baseUrl;
    }

    public Team createTeam(TeamInputDTO teamInput) throws TNTException{
        String url = BASE_API_URL+"/team";
        Team newTeam = null;
        HttpEntity<TeamInputDTO> entity = getHttpTeamInputDTOEntity(teamInput);
        try {
            newTeam = restTemplate.postForObject(url, entity, Team.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            throw new TNTException("Unable to create team");
        }

        return newTeam;
    }




    /*
    Private methods
     */

    private HttpEntity<TeamInputDTO> getHttpTeamInputDTOEntity(TeamInputDTO teamInput) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(teamInput, httpHeaders);
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
