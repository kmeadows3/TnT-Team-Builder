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
import java.util.stream.Collectors;

@Component
public class UnitService {
    public static final double MAX_SPECIALIST_RATIO = .34;
    private final int FREELANCER_FACTION_ID = 7;
    private final UnitDao unitDao;
    private final TeamDao teamDao;
    private final UnitValidator unitValidator;
    private final TeamService teamService;

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

    //TODO: TEST ME MORE TOO
    public List<Unit> getUnitsForFaction(int factionId, Team team){
        List<Unit> units = null;
        try {
            units = unitDao.getListOfUnitsByFactionId(factionId);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }

        units = filterOutInvalidUnits(team, units);

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

    public Unit getReferenceUnitByClass(String unitClass){
        Unit unit = null;
        try {
            unit = unitDao.convertReferenceUnitToUnit(unitClass);
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

        Unit potentialUnit = unitDao.convertReferenceUnitToUnit(unit.getId());
        if (team.getMoney() - potentialUnit.getBaseCost() < 0){
            throw new ServiceException("Team cannot afford this unit");
        }

        int unitFaction = unitDao.getFactionIdByUnitId(unit.getId());
        if (unitFaction != team.getFactionId() && unitFaction != FREELANCER_FACTION_ID) {
            throw new ServiceException("Invalid unit. Unit does not belong to same faction as team.");
        }

        confirmNewUnitRankIsValidOption(potentialUnit, team);

    }

    private void confirmNewUnitRankIsValidOption(Unit potentialUnit, Team team) {
        if ( potentialUnit.getRank().equalsIgnoreCase("Leader") && !teamMustBuyLeader (team)){
            throw new ServiceException("Team cannot have two leaders.");
        } else if (potentialUnit.getRank().equalsIgnoreCase("Elite") && teamCanNotBuyElite(team)) {
            throw new ServiceException("Team cannot take more than 3 elites.");
        } else if (potentialUnit.getRank().equalsIgnoreCase("Specialist") && teamCanNotBuySpecialist(team)) {
            throw new ServiceException("Specialists may not exceed more than 1/3rd of the team");
        } else if (teamMustBuyLeader(team) && !potentialUnit.getRank().equalsIgnoreCase("Leader")){
            throw new ServiceException("Team cannot purchase units until it has a leader");
        }
    }
    private boolean teamMustBuyLeader(Team team){
        return team.getUnitList().stream().noneMatch(teamUnit -> "Leader".equalsIgnoreCase(teamUnit.getRank()));
    }

    private boolean teamCanNotBuyElite(Team team){
        return team.getUnitList().stream()
                .filter(teamUnit -> "Elite".equalsIgnoreCase(teamUnit.getRank()))
                .count() >= 3;
    }

    private boolean teamCanNotBuySpecialist(Team team){
        int unitCount = team.getUnitList().size();
        int specialistCount = (int) team.getUnitList().stream()
                .filter( teamUnit -> "Specialist".equalsIgnoreCase( teamUnit.getRank()))
                .count();

        return !(((double) (specialistCount + 1) / unitCount) <= MAX_SPECIALIST_RATIO);
    }

    private List<Unit> filterOutInvalidUnits(Team team, List<Unit> units) {
        if ( teamMustBuyLeader(team) ) {
            units = units.stream()
                    .filter(unit -> "Leader".equalsIgnoreCase(unit.getRank()))
                    .collect(Collectors.toList());
        } else {
            units = filterOutRank(units, "Leader");

            if (teamCanNotBuyElite(team)){
                units = filterOutRank(units, "Elite");
            }
            if (teamCanNotBuySpecialist(team)){
                units = filterOutRank(units, "Specialist");
            }
        }
        return units;
    }

    private List<Unit> filterOutRank(List<Unit> units, String filteredOutRank) {
        units = units.stream()
                .filter( unit -> !filteredOutRank.equalsIgnoreCase(unit.getRank() ) )
                .collect(Collectors.toList());
        return units;
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
            spendExpForAdvance(updatedUnit);
        } else if (unitValidator.validTenPointLevel(currentUnit, updatedUnit)
                && unitValidator.unitCanAffordAdvance(currentUnit)){
            spendExpForAdvance(updatedUnit);
            updatedUnit.setTenPointAdvances(updatedUnit.getTenPointAdvances() + 1);
        } else {
            throw new ServiceException("Unit update is not valid");
        }
    }

    private void spendExpForAdvance(Unit updatedUnit) {
        updatedUnit.setUnspentExperience(updatedUnit.getUnspentExperience() - updatedUnit.getCostToAdvance());
        updatedUnit.setSpentExperience(updatedUnit.getSpentExperience() + updatedUnit.getCostToAdvance());
        updatedUnit.setTotalAdvances(updatedUnit.getTotalAdvances() + 1);
    }

    private boolean unitCanAddSkill(int unitId, int skillId, int userId){

        Unit unit = unitDao.getUnitById(unitId, userId);
        if (unit == null){
            throw new ServiceException("Error, invalid unit.");
        } else if (unit.getEmptySkills() < 1){
            throw new ServiceException("Error, no open skills.");
        }

        for (Skill skill : unit.getSkills()){
            if (skill.getId() == skillId){
                throw new ServiceException("Error, unit already has this skill");
            }
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
