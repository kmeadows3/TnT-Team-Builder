package my.TNTBuilder.controller;

import my.TNTBuilder.dao.TeamDao;
import my.TNTBuilder.dao.UserDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.FactionDTO;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.service.TeamService;
import my.TNTBuilder.service.UnitService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
@CrossOrigin
@RestController
@PreAuthorize("isAuthenticated()")
public class TeamController {
    private final TeamDao teamDao;
    private final UserDao userDao;
    private final UnitService unitService;
    private final TeamService teamService;

    public TeamController(TeamDao teamDao, UserDao userDao, UnitService unitService, TeamService teamService){
        this.teamDao = teamDao;
        this.userDao = userDao;
        this.unitService = unitService;
        this.teamService = teamService;
    }

    /**
     * Creates a new team in the database
     * @param team the team sent by the client, only with basic data to instantiate the team
     * @param principal the logged-in user
     * @return the fully created team from the database
     */
    @RequestMapping(path = "/teams", method = RequestMethod.POST)
    public Team createTeam(@Valid @RequestBody Team team, Principal principal){
        team.setUserId(userDao.getUserIdByUsername(principal.getName()));
        Team returnTeam = null;
        try{
            returnTeam = teamDao.createTeam(team);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return returnTeam;
    }

    /**
     * Returns a list of teams belonging to the user
     * @param principal the logged-in user
     * @return A list of teams belonging to the user
     */
    @RequestMapping(path = "/teams", method = RequestMethod.GET)
    public List<Team> getUsersTeams(Principal principal){
        List<Team> teamList = null;
        try{
            teamList = teamDao.getAllTeamsForUser(userDao.getUserIdByUsername(principal.getName()));
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }

        return teamList;
    }

    /**
     * Validates and updates a specific team
     * @param updatedTeam the team from the client
     * @param principal the logged-in user
     * @return the team after the update is made
     */
    @RequestMapping(path = "/teams/{id}", method = RequestMethod.PUT)
    public Team updateTeam(@Valid @RequestBody Team updatedTeam, Principal principal){
        Team teamAfterUpdate = null;
        try{
            teamAfterUpdate = teamService.updateTeam(updatedTeam, userDao.getUserIdByUsername(principal.getName()));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return teamAfterUpdate;
    }

    /**
     * Gets a specific team belonging to the user based on the ID
     * @param id the id of the team
     * @param principal the logged-in user
     * @return the team belonging to the user
     */
    @RequestMapping(path = "/teams/{id}", method = RequestMethod.GET)
    public Team getTeam(@PathVariable int id, Principal principal){
        Team returnTeam = null;
        try{
            returnTeam = teamDao.getTeamById(id, userDao.getUserIdByUsername(principal.getName()));
            if(returnTeam == null){
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to find selected team belonging to logged in user");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return returnTeam;
    }

    /**
     * Returns a list of all possible factions in the database
     * @return the list of factions
     */
    @RequestMapping(path = "/factions", method = RequestMethod.GET)
    public List<FactionDTO> lookupAllFactions(){
        try {
            return teamDao.getAllFactions();
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * returns a list of valid units for a faction that a specific team can buy
     * @param id the id of the faction
     * @param team the team the new units would belong to
     * @return the list of valid units for that team
     */
    @RequestMapping(path = "/factions/{id}", method = RequestMethod.POST)
    public List<Unit> getUnitsForFaction(@PathVariable int id, @RequestBody Team team) {
        try {
            return unitService.getUnitsForFaction(id, team);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
