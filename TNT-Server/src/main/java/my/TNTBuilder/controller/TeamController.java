package my.TNTBuilder.controller;

import my.TNTBuilder.dao.TeamDao;
import my.TNTBuilder.dao.UserDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.Team;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class TeamController {
    private final TeamDao teamDao;
    private final UserDao userDao;

    public TeamController(TeamDao teamDao, UserDao userDao){
        this.teamDao = teamDao;
        this.userDao = userDao;
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
}
