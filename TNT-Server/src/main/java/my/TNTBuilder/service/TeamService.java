package my.TNTBuilder.service;

import my.TNTBuilder.model.FactionDTO;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.validator.TeamValidator;
import my.TNTBuilder.dao.TeamDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Team;
import org.springframework.stereotype.Component;

import java.util.List;

    @Component
public class TeamService {
    private final TeamDao teamDao;
    private final TeamValidator teamValidator;
    public TeamService (TeamDao teamDao, TeamValidator teamValidator){
        this.teamDao = teamDao;
        this.teamValidator = teamValidator;
    }

    /*
    PUBLIC METHODS
     */
    public Team updateTeam(Team updatedTeam, int userId){
        int teamId = updatedTeam.getId();

        Team currentTeam = null;
        try {
            currentTeam = teamDao.getTeamById(teamId, userId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
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

    public Team spendMoney(int amountToSpend, Team team){

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

    public void updateTeamAfterNewUnitPurchase(int userId, Unit newUnit) {
        Team team = getTeamById(newUnit.getTeamId(), userId);

        int cost = getUnitBaseCostWithDiscounts(team, newUnit);
        team = spendMoney(cost, team);

        if (newUnit.getRank().equalsIgnoreCase("Leader")){
            if (!team.isBoughtFirstLeader()){
                team.setBoughtFirstLeader(true);
                updateTeam(team, userId);
            }
        }
    }


    /*
    METHODS THAT GO TO THE TEAM DAO WITH NO ALTERATIONS
     */
    public Team getTeamById(int teamId, int userId){
        Team team = null;
        try{
            team = teamDao.getTeamById(teamId, userId);

        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
        return team;
    }

    public Team createTeam(Team newTeam){

        try {
            newTeam = teamDao.createTeam(newTeam);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return newTeam;
    }

    public List<FactionDTO> getAllFactions(){
        List<FactionDTO> factionList = null;
        try {
            factionList = teamDao.getAllFactions();
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
        return factionList;
    }

    public List<Team> getAllTeamsForUser(int userId){
        List<Team> allTeams = null;

        try {
            allTeams = teamDao.getAllTeamsForUser(userId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return allTeams;
    }

    public Team getTeamByUnitId(int unitId){
        Team team = null;
        try {
            team = teamDao.getTeamByUnitId(unitId);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
        return team;
    }



    /*
    PRIVATE METHODS
     */

    private int getUnitBaseCostWithDiscounts(Team team, Unit newUnit) {
        int cost = newUnit.getBaseCost();

        if (team.isBoughtFirstLeader() && newUnit.getRank().equalsIgnoreCase("Leader")){
            cost /= 2;
        }

        return cost;
    }
}
