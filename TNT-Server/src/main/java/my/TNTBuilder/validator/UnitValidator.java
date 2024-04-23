package my.TNTBuilder.validator;
import my.TNTBuilder.model.Unit;
import org.springframework.stereotype.Component;

import java.util.Objects;
@Component
public class UnitValidator {

    public boolean onlyNameChanged(Unit unit, Unit updatedUnit){
        if ( !(unit.getName().equals(updatedUnit.getName()))) {
            return unit.getId() == updatedUnit.getId() && unit.getTeamId() == updatedUnit.getTeamId()
                    && unit.getBaseCost() == updatedUnit.getBaseCost() && unit.getWounds() == updatedUnit.getWounds()
                    && unit.getDefense() == updatedUnit.getDefense() && unit.getMettle() == updatedUnit.getMettle()
                    && unit.getMove() == updatedUnit.getMove() && unit.getRanged() == updatedUnit.getRanged()
                    && unit.getMelee() == updatedUnit.getMelee() && unit.getStrength() == updatedUnit.getStrength()
                    && unit.getEmptySkills() == updatedUnit.getEmptySkills()
                    && unit.getSpentExperience() == updatedUnit.getSpentExperience()
                    && unit.getUnspentExperience() == updatedUnit.getUnspentExperience()
                    && unit.getTotalAdvances() == updatedUnit.getTotalAdvances()
                    && unit.getTenPointAdvances() == updatedUnit.getTenPointAdvances()
                    && Objects.equals(unit.getUnitClass(), updatedUnit.getUnitClass())
                    && Objects.equals(unit.getRank(), updatedUnit.getRank())
                    && Objects.equals(unit.getSpecies(), updatedUnit.getSpecies())
                    && Objects.equals(unit.getSpecialRules(), updatedUnit.getSpecialRules())
                    && Objects.equals(unit.getAvailableSkillsets(), updatedUnit.getAvailableSkillsets())
                    && Objects.equals(unit.getSkills(), updatedUnit.getSkills())
                    && Objects.equals(unit.getInventory(), updatedUnit.getInventory());

        }
        return false;
    }

    public boolean onlyUnspentExpGained(Unit unit, Unit updatedUnit){
        if ( unit.getUnspentExperience() < updatedUnit.getUnspentExperience()) {
            return unit.getId() == updatedUnit.getId() && unit.getTeamId() == updatedUnit.getTeamId()
                    && unit.getBaseCost() == updatedUnit.getBaseCost() && unit.getWounds() == updatedUnit.getWounds()
                    && unit.getDefense() == updatedUnit.getDefense() && unit.getMettle() == updatedUnit.getMettle()
                    && unit.getMove() == updatedUnit.getMove() && unit.getRanged() == updatedUnit.getRanged()
                    && unit.getMelee() == updatedUnit.getMelee() && unit.getStrength() == updatedUnit.getStrength()
                    && unit.getEmptySkills() == updatedUnit.getEmptySkills()
                    && unit.getSpentExperience() == updatedUnit.getSpentExperience()
                    && unit.getTotalAdvances() == updatedUnit.getTotalAdvances()
                    && unit.getTenPointAdvances() == updatedUnit.getTenPointAdvances()
                    && Objects.equals(unit.getName(), updatedUnit.getName())
                    && Objects.equals(unit.getUnitClass(), updatedUnit.getUnitClass())
                    && Objects.equals(unit.getRank(), updatedUnit.getRank())
                    && Objects.equals(unit.getSpecies(), updatedUnit.getSpecies())
                    && Objects.equals(unit.getSpecialRules(), updatedUnit.getSpecialRules())
                    && Objects.equals(unit.getAvailableSkillsets(), updatedUnit.getAvailableSkillsets())
                    && Objects.equals(unit.getSkills(), updatedUnit.getSkills())
                    && Objects.equals(unit.getInventory(), updatedUnit.getInventory());

        }
        return false;
    }

    //TODO test everything below this
    public boolean validWoundsChange(Unit unit, Unit updatedUnit){
        if ( updatedUnit.getWounds() - unit.getWounds() == 1) {
            return unit.getId() == updatedUnit.getId() && unit.getTeamId() == updatedUnit.getTeamId()
                    && unit.getBaseCost() == updatedUnit.getBaseCost()
                    && unit.getDefense() == updatedUnit.getDefense() && unit.getMettle() == updatedUnit.getMettle()
                    && unit.getMove() == updatedUnit.getMove() && unit.getRanged() == updatedUnit.getRanged()
                    && unit.getMelee() == updatedUnit.getMelee() && unit.getStrength() == updatedUnit.getStrength()
                    && unit.getEmptySkills() == updatedUnit.getEmptySkills()
                    && unit.getSpentExperience() == updatedUnit.getSpentExperience()
                    && unit.getUnspentExperience() == updatedUnit.getUnspentExperience()
                    && unit.getTotalAdvances() == updatedUnit.getTotalAdvances()
                    && unit.getTenPointAdvances() == updatedUnit.getTenPointAdvances()
                    && Objects.equals(unit.getName(), updatedUnit.getName())
                    && Objects.equals(unit.getUnitClass(), updatedUnit.getUnitClass())
                    && Objects.equals(unit.getRank(), updatedUnit.getRank())
                    && Objects.equals(unit.getSpecies(), updatedUnit.getSpecies())
                    && Objects.equals(unit.getSpecialRules(), updatedUnit.getSpecialRules())
                    && Objects.equals(unit.getAvailableSkillsets(), updatedUnit.getAvailableSkillsets())
                    && Objects.equals(unit.getSkills(), updatedUnit.getSkills())
                    && Objects.equals(unit.getInventory(), updatedUnit.getInventory());
        }
        return false;
    }

    public boolean validPromotion(Unit unit, Unit updatedUnit){
        if ( updatedUnit.getMettle() - unit.getMettle() == 1) {
            return unit.getId() == updatedUnit.getId() && unit.getTeamId() == updatedUnit.getTeamId()
                    && unit.getBaseCost() == updatedUnit.getBaseCost() && unit.getWounds() == updatedUnit.getWounds()
                    && unit.getDefense() == updatedUnit.getDefense()
                    && unit.getMove() == updatedUnit.getMove() && unit.getRanged() == updatedUnit.getRanged()
                    && unit.getMelee() == updatedUnit.getMelee() && unit.getStrength() == updatedUnit.getStrength()
                    && unit.getEmptySkills() == updatedUnit.getEmptySkills()
                    && unit.getSpentExperience() == updatedUnit.getSpentExperience()
                    && unit.getUnspentExperience() == updatedUnit.getUnspentExperience()
                    && unit.getTotalAdvances() == updatedUnit.getTotalAdvances()
                    && unit.getTenPointAdvances() == updatedUnit.getTenPointAdvances()
                    && Objects.equals(unit.getName(), updatedUnit.getName())
                    && Objects.equals(unit.getUnitClass(), updatedUnit.getUnitClass())
                    && Objects.equals(unit.getSpecies(), updatedUnit.getSpecies())
                    && Objects.equals(unit.getSpecialRules(), updatedUnit.getSpecialRules())
                    && Objects.equals(unit.getAvailableSkillsets(), updatedUnit.getAvailableSkillsets())
                    && Objects.equals(unit.getSkills(), updatedUnit.getSkills())
                    && Objects.equals(unit.getInventory(), updatedUnit.getInventory());
        }
        return false;
    }

    public boolean validFivePointLevel(Unit unit, Unit updatedUnit){
        return validMeleeChange(unit, updatedUnit) || validStrengthChange(unit, updatedUnit)
            || validMoveChange(unit, updatedUnit) || validRangedChange(unit, updatedUnit)
            || validDefenseChange(unit, updatedUnit) || validGainEmptySkill(unit, updatedUnit);
    }

    public boolean validTenPointLevel(Unit unit, Unit updatedUnit){
        return validWoundsChange(unit, updatedUnit) || validPromotion(unit, updatedUnit);
    }

    public boolean unitCanAffordAdvance(Unit unit){
        return unit.getUnspentExperience() - unit.getCostToAdvance() >= 0;
    }



    /*
    Private Methods
    */

    private boolean validMeleeChange(Unit unit, Unit updatedUnit){
        if ( updatedUnit.getMelee() - unit.getMelee() == 1) {
            return unit.getId() == updatedUnit.getId() && unit.getTeamId() == updatedUnit.getTeamId()
                    && unit.getBaseCost() == updatedUnit.getBaseCost() && unit.getWounds() == updatedUnit.getWounds()
                    && unit.getDefense() == updatedUnit.getDefense() && unit.getMettle() == updatedUnit.getMettle()
                    && unit.getMove() == updatedUnit.getMove() && unit.getRanged() == updatedUnit.getRanged()
                    && unit.getStrength() == updatedUnit.getStrength()
                    && unit.getEmptySkills() == updatedUnit.getEmptySkills()
                    && unit.getSpentExperience() == updatedUnit.getSpentExperience()
                    && unit.getUnspentExperience() == updatedUnit.getUnspentExperience()
                    && unit.getTotalAdvances() == updatedUnit.getTotalAdvances()
                    && unit.getTenPointAdvances() == updatedUnit.getTenPointAdvances()
                    && Objects.equals(unit.getName(), updatedUnit.getName())
                    && Objects.equals(unit.getUnitClass(), updatedUnit.getUnitClass())
                    && Objects.equals(unit.getRank(), updatedUnit.getRank())
                    && Objects.equals(unit.getSpecies(), updatedUnit.getSpecies())
                    && Objects.equals(unit.getSpecialRules(), updatedUnit.getSpecialRules())
                    && Objects.equals(unit.getAvailableSkillsets(), updatedUnit.getAvailableSkillsets())
                    && Objects.equals(unit.getSkills(), updatedUnit.getSkills())
                    && Objects.equals(unit.getInventory(), updatedUnit.getInventory());
        }
        return false;
    }

    private boolean validStrengthChange(Unit unit, Unit updatedUnit){
        if ( updatedUnit.getStrength() - unit.getStrength() == 1) {
            return unit.getId() == updatedUnit.getId() && unit.getTeamId() == updatedUnit.getTeamId()
                    && unit.getBaseCost() == updatedUnit.getBaseCost() && unit.getWounds() == updatedUnit.getWounds()
                    && unit.getDefense() == updatedUnit.getDefense() && unit.getMettle() == updatedUnit.getMettle()
                    && unit.getMove() == updatedUnit.getMove() && unit.getRanged() == updatedUnit.getRanged()
                    && unit.getMelee() == updatedUnit.getMelee()
                    && unit.getEmptySkills() == updatedUnit.getEmptySkills()
                    && unit.getSpentExperience() == updatedUnit.getSpentExperience()
                    && unit.getUnspentExperience() == updatedUnit.getUnspentExperience()
                    && unit.getTotalAdvances() == updatedUnit.getTotalAdvances()
                    && unit.getTenPointAdvances() == updatedUnit.getTenPointAdvances()
                    && Objects.equals(unit.getName(), updatedUnit.getName())
                    && Objects.equals(unit.getUnitClass(), updatedUnit.getUnitClass())
                    && Objects.equals(unit.getRank(), updatedUnit.getRank())
                    && Objects.equals(unit.getSpecies(), updatedUnit.getSpecies())
                    && Objects.equals(unit.getSpecialRules(), updatedUnit.getSpecialRules())
                    && Objects.equals(unit.getAvailableSkillsets(), updatedUnit.getAvailableSkillsets())
                    && Objects.equals(unit.getSkills(), updatedUnit.getSkills())
                    && Objects.equals(unit.getInventory(), updatedUnit.getInventory());
        }
        return false;
    }

    private boolean validMoveChange(Unit unit, Unit updatedUnit){
        if ( updatedUnit.getMove() - unit.getMove() == 1) {
            return unit.getId() == updatedUnit.getId() && unit.getTeamId() == updatedUnit.getTeamId()
                    && unit.getBaseCost() == updatedUnit.getBaseCost() && unit.getWounds() == updatedUnit.getWounds()
                    && unit.getDefense() == updatedUnit.getDefense() && unit.getMettle() == updatedUnit.getMettle()
                    && unit.getRanged() == updatedUnit.getRanged()
                    && unit.getMelee() == updatedUnit.getMelee() && unit.getStrength() == updatedUnit.getStrength()
                    && unit.getEmptySkills() == updatedUnit.getEmptySkills()
                    && unit.getSpentExperience() == updatedUnit.getSpentExperience()
                    && unit.getUnspentExperience() == updatedUnit.getUnspentExperience()
                    && unit.getTotalAdvances() == updatedUnit.getTotalAdvances()
                    && unit.getTenPointAdvances() == updatedUnit.getTenPointAdvances()
                    && Objects.equals(unit.getName(), updatedUnit.getName())
                    && Objects.equals(unit.getUnitClass(), updatedUnit.getUnitClass())
                    && Objects.equals(unit.getRank(), updatedUnit.getRank())
                    && Objects.equals(unit.getSpecies(), updatedUnit.getSpecies())
                    && Objects.equals(unit.getSpecialRules(), updatedUnit.getSpecialRules())
                    && Objects.equals(unit.getAvailableSkillsets(), updatedUnit.getAvailableSkillsets())
                    && Objects.equals(unit.getSkills(), updatedUnit.getSkills())
                    && Objects.equals(unit.getInventory(), updatedUnit.getInventory());
        }
        return false;
    }

    private boolean validRangedChange(Unit unit, Unit updatedUnit){
        if ( updatedUnit.getRanged() - unit.getRanged() == 1) {
            return unit.getId() == updatedUnit.getId() && unit.getTeamId() == updatedUnit.getTeamId()
                    && unit.getBaseCost() == updatedUnit.getBaseCost() && unit.getWounds() == updatedUnit.getWounds()
                    && unit.getDefense() == updatedUnit.getDefense() && unit.getMettle() == updatedUnit.getMettle()
                    && unit.getMove() == updatedUnit.getMove()
                    && unit.getMelee() == updatedUnit.getMelee() && unit.getStrength() == updatedUnit.getStrength()
                    && unit.getEmptySkills() == updatedUnit.getEmptySkills()
                    && unit.getSpentExperience() == updatedUnit.getSpentExperience()
                    && unit.getUnspentExperience() == updatedUnit.getUnspentExperience()
                    && unit.getTotalAdvances() == updatedUnit.getTotalAdvances()
                    && unit.getTenPointAdvances() == updatedUnit.getTenPointAdvances()
                    && Objects.equals(unit.getName(), updatedUnit.getName())
                    && Objects.equals(unit.getUnitClass(), updatedUnit.getUnitClass())
                    && Objects.equals(unit.getRank(), updatedUnit.getRank())
                    && Objects.equals(unit.getSpecies(), updatedUnit.getSpecies())
                    && Objects.equals(unit.getSpecialRules(), updatedUnit.getSpecialRules())
                    && Objects.equals(unit.getAvailableSkillsets(), updatedUnit.getAvailableSkillsets())
                    && Objects.equals(unit.getSkills(), updatedUnit.getSkills())
                    && Objects.equals(unit.getInventory(), updatedUnit.getInventory());
        }
        return false;
    }

    private boolean validDefenseChange(Unit unit, Unit updatedUnit){
        if ( updatedUnit.getDefense() - unit.getDefense() == 1) {
            return unit.getId() == updatedUnit.getId() && unit.getTeamId() == updatedUnit.getTeamId()
                    && unit.getBaseCost() == updatedUnit.getBaseCost() && unit.getWounds() == updatedUnit.getWounds()
                    && unit.getMettle() == updatedUnit.getMettle()
                    && unit.getMove() == updatedUnit.getMove() && unit.getRanged() == updatedUnit.getRanged()
                    && unit.getMelee() == updatedUnit.getMelee() && unit.getStrength() == updatedUnit.getStrength()
                    && unit.getEmptySkills() == updatedUnit.getEmptySkills()
                    && unit.getSpentExperience() == updatedUnit.getSpentExperience()
                    && unit.getUnspentExperience() == updatedUnit.getUnspentExperience()
                    && unit.getTotalAdvances() == updatedUnit.getTotalAdvances()
                    && unit.getTenPointAdvances() == updatedUnit.getTenPointAdvances()
                    && Objects.equals(unit.getName(), updatedUnit.getName())
                    && Objects.equals(unit.getUnitClass(), updatedUnit.getUnitClass())
                    && Objects.equals(unit.getRank(), updatedUnit.getRank())
                    && Objects.equals(unit.getSpecies(), updatedUnit.getSpecies())
                    && Objects.equals(unit.getSpecialRules(), updatedUnit.getSpecialRules())
                    && Objects.equals(unit.getAvailableSkillsets(), updatedUnit.getAvailableSkillsets())
                    && Objects.equals(unit.getSkills(), updatedUnit.getSkills())
                    && Objects.equals(unit.getInventory(), updatedUnit.getInventory());
        }
        return false;
    }


    private boolean validGainEmptySkill(Unit unit, Unit updatedUnit){
        if ( updatedUnit.getEmptySkills() - unit.getEmptySkills() == 1) {
            return unit.getId() == updatedUnit.getId() && unit.getTeamId() == updatedUnit.getTeamId()
                    && unit.getBaseCost() == updatedUnit.getBaseCost() && unit.getWounds() == updatedUnit.getWounds()
                    && unit.getDefense() == updatedUnit.getDefense() && unit.getMettle() == updatedUnit.getMettle()
                    && unit.getMove() == updatedUnit.getMove() && unit.getRanged() == updatedUnit.getRanged()
                    && unit.getMelee() == updatedUnit.getMelee() && unit.getStrength() == updatedUnit.getStrength()
                    && unit.getSpentExperience() == updatedUnit.getSpentExperience()
                    && unit.getUnspentExperience() == updatedUnit.getUnspentExperience()
                    && unit.getTotalAdvances() == updatedUnit.getTotalAdvances()
                    && unit.getTenPointAdvances() == updatedUnit.getTenPointAdvances()
                    && Objects.equals(unit.getName(), updatedUnit.getName())
                    && Objects.equals(unit.getUnitClass(), updatedUnit.getUnitClass())
                    && Objects.equals(unit.getRank(), updatedUnit.getRank())
                    && Objects.equals(unit.getSpecies(), updatedUnit.getSpecies())
                    && Objects.equals(unit.getSpecialRules(), updatedUnit.getSpecialRules())
                    && Objects.equals(unit.getAvailableSkillsets(), updatedUnit.getAvailableSkillsets())
                    && Objects.equals(unit.getSkills(), updatedUnit.getSkills())
                    && Objects.equals(unit.getInventory(), updatedUnit.getInventory());
        }
        return false;
    }




    /*
    Completely equal units

    unit.getId() == updatedUnit.getId() && unit.getTeamId() == updatedUnit.getTeamId()
        && unit.getBaseCost() == updatedUnit.getBaseCost() && unit.getWounds() == updatedUnit.getWounds()
        && unit.getDefense() == updatedUnit.getDefense() && unit.getMettle() == updatedUnit.getMettle()
        && unit.getMove() == updatedUnit.getMove() && unit.getRanged() == updatedUnit.getRanged()
        && unit.getMelee() == updatedUnit.getMelee() && unit.getStrength() == updatedUnit.getStrength()
        && unit.getEmptySkills() == updatedUnit.getEmptySkills()
        && unit.getSpentExperience() == updatedUnit.getSpentExperience()
        && unit.getUnspentExperience() == updatedUnit.getUnspentExperience()
        && unit.getTotalAdvances() == updatedUnit.getTotalAdvances()
        && unit.getTenPointAdvances() == updatedUnit.getTenPointAdvances()
        && Objects.equals(unit.getName(), updatedUnit.getName())
        && Objects.equals(unit.getUnitClass(), updatedUnit.getUnitClass())
        && Objects.equals(unit.getRank(), updatedUnit.getRank())
        && Objects.equals(unit.getSpecies(), updatedUnit.getSpecies())
        && Objects.equals(unit.getSpecialRules(), updatedUnit.getSpecialRules())
        && Objects.equals(unit.getAvailableSkillsets(), updatedUnit.getAvailableSkillsets())
        && Objects.equals(unit.getSkills(), updatedUnit.getSkills())
        && Objects.equals(unit.getInventory(), updatedUnit.getInventory());
     */


}
