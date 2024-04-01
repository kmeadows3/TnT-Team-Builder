package my.TNTBuilder.services;

import my.TNTBuilder.TNTException;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.userModels.AuthenticatedUser;
import my.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;


public class UnitService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_API_URL;
    private AuthenticatedUser currentUser;

    public UnitService(String baseUrl){
        this.BASE_API_URL = baseUrl;
    }

    public Unit addNewUnitToDatabase(Unit newUnit) throws TNTException{
        String url = BASE_API_URL +"/unit";
        Unit returnedUnit = null;
        try {
            ResponseEntity<Unit> response = restTemplate.exchange(url, HttpMethod.POST, generateUnitHttpEntity(newUnit), Unit.class);
            returnedUnit = response.getBody();
            if (returnedUnit == null){
                throw new TNTException("No unit returned");
            }
        } catch (ResponseStatusException e) {
            BasicLogger.log(e.getMessage());
            throw new TNTException(e.getMessage(), e);
        }

        return returnedUnit;
    }





    /*
    Private Methods
     */

    public HttpEntity<Unit> generateUnitHttpEntity(Unit unit){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Unit> entity = new HttpEntity<>(unit, headers);
        return entity;
    }


    public HttpEntity<Void> generateVoidHttpEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return entity;
    }














    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }
}
