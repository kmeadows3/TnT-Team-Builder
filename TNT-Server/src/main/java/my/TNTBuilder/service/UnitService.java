package my.TNTBuilder.service;

import my.TNTBuilder.dao.ItemDao;
import my.TNTBuilder.exception.ValidationException;
import my.TNTBuilder.model.Injury;
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

    public static final int INJURY_SKILLSET_ID = 16;
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

    public Unit createNewUnit(Unit clientUnit, int userId)  throws ServiceException{
        Unit newUnit = null;


        try {
            unitValidator.validateNewUnit(clientUnit, teamService.getTeamById(clientUnit.getTeamId(), userId) );
            newUnit = unitDao.createUnit(clientUnit);
            teamService.updateTeamAfterNewUnitPurchase(userId, newUnit);

        } catch (DaoException | ValidationException e){
            throw new ServiceException(e.getMessage());
        }

        return newUnit;
    }

    public List<Unit> getUnitsForFaction (int factionId, Team team) throws ServiceException{
        List<Unit> units = null;
        try {
            units = unitDao.getListOfUnitsByFactionId(factionId);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }

        units = adjustUnitListForTeamStatus(team, units);

        return units;
    }

    public Unit updateUnit(Unit clientUnit, int userId) throws ServiceException{

        Unit currentUnit = null;
        try {
            currentUnit = unitDao.getUnitById(clientUnit.getId(), userId);

            unitValidator.validateUpdatedUnit(clientUnit, currentUnit);

            if (unitValidator.validFivePointLevel(currentUnit, clientUnit)){
                spendExpForAdvance(clientUnit);
            } else if (unitValidator.validTenPointLevel(currentUnit, clientUnit)) {
                spendExpForAdvance(clientUnit);
                clientUnit.setTenPointAdvances(clientUnit.getTenPointAdvances() + 1);
            }

            unitDao.updateUnit(clientUnit);
            currentUnit = unitDao.getUnitById(clientUnit.getId(), userId);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
        return currentUnit;
    }

    public void addSkillToUnit(Skill skill, int unitId, int userId) throws ServiceException{
        try {
            if (unitCanAddSkill(unitId, skill, userId)){
                unitDao.addSkillToUnit(skill.getId(), unitId);
                Unit unit = unitDao.getUnitById(unitId, userId);
                unit.setEmptySkills(unit.getEmptySkills() - 1);
                unitDao.updateUnit(unit);
            }
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void deleteUnit(int unitId, int userId, boolean deleteItems) throws ServiceException{

        try {
            Unit unitToDelete = unitDao.getUnitById(unitId, userId);
            if (deleteItems) {
                for (Item item : unitToDelete.getInventory()){
                    itemDao.deleteItem(item.getId());
                }
            } else if (unitToDelete.getRank().equals("Freelancer")) {
                for (Item item : unitToDelete.getInventory()){
                    transferOnlyNonWeapons(unitId, item);
                }
            } else {
                for (Item item : unitToDelete.getInventory()){
                    itemDao.transferItem(item.getId(), unitId, teamService.getTeamByUnitId(unitId).getId(), false);
                }
            }
            unitDao.deleteUnit(unitToDelete);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }

    }

    /*
    Methods that go to DAO with no changes
     */
    public List<Skill> getPotentialSkills(int unitId) throws ServiceException{
        List<Skill> skillList = null;
        try {
            skillList = unitDao.getPotentialSkills(unitId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return skillList;
    }

    public Unit getUnitById(int unitId, int userId) throws ServiceException{
        Unit unit = null;
        try {
            unit = unitDao.getUnitById(unitId, userId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return unit;
    }

    public Unit getReferenceUnitByClass(String unitClass) throws ServiceException{
        Unit unit = null;
        try {
            unit = unitDao.convertReferenceUnitToUnit(unitClass);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return unit;
    }

    public List<Injury> getPotentialInjuries(int unitId, int userId) throws ServiceException{
        Unit unit = null;
        List<Injury> injuries = null;
        try {
            unit = unitDao.getUnitById(unitId, userId);
            injuries = unitDao.getAllPotentialInjuries(unit);
            if (injuries == null){
                throw new ServiceException("No injuries returned");
            }
            for (Injury injury : unit.getInjuries()){
                if (!injury.isStackable()){
                    injuries.remove(injury);
                }
            }

        } catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }
        return injuries;
    }

    public void deleteInjury(int injuryId, int unitId, int userId) throws ServiceException{
        Unit unit = getUnitById(unitId, userId);
        Injury injuryToDelete = selectInjuryFromInjuryList(injuryId, unit);

        if (injuryToDelete == null){
            throw new ServiceException("This unit does not have that injury");
        } else if (!injuryToDelete.isRemovable()){
            throw new ServiceException("This injury cannot be removed.");
        }

        try {
            unitDao.deleteInjuryFromUnit(injuryId, unitId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    public void addInjury(int injuryId, int unitId, int userId) throws ServiceException {
        Unit unit = getUnitById(unitId, userId);

        Injury injury = selectInjuryFromInjuryList(injuryId, unit);

        try {
            if (injury == null){
                injury = unitDao.selectInjuryById(injuryId);
                applyInjuryEffects(injury, unit);
                unitDao.addInjuryToUnit(injuryId, unitId);
            } else if (injury.isStackable()){
                applyInjuryEffects(injury, unit);
                unitDao.updateInjuryCount(injuryId, unitId, injury.getCount() + 1);
            } else {
                throw new ServiceException("Unit already has this injury and cannot add another instance.");
            }

            unitDao.updateUnit(unit);

        } catch (DaoException | ValidationException e) {
            throw new ServiceException(e.getMessage(), e);
        }

    }

    private void applyInjuryEffects(Injury injury, Unit unit) throws ValidationException {
        if (injury.isStatDamage()){
            switch (injury.getStatDamaged()) {
                case "Mettle":
                    unit.setMettle(unit.getMettle() - 1);
                    break;
                case "Move":
                    unit.setMove(unit.getMove() - 1);
                    break;
                case "Ranged":
                    unit.setRanged(unit.getRanged() - 1);
                    break;
                case "Defense":
                    unit.setDefense(unit.getDefense() - 1);
                    break;
                case "Melee":
                    unit.setMelee(unit.getMelee() - 1);
                    break;
                default:
                    throw new ValidationException("Error: improperly configured injury");
            }
        }
    }


    /*
        PRIVATE METHODS
     */
    private void transferOnlyNonWeapons(int unitId, Item item) throws ServiceException {
        if (item.getCategory().equals("Armor") || item.getCategory().equals("Equipment")){
            itemDao.transferItem(item.getId(), unitId, teamService.getTeamByUnitId(unitId).getId(), false);
        } else {
            itemDao.deleteItem(item.getId());
        }
    }

    private List<Unit> adjustUnitListForTeamStatus(Team team, List<Unit> units) {
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

    private boolean unitCanAddSkill(int unitId, Skill skill, int userId) throws ServiceException, DaoException {

        Unit unit = unitDao.getUnitById(unitId, userId);
        int skillId = skill.getId();

        if (unit == null) {
            throw new ServiceException("Error, invalid unit.");
        } else if (skill.getSkillsetId() == INJURY_SKILLSET_ID){
            return true;
        } else if (unit.getEmptySkills() < 1){
            throw new ServiceException("Error, no open skills.");
        } else {

            for (Skill ownedSkills : unit.getSkills()) {
                if (ownedSkills.getId() == skillId) {
                    throw new ServiceException("Error, unit already cannot have two copies of a ability.");
                }
            }

            List<Skill> potentialSkills = unitDao.getPotentialSkills(unit.getId());
            for (Skill potentialSkill: potentialSkills){
                if(potentialSkill.getId() == skillId){
                    return true;
                }
            }
        }

        throw new ServiceException("Error, unit cannot have this skill.");
    }

    private Injury selectInjuryFromInjuryList(int injuryId, Unit unit) throws ServiceException {
        for (Injury injury : unit.getInjuries()){
            if (injury.getId() == injuryId){
                return injury;
            }
        }
        return null;
    }



}
