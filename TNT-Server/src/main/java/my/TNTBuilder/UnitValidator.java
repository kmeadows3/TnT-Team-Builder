package my.TNTBuilder;
import my.TNTBuilder.model.Unit;
import java.util.Objects;

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

    public boolean onlyMeleeChanged(Unit unit, Unit updatedUnit){
        if ( unit.getMelee() != updatedUnit.getMelee()) {
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

    public boolean onlyStrengthChanged(Unit unit, Unit updatedUnit){
        if ( unit.getStrength() != updatedUnit.getStrength()) {
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

    public boolean onlyMoveChanged(Unit unit, Unit updatedUnit){
        if ( unit.getMove() != updatedUnit.getMove()) {
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

    public boolean onlyRangedChanged(Unit unit, Unit updatedUnit){
        if ( unit.getRanged() != updatedUnit.getRanged()) {
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

    public boolean onlyDefenseChanged(Unit unit, Unit updatedUnit){
        if ( unit.getDefense() != updatedUnit.getDefense()) {
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

    public boolean onlyWoundsChanged(Unit unit, Unit updatedUnit){
        if ( unit.getWounds() != updatedUnit.getWounds()) {
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

    public boolean onlyRankAndMettleChanged(Unit unit, Unit updatedUnit){
        if ( unit.getMettle() != updatedUnit.getMettle()) {
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

    public boolean onlyEmptySkillsChanged(Unit unit, Unit updatedUnit){
        if ( unit.getEmptySkills() != updatedUnit.getEmptySkills()) {
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
