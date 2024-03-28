package my.TNTBuilder.service;

import my.TNTBuilder.dao.TeamDao;
import my.TNTBuilder.dao.UnitDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import org.springframework.stereotype.Component;

@Component
public class UnitService {
    private final int FREELANCER_FACTION_ID = 7;
    private UnitDao unitDao;
    private TeamDao teamDao;


    public UnitService(UnitDao unitDao, TeamDao teamDao) {
        this.unitDao = unitDao;
        this.teamDao = teamDao;
    }

    public Unit createNewUnit(Unit clientUnit, int UserId){
        Unit newUnit = null;
        try {
            validateClientUnit(clientUnit, UserId);
            newUnit = unitDao.createUnit(clientUnit);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }
        return newUnit;
    }

    private void validateClientUnit(Unit unit, int userId) {
        Team team = teamDao.getTeamById(unit.getTeamId(), userId);
        if (team == null) {
            throw new ServiceException("Invalid Unit. Logged in user does not own team.");
        }

        int unitFaction = unitDao.getFactionIdByUnitId(unit.getId());
        if (unitFaction != team.getFactionId() && unitFaction != FREELANCER_FACTION_ID) {
            throw new ServiceException("Invalid unit. Unit does not belong to same faction as team.");
        }
    }






}
