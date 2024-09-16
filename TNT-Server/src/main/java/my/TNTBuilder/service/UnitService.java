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

    public static final int LARGE_SKILL_ID = 103;
    private final UnitDao unitDao;
    private final UnitValidator unitValidator;
    private final TeamService teamService;
    private final ItemDao itemDao;
    private final int FRENZIED_SKILL_ID = 99;

    public UnitService(UnitDao unitDao, ItemDao itemDao, UnitValidator unitValidator, TeamService teamService) {
        this.unitDao = unitDao;
        this.itemDao = itemDao;
        this.unitValidator = unitValidator;
        this.teamService = teamService;
    }

    public Unit createNewUnit(Unit clientUnit, int userId, boolean isExploreGain)  throws ServiceException{
        Unit newUnit = null;


        try {
            if (isExploreGain){
                newUnit = unitDao.createUnit(clientUnit);
            } else {
                unitValidator.validateNewUnit(clientUnit, teamService.getTeamById(clientUnit.getTeamId(), userId) );
                newUnit = unitDao.createUnit(clientUnit);
                teamService.updateTeamAfterNewUnitPurchase(userId, newUnit);
            }


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

    public List<Unit> getExplorationUnits() throws ServiceException{
        List<Unit> units = null;
        try {
            units = unitDao.getExplorationUnits();
        } catch (DaoException e){
            throw new ServiceException(e.getMessage());
        }

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

            clientUnit.setNewPurchase(false);
            unitDao.updateUnit(clientUnit);
            currentUnit = unitDao.getUnitById(clientUnit.getId(), userId);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
        return currentUnit;
    }

    public void addSkillToUnit(Skill skill, int unitId, int userId) throws ServiceException{
        try {
            Unit unit = unitDao.getUnitById(unitId, userId);

            if (unitCanAddSkill(unit, skill)){

                if (skill.isMutation() && skill.getCost() != 0){
                    teamService.spendMoney(skill.getCost(), teamService.getTeamByUnitId(unitId));
                }

                addSideEffectsToAddingSkill(skill, unit);

                if (skill.isDetriment() && getExistingDetrimentCount(skill, unit) > 0){
                    skill.setCount(getExistingDetrimentCount(skill, unit) + 1);
                    unitDao.updateSkillCount(skill, unitId);
                } else {
                    unitDao.addSkillToUnit(skill.getId(), unitId);
                }

                updateUnitAfterSkillPurchase(skill, unitId, userId);

            }
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void addSideEffectsToAddingSkill(Skill skill, Unit unit) throws ValidationException {
        if (skill.getName().equals("Psychic")) {
            unitDao.addPsychicToSkillsets(unit.getId());
        } else if (skill.getName().equals("Weapon Growths (x2)")) {
            unit.setMelee(unit.getMelee() + 1);
            unitDao.updateUnit(unit);
            unitDao.deleteWeaponsGrowthsFromUnit(unit.getId());
        } else if (skill.getName().equals("Frother") && unit.getSkills().stream().noneMatch(unitSkill -> unitSkill.getId() == FRENZIED_SKILL_ID)) {
            unitDao.addSkillToUnit(FRENZIED_SKILL_ID, unit.getId());
        } else if (skill.getName().equals("Big")) {
            unit.setWounds(unit.getWounds() + 1);
            unit.setStrength(unit.getStrength() + 1);
            unitDao.addSkillToUnit(LARGE_SKILL_ID, unit.getId());
            unitDao.updateUnit(unit);
        } else if (skill.getName().equals("Long Arms")) {
            unit.setMelee(unit.getMelee() + 1);
            unitDao.updateUnit(unit);
        } else if (skill.getName().equals("Long Legs")) {
            unit.setMove(unit.getMove() + 1);
            unitDao.updateUnit(unit);
        } else if (skill.getName().equals("Multi-Limbed / Prehensile Tail")) {
            unit.setEmptySkills(unit.getEmptySkills() + 1);
            unitDao.updateUnit(unit);
        } else if (skill.getName().equals("No Arms")) {
            unit.setMove(unit.getMove() + 1);
            unitDao.updateUnit(unit);
        } else if (skill.getName().equals("Weapon Growths")){
            unit.setMelee(unit.getMelee() + 1);
            unitDao.updateUnit(unit);
        }else if (skill.isDetriment()) {
            handleDetrimentStatLoss(skill, unit);
        }
    }

    private void handleDetrimentStatLoss(Skill skill, Unit unit) throws ValidationException{
        int statValue = 0;

        switch (skill.getName()) {
            case "Atrophied Muscles":
                statValue = unit.getStrength() - 1;
                lowerStrengthFromDetriment(skill, unit, statValue);
                break;
            case "Frailty":
                statValue = unit.getStrength() - 1;
                lowerDefenseFromDetriment(skill, unit, statValue);
                break;
            case "Weakening Sight":
                statValue = unit.getStrength() - 1;
                lowerRangedFromDetriment(skill, unit, statValue);
                break;
            case "Inert Twin":
            case "Obese":
            case "No Legs":
                statValue = unit.getMove() -1;
                lowerMoveFromDetriment(skill, unit, statValue);
                break;
            case "Stumpy Leg":
                statValue = unit.getMove() - 2;
                lowerMoveFromDetriment(skill, unit, statValue);
                break;
            default:
                return;
        }

        unitDao.updateUnit(unit);

    }

    private void lowerMoveFromDetriment(Skill skill, Unit unit, int statValue) throws ValidationException {
        if (statValue > 0) {
            unit.setMove(statValue);
        } else if (unit.isCannotLowerMove()) {
            throw new ValidationException(skill.getName() + " has made this unit too weak to fight.");
        } else {
            unit.setMove(1);
            unit.setCannotLowerMove(true);
        }
    }

    private void lowerRangedFromDetriment(Skill skill, Unit unit, int statValue) throws ValidationException {
        if (statValue > 0) {
            unit.setRanged(statValue);
        } else if (unit.isCannotLowerRanged()) {
            throw new ValidationException(skill.getName() + " has made this unit too weak to fight.");
        } else {
            unit.setCannotLowerRanged(true);
        }
    }

    private void lowerDefenseFromDetriment(Skill skill, Unit unit, int statValue) throws ValidationException {
        if (statValue > 0) {
            unit.setDefense(statValue);
        } else if (unit.isCannotLowerDefense()) {
            throw new ValidationException(skill.getName() + " has made this unit too weak to fight.");
        } else {
            unit.setCannotLowerDefense(true);
        }
    }

    private void lowerStrengthFromDetriment(Skill skill, Unit unit, int statValue) throws ValidationException {
        if (statValue > 0) {
            unit.setStrength(statValue);
        } else if (unit.isCannotLowerStrength()) {
            throw new ValidationException(skill.getName() + " has made this unit too weak to fight.");
        } else {
            unit.setCannotLowerStrength(true);
        }
    }

    private int getExistingDetrimentCount(Skill skill, Unit unit) {

        List<Skill> existingSkills = unit.getSkills().stream()
                .filter(unitSkill -> unitSkill.equals(skill))
                .collect(Collectors.toList());

        if (!existingSkills.isEmpty()){
            Skill existingSkill = existingSkills.get(0);
            return existingSkill.getCount();
        }

        return 0;
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

    public List<Skill> getPotentialSkills(int unitId, int userId) throws ServiceException{
        List<Skill> skillList = null;
        try {
            skillList = unitDao.getPotentialSkills(unitId);

        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        removeUnpurchasableSkills(skillList, unitId, userId);

        return skillList;
    }

    /*
    Methods that go to DAO with no changes
     */

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

    /*
        PRIVATE METHODS
     */

    private boolean doesUnitHaveSkill (String skillName, Unit unit){
        return unit.getSkills().stream().anyMatch( skill -> skill.getName().equals(skillName));
    }

    private int countInstancesOfSkillOnTeam (String skillName, Unit unit) throws ServiceException{
        Team team = teamService.getTeamByUnitId(unit.getId());

        return (int) team.getUnitList().stream()
                .filter(teamUnit -> teamUnit.getSkills().stream()
                        .filter( skill -> skill.getName().equals(skillName)).count() > 0)
                .count();
    }

    private void removeUnpurchasableSkills(List<Skill> skillList, int unitId, int userId) throws ServiceException{
        Unit unit = unitDao.getUnitById(unitId, userId);
        boolean hasFearfulRep = false;
        boolean hasMedic = false;

        for (Skill skill : unit.getSkills()){
            if (skill.getName().equals("Fearful Reputation")) {
                hasFearfulRep = true;
            }
            if (skill.getName().equals("Medic")||skill.getName().equals("Healing Touch")){
                hasMedic= true;
            }
        }

        for (int i = 0; i < skillList.size(); i++){
            Skill skill = skillList.get(i);
            if      (skill.getName().equals("Psychic Battery")
                    ||  (hasFearfulRep && skill.getName().equals("Fearful Reputation"))
                    ||  (hasMedic && ( skill.getName().equals("Medic") || skill.getName().equals("Healing Touch")) )
                    ||  skillIsIllegal(skill, unit)
            ){
                skillList.remove(i);
            }

        }
    }

    private boolean skillIsIllegal(Skill skill, Unit unit) throws ServiceException{

        if (unit.getUnitClass().contains("Mondo") && skill.getName().equals("Psychic")){
            return true;
        } else if (skill.getName().equals("Weapon Growths (x2)")) {
            return unit.getSkills().stream().noneMatch(unitSkill -> unitSkill.getName().equals("Weapon Growths"));
        } else if (skill.getName().equals("Ranger")) {
            return countInstancesOfSkillOnTeam("Ranger", unit) >=3;
        } else if (skill.getName().equals("Reactive")) {
            return !doesUnitHaveSkill("Nimble", unit);
        } else if (skill.getName().equals("Nimble")) {
            return !doesUnitHaveSkill("Reactive", unit);
        } else if (skill.getName().equals("Flighty")){
            return !doesUnitHaveSkill("Push Off", unit);
        } else if (skill.getName().equals("Push Off")){
            return !doesUnitHaveSkill("Flighty", unit);
        } else if (skill.getName().equals("Resourceful")) {
            return countInstancesOfSkillOnTeam("Resourceful", unit) >=2;
        } else if (skill.getName().equals("Scavenger")) {
            return unit.getRank().equals("Freelancer");
        }

        return false;

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

    private void updateUnitAfterSkillPurchase(Skill skill, int unitId, int userId) {
        if (!skill.isDetriment() && !(skill.getName().equals("Psychic") || skill.getName().equals("Psychic Battery"))){
            Unit unit = unitDao.getUnitById(unitId, userId);

            unit.setEmptySkills(unit.getEmptySkills() - 1);

            if (unit.getEmptySkills()==0){
                unit.setNewPurchase(false);
            }

            unitDao.updateUnit(unit);

        }
    }
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

        units = units.stream().filter(unit -> unitValidator.teamCanHaveUnitClass(unit, team))
                .collect(Collectors.toList());


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

    private boolean unitCanAddSkill(Unit unit, Skill skill) throws ServiceException, DaoException {

        int skillId = skill.getId();

        if (unit.getEmptySkills() < 1 && !skill.isDetriment()){
            throw new ServiceException("Error, unit does not have any unpurchased skills.");
        } else if (skillIsIllegal(skill, unit)){
            throw new ServiceException("This unit is unable to purchase " + skill.getName());
        }else {

            if (!skill.isDetriment()){
                for (Skill ownedSkills : unit.getSkills()) {
                    if (ownedSkills.getId() == skillId) {
                        throw new ServiceException("Error, unit cannot have two copies of an ability.");
                    }
                }
            }

            if (!skill.isMutation()){
                List<Skill> potentialSkills = unitDao.getPotentialSkills(unit.getId());
                for (Skill potentialSkill: potentialSkills){
                    if(potentialSkill.getId() == skillId){
                        return true;
                    }
                }
            } else if (skill.isMutation() && unit.getSpecies().equals("Mutant")){
                return true;
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
