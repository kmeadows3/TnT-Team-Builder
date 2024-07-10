package my.TNTBuilder.service;

import my.TNTBuilder.dao.*;
import my.TNTBuilder.model.FactionDTO;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import my.TNTBuilder.validator.TeamValidator;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.validator.UnitValidator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

    @Component
public class TeamService {
    private final TeamDao teamDao;
    private final TeamValidator teamValidator;
    private final JdbcTemplate jdbcTemplate;
    public TeamService (TeamDao teamDao, TeamValidator teamValidator, JdbcTemplate jdbcTemplate){
        this.teamDao = teamDao;
        this.teamValidator = teamValidator;
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    PUBLIC METHODS
     */
    public Team updateTeam(Team updatedTeam, int userId) throws ServiceException{
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
                updatedTeam = teamDao.getTeamById(teamId, userId);
            } catch (DaoException e){
                throw new ServiceException(e.getMessage(), e);
            }
        } else {
            throw new ServiceException("Invalid update to Team");
        }

        return updatedTeam;
    }

    public Team spendMoney(int amountToSpend, Team team) throws ServiceException{

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

    public void updateTeamAfterNewUnitPurchase(int userId, Unit newUnit)  throws ServiceException {
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
    public Team getTeamById(int teamId, int userId)  throws ServiceException {
        Team team = null;
        try{
            team = teamDao.getTeamById(teamId, userId);

        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
        return team;
    }

    public Team createTeam(Team newTeam)  throws ServiceException{

        try {
            newTeam = teamDao.createTeam(newTeam);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return newTeam;
    }

    public List<FactionDTO> getAllFactions()  throws ServiceException {
        List<FactionDTO> factionList = null;
        try {
            factionList = teamDao.getAllFactions();
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
        return factionList;
    }

    public List<Team> getAllTeamsForUser(int userId)  throws ServiceException {
        List<Team> allTeams = null;

        try {
            allTeams = teamDao.getAllTeamsForUser(userId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return allTeams;
    }

    public Team getTeamByUnitId(int unitId) throws ServiceException{
        Team team = null;
        try {
            team = teamDao.getTeamByUnitId(unitId);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
        return team;
    }

    public void deleteTeam(int teamId, int userId) throws ServiceException{
        Team team = getTeamById(teamId, userId);
        UnitDao unitDao = new JdbcUnitDao(jdbcTemplate);
        ItemDao itemDao = new JdbcItemDao(jdbcTemplate);
        UnitService unitService = new UnitService(unitDao, itemDao, new UnitValidator(unitDao), this);
        try {
            for(Unit unit : team.getUnitList()){
                unitService.deleteUnit(unit.getId(), userId, true);
            }
            for (Item item : team.getInventory()){
                itemDao.deleteItem(item.getId());
            }

            teamDao.deleteTeam(team.getId());
        } catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }
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
