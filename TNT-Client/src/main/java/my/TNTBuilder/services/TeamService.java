package my.TNTBuilder.services;

import my.TNTBuilder.model.Team;
import org.springframework.web.client.RestTemplate;

public class TeamService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public TeamService(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public Team createTeam(Team team){
        //TODO CREATE TEAM FUNCTION
    }

}
