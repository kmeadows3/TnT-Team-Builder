package my.TNTBuilder.service;

import my.TNTBuilder.model.Unit;
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

    public Team updateTeam(Team updatedTeam, int userId){
        int teamId = updatedTeam.getId();

        Team currentTeam = teamDao.getTeamById(teamId, userId);
        if (currentTeam == null){
            throw new ServiceException("Unable to update team. Either team does not exist, or does not belong to user.");
        }

        if (teamValidator.validMoneyChange(currentTeam, updatedTeam)
                || teamValidator.validNameChange(currentTeam, updatedTeam)
                || teamValidator.validFirstLeaderChange(currentTeam, updatedTeam)){
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

    //TODO test me
    public Team getTeamById(int teamId, int userId){
        Team team = null;
        try{
            team = teamDao.getTeamById(teamId, userId);
            if (team == null){
                throw new ServiceException("No team returned");
            }
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
        return team;
    }

    //TODO more tests
    public void updateTeamAfterNewUnitPurchase(int userId, Unit newUnit) {
        Team team = getTeamById(newUnit.getTeamId(), userId);

        int cost = getUnitBaseCostWithDiscounts(team, newUnit);
        team = spendMoney(cost, newUnit.getTeamId(), userId);

        if (newUnit.getRank().equalsIgnoreCase("Leader")){
            if (!team.isBoughtFirstLeader()){
                team.setBoughtFirstLeader(true);
                updateTeam(team, userId);
            }
        }
    }


    /*
    PRIVATE METHODS
     */

    private Team spendMoney(int amountToSpend, int teamId, int userId){
        Team team = getTeamById(teamId, userId);

        if (team.getMoney() - amountToSpend < 0){
            throw new ServiceException("Team does not have enough money for this action.");
        }

        team.setMoney(team.getMoney() - amountToSpend);

        try{
            teamDao.updateTeam(team);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }

        return team;
    }

    private int getUnitBaseCostWithDiscounts(Team team, Unit newUnit) {
        int cost = newUnit.getBaseCost();

        if (team.isBoughtFirstLeader() && newUnit.getRank().equalsIgnoreCase("Leader")){
            cost /= 2;
        }

        return cost;
    }
}
