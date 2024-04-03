package my.TNTBuilder.service;

import my.TNTBuilder.validator.TeamValidator;
import my.TNTBuilder.dao.TeamDao;
import my.TNTBuilder.dao.UserDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamService {
    private TeamDao teamDao;
    private UserDao userDao;
    private TeamValidator teamValidator;
    public TeamService (TeamDao teamDao, UserDao userDao, TeamValidator teamValidator){
        this.teamDao = teamDao;
        this.userDao = userDao;
        this.teamValidator = teamValidator;
    }

    public Team updateTeam(Team updatedTeam, String username){
        int userId = userDao.getUserIdByUsername(username);
        int teamId = updatedTeam.getId();
        Team currentTeam = teamDao.getTeamById(teamId, userId);
        if (currentTeam == null){
            throw new ServiceException("Unable to update team. Either team does not exist, or does not belong to user.");
        }
        if (teamValidator.validMoneyChange(currentTeam, updatedTeam) || teamValidator.validNameChange(currentTeam, updatedTeam)){
            try {
                teamDao.updateTeam(updatedTeam);
            } catch (DaoException e){
                throw new ServiceException(e.getMessage(), e);
            }
        } else {
            throw new ServiceException("Invalid update to Team");
        }
        return teamDao.getTeamById(teamId, userId);
    }

    public void spendMoney(int amountToSpend, int teamId, int userId){
        try{
            Team team = teamDao.getTeamById(teamId, userId);
            if (team.getMoney() - amountToSpend < 0){
                throw new ServiceException("Team does not have enough money for this action.");
            }
            team.setMoney(team.getMoney() - amountToSpend);
            teamDao.updateTeam(team);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
