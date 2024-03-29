package my.TNTBuilder.controller;

import my.TNTBuilder.dao.TeamDao;
import my.TNTBuilder.dao.UserDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.FactionDTO;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.service.UnitService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TeamController {
    private final TeamDao teamDao;
    private final UserDao userDao;
    private final UnitService unitService;

    public TeamController(TeamDao teamDao, UserDao userDao, UnitService unitService){
        this.teamDao = teamDao;
        this.userDao = userDao;
        this.unitService = unitService;
    }

    @RequestMapping(path = "/team", method = RequestMethod.POST)
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

    @RequestMapping(path = "/team", method = RequestMethod.GET)
    public List<Team> getUsersTeams(Principal principal){
        List<Team> teamList = null;
        try{
            teamList = teamDao.getAllTeamsForUser(userDao.getUserIdByUsername(principal.getName()));
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }

        return teamList;
    }

    @RequestMapping(path = "/team/{id}", method = RequestMethod.GET)
    public Team createTeam(@PathVariable int id, Principal principal){
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

    @RequestMapping(path = "/faction", method = RequestMethod.GET)
    public List<FactionDTO> lookupAllFactions(){
        try {
            return teamDao.getAllFactions();
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(path = "/faction/{id}", method = RequestMethod.GET)
    public List<Unit> getUnitsForFaction(@PathVariable int id){
        try {
            return unitService.getUnitsForFaction(id);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
