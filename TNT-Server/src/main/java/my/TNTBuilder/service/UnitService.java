package my.TNTBuilder.service;

import my.TNTBuilder.model.Skill;
import my.TNTBuilder.validator.UnitValidator;
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
    private TeamService teamService;

    public UnitService(UnitDao unitDao, TeamDao teamDao, UnitValidator unitValidator, TeamService teamService) {
        this.unitDao = unitDao;
        this.teamDao = teamDao;
        this.unitValidator = unitValidator;
        this.teamService = teamService;
    }

    public Unit createNewUnit(Unit clientUnit, int userId){
        Unit newUnit = null;
        try {
            validateNewClientUnit(clientUnit, userId);
            newUnit = unitDao.createUnit(clientUnit);
            teamService.spendMoney(newUnit.getBaseCost(), newUnit.getTeamId(), userId);

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

    public List<Skill> getPotentialSkills(int unitId){
        List<Skill> skillList = null;
        try {
            skillList = unitDao.getPotentialSkills(unitId);
            if (skillList == null){
                throw new ServiceException("No valid skills");
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return skillList;
    }

    public void addSkillToUnit(int skillId, int unitId, int userId){
        try {
            if (unitCanAddSkill(unitId, skillId, userId)){
                unitDao.addSkillToUnit(skillId, unitId);
                Unit unit = unitDao.getUnitById(unitId, userId);
                unit.setEmptySkills(unit.getEmptySkills() - 1);
                unitDao.updateUnit(unit);
            }
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }

    //TODO test me
    public Unit getUnitById(int unitId, int userId){
        Unit unit = null;
        try {
            unit = unitDao.getUnitById(unitId, userId);
            if (unit == null){
                throw new ServiceException("Unable to retrieve unit");
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return unit;
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
        int cost = unitDao.convertReferenceUnitToUnit(unit.getId()).getBaseCost();
        if (team.getMoney() - cost < 0){
            throw new ServiceException("Team cannot afford this unit");
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

    private boolean unitCanAddSkill(int unitId, int skillId, int userId){
        boolean valid = false;

        Unit unit = unitDao.getUnitById(unitId, userId);
        if (unit == null){
            throw new ServiceException("Error, invalid unit.");
        }

        if (unit.getEmptySkills() < 1){
            throw new ServiceException("Error, no open skills.");
        }

        List<Skill> potentialSkills = unitDao.getPotentialSkills(unit.getId());
        for (Skill skill: potentialSkills){
            if(skill.getId() == skillId){
                return true;
            }
        }

        throw new ServiceException("Error, unit cannot have this skill.");
    }



}
