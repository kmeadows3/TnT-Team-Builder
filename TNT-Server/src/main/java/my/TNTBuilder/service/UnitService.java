package my.TNTBuilder.service;

import my.TNTBuilder.UnitValidator;
import my.TNTBuilder.dao.TeamDao;
import my.TNTBuilder.dao.UnitDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnitService {
    private final int FREELANCER_FACTION_ID = 7;
    private UnitDao unitDao;
    private TeamDao teamDao;
    private UnitValidator unitValidator;

    public UnitService(UnitDao unitDao, TeamDao teamDao, UnitValidator unitValidator) {
        this.unitDao = unitDao;
        this.teamDao = teamDao;
        this.unitValidator = unitValidator;
    }

    public Unit createNewUnit(Unit clientUnit, int UserId){
        Unit newUnit = null;
        try {
            validateNewClientUnit(clientUnit, UserId);
            newUnit = unitDao.createUnit(clientUnit);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }
        return newUnit;
    }

    public List<Unit> getUnitsForFaction(int factionId){
        List<Unit> units = null;
        try {
            units = unitDao.getListOfUnitsByFactionId(factionId);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }
        return units;
    }


    //TODO this method needs testing!
    public Unit updateUnit(Unit clientUnit, int userId){
        try {
            validateUpdatedUnit(clientUnit, userId);
            unitDao.updateUnit(clientUnit);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
        return unitDao.getUnitById(clientUnit.getId(), userId);
    }

    /*
        PRIVATE METHODS
     */
    private void validateNewClientUnit(Unit unit, int userId) {
        Team team = teamDao.getTeamById(unit.getTeamId(), userId);
        if (team == null) {
            throw new ServiceException("Invalid Unit. Logged in user does not own team.");
        }

        int unitFaction = unitDao.getFactionIdByUnitId(unit.getId());
        if (unitFaction != team.getFactionId() && unitFaction != FREELANCER_FACTION_ID) {
            throw new ServiceException("Invalid unit. Unit does not belong to same faction as team.");
        }
    }


    private void validateUpdatedUnit(Unit updatedUnit, int userId){
        Unit currentUnit = unitDao.getUnitById(updatedUnit.getId(), userId);
        if (currentUnit == null){
            throw new ServiceException("Update failed. User does not own unit.");
        }
        if ( unitValidator.onlyNameChanged(currentUnit, updatedUnit)){
            return;
        } else if (unitValidator.onlyUnspentExpGained(currentUnit, updatedUnit)) {
            return;
        } else if (unitValidator.validFivePointLevel(currentUnit, updatedUnit)
                && unitValidator.unitCanAffordAdvance(currentUnit)){
            spentExpForAdvance(updatedUnit);
        } else if (unitValidator.validTenPointLevel(currentUnit, updatedUnit)
                && unitValidator.unitCanAffordAdvance(currentUnit)){
            spentExpForAdvance(updatedUnit);
            updatedUnit.setTenPointAdvances(updatedUnit.getTenPointAdvances() + 1);
        } else {
            throw new ServiceException("Unit update is not valid");
        }
    }

    private void spentExpForAdvance(Unit updatedUnit) {
        updatedUnit.setUnspentExperience(updatedUnit.getUnspentExperience() - updatedUnit.getCostToAdvance());
        updatedUnit.setTotalAdvances(updatedUnit.getTotalAdvances() + 1);
    }


}
