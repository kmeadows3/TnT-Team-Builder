package my.TNTBuilder.service;

import my.TNTBuilder.dao.ItemDao;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.inventory.Item;
import my.TNTBuilder.validator.UnitValidator;
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

    private final UnitDao unitDao;
    private final UnitValidator unitValidator;
    private final TeamService teamService;

    private final ItemDao itemDao;

    public UnitService(UnitDao unitDao, ItemDao itemDao, UnitValidator unitValidator, TeamService teamService) {
        this.unitDao = unitDao;
        this.itemDao = itemDao;
        this.unitValidator = unitValidator;
        this.teamService = teamService;
    }

    public Unit createNewUnit(Unit clientUnit, int userId){
        Unit newUnit = null;

        unitValidator.validateNewClientUnit(clientUnit, teamService.getTeamById(clientUnit.getTeamId(), userId) );

        try {
            newUnit = unitDao.createUnit(clientUnit);
            teamService.updateTeamAfterNewUnitPurchase(userId, newUnit);

        } catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }

        return newUnit;
    }

    public List<Unit> getUnitsForFaction(int factionId, Team team){
        List<Unit> units = null;
        try {
            units = unitDao.getListOfUnitsByFactionId(factionId);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }

        units = adjustUnitListForTeamStatus(team, units);

        return units;
    }

    public Unit updateUnit(Unit clientUnit, int userId){
        try {
            Unit currentUnit = unitDao.getUnitById(clientUnit.getId(), userId);

            unitValidator.validateUpdatedUnit(clientUnit, currentUnit);

            if (unitValidator.validFivePointLevel(currentUnit, clientUnit)){
                spendExpForAdvance(clientUnit);
            } else if (unitValidator.validTenPointLevel(currentUnit, clientUnit)) {
                spendExpForAdvance(clientUnit);
                clientUnit.setTenPointAdvances(clientUnit.getTenPointAdvances() + 1);
            }

            unitDao.updateUnit(clientUnit);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
        return unitDao.getUnitById(clientUnit.getId(), userId);
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

    public int purchaseItemForUnit(int itemReferenceId, int unitId, int userId){

        Item referenceItem = null;
        Unit unit = null;
        try {
            referenceItem = itemDao.lookupReferenceItem(itemReferenceId);
            unit = unitDao.getUnitById(unitId, userId);

        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }


        //TODO logic to validate purchase

        int itemId = 0;
        try {

           teamService.spendMoney( referenceItem.calculateCostToPurchase(unit), teamService.getTeamByUnitId(unitId));

           itemId = itemDao.addItemToUnit(referenceItem.getReferenceId(), unit.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return itemId;
    }


    /*
    Methods that go to DAO with no changes
     */
    public List<Skill> getPotentialSkills(int unitId){
        List<Skill> skillList = null;
        try {
            skillList = unitDao.getPotentialSkills(unitId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return skillList;
    }

    public Unit getUnitById(int unitId, int userId){
        Unit unit = null;
        try {
            unit = unitDao.getUnitById(unitId, userId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return unit;
    }

    public Unit getReferenceUnitByClass(String unitClass){
        Unit unit = null;
        try {
            unit = unitDao.convertReferenceUnitToUnit(unitClass);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return unit;
    }



    /*
        PRIVATE METHODS
     */
    public List<Unit> adjustUnitListForTeamStatus(Team team, List<Unit> units) {
        if ( unitValidator.teamMustBuyLeader(team) ) {
            units = units.stream()
                    .filter(unit -> "Leader".equalsIgnoreCase(unit.getRank()))
                    .collect(Collectors.toList());
            if (team.isBoughtFirstLeader()){
                units.forEach(unit -> unit.setBaseCost(unit.getBaseCost()/2));
            }
        } else {
            units = filterOutRank(units, "Leader");

            if (unitValidator.teamCanNotBuyElite(team)){
                units = filterOutRank(units, "Elite");
            }
            if (unitValidator.teamCanNotBuySpecialist(team)){
                units = filterOutRank(units, "Specialist");
            }
            if (unitValidator.teamCanNotBuyFreelancer(team)){
                units = filterOutRank(units, "Freelancer");
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
