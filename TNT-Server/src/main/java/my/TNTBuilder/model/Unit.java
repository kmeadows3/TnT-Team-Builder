package my.TNTBuilder.model;

import my.TNTBuilder.exception.ValidationException;
import my.TNTBuilder.model.inventory.Armor;
import my.TNTBuilder.model.inventory.Item;
import javax.validation.constraints.Min;
import java.util.*;


public class Unit {
    @Min(value = 1, message = "Unit ID must be provided")
    private int id;
    @Min(value = 0, message = "Team ID must be provided")
    private int teamId;
    private String name = "";
    private String unitClass;
    private String rank;
    private String species;
    private int baseCost;
    private int wounds;
    private int defense;
    private int mettle;
    private int move;
    private int ranged;
    private int melee;
    private int strength;
    private int emptySkills;
    private String specialRules;
    private int spentExperience;
    private int unspentExperience = 0;
    private int totalAdvances = 0;
    private int tenPointAdvances = 0;
    private List<Skillset> availableSkillsets;
    private List<Skill> skills = new ArrayList<>();
    private List<Injury> injuries = new ArrayList<>();
    private List<Item> inventory = new ArrayList<>();
    private boolean isNewPurchase = true;
    private boolean cannotLowerStrength = false;
    private boolean cannotLowerDefense = false;
    private boolean cannotLowerRanged = false;
    private boolean cannotLowerMove = false;


    //constructors

    public Unit(){};

    public Unit(int id, int teamId, String name, String unitClass, String rank, String species, int baseCost, int wounds, int defense,
                int mettle, int move, int ranged, int melee, int strength, int emptySkills, String specialRules,
                int spentExperience, int unspentExperience, int totalAdvances, int tenPointAdvances,
                List<Skillset> availableSkillsets, List<Skill> skills, List<Injury> injuries, List<Item> inventory, boolean isNewPurchase) {
        this.id = id;
        this.teamId = teamId;
        this.name = name;
        this.unitClass = unitClass;
        this.rank = rank;
        this.species = species;
        this.baseCost = baseCost;
        this.wounds = wounds;
        this.defense = defense;
        this.mettle = mettle;
        this.move = move;
        this.ranged = ranged;
        this.melee = melee;
        this.strength = strength;
        this.emptySkills = emptySkills;
        this.specialRules = specialRules;
        this.spentExperience = spentExperience;
        this.unspentExperience = unspentExperience;
        this.totalAdvances = totalAdvances;
        this.tenPointAdvances = tenPointAdvances;
        this.availableSkillsets = availableSkillsets;
        this.skills = skills;
        this.injuries = injuries;
        this.inventory = inventory;
        this.isNewPurchase = isNewPurchase;
    }
    //Methods

    public int getBSCost() {
        int bsCost = baseCost;
        bsCost += (totalAdvances + tenPointAdvances) * 5;

        bsCost = inventory.stream().reduce( bsCost, (subtotal, item) -> {
            if (item instanceof Armor && wounds > 1) {
                int cost = wounds == 2 ? ((Armor) item).getCost2Wounds() :  ((Armor) item).getCost3Wounds();
                return subtotal + cost;
            }
            return subtotal + item.getCost();
        }, Integer::sum);

        return bsCost;
    }

    public int getUnitUpkeep() {
        int upkeep = 0;

        if (skills.stream().anyMatch(item -> "Scavenger".equals(item.getName()))) {
            upkeep += 0;
        } else if (rank.equals("Leader")) {
            upkeep += 3;
        } else if (rank.equals("Elite") || rank.equals("Specialist")) {
            upkeep += 2;
        } else if (rank.equals("Rank and File")) {
            upkeep += 1;
        }

        upkeep = inventory.stream()
                .reduce( upkeep, (subtotal, item) -> item.isRelic() ? subtotal + 1 : subtotal, Integer::sum);

        return upkeep;
    }

    public int getCostToAdvance() {
        int totalExp = spentExperience + unspentExperience;
        if (totalExp <= 20) {
            return 5;
        } else if (totalExp <= 45) {
            return 6;
        } else if (totalExp <= 74) {
            return 7;
        }
        return 8;
    }



    //Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitClass() {
        return unitClass;
    }

    public void setUnitClass(String unitClass) {
        this.unitClass = unitClass;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(int baseCost) {
        this.baseCost = baseCost;
    }

    public int getWounds() {
        return wounds;
    }

    public void setWounds(int wounds) {
        this.wounds = wounds;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) throws ValidationException{
        if (defense <= 0){
            throw new ValidationException("Defense cannot fall below 1. If an injury causes this to occur, instead kill the unit.");
        }
        this.defense = defense;
    }

    public int getMettle() {
        return mettle;
    }

    public void setMettle(int mettle) throws ValidationException{
        if (mettle <= 0){
            throw new ValidationException("Mettle cannot fall below 1. If an injury causes this to occur, instead kill the unit.");
        }
        this.mettle = mettle;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) throws ValidationException{
        if (move <= 0){
            throw new ValidationException("Move cannot fall below 1. If an injury causes this to occur, instead kill the unit.");
        }
        this.move = move;
    }

    public int getRanged() {
        return ranged;
    }

    public void setRanged(int ranged) {
        this.ranged = ranged;
    }

    public int getMelee() {
        return melee;
    }

    public void setMelee(int melee) {
        this.melee = melee;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getEmptySkills() {
        return emptySkills;
    }

    public void setEmptySkills(int emptySkills) {
        this.emptySkills = emptySkills;
    }

    public String getSpecialRules() {
        return specialRules;
    }

    public void setSpecialRules(String specialRules) {
        this.specialRules = specialRules;
    }

    public int getSpentExperience() {
        return spentExperience;
    }

    public void setSpentExperience(int spentExperience) {
        this.spentExperience = spentExperience;
    }

    public int getUnspentExperience() {
        return unspentExperience;
    }

    public void setUnspentExperience(int unspentExperience) {
        this.unspentExperience = unspentExperience;
    }

    public int getTotalAdvances() {
        return totalAdvances;
    }

    public void setTotalAdvances(int totalAdvances) {
        this.totalAdvances = totalAdvances;
    }

    public int getTenPointAdvances() {
        return tenPointAdvances;
    }

    public void setTenPointAdvances(int tenPointAdvances) {
        this.tenPointAdvances = tenPointAdvances;
    }

    public List<Skillset> getAvailableSkillsets() {
        return availableSkillsets;
    }

    public void setAvailableSkillsets(List<Skillset> availableSkillsets) {
        this.availableSkillsets = availableSkillsets;
    }
    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public List<Injury> getInjuries() {
        return injuries;
    }

    public void setInjuries(List<Injury> injuries) {
        this.injuries = injuries;
    }

    public boolean isNewPurchase() {
        return isNewPurchase;
    }

    public void setNewPurchase(boolean newPurchase) {
        isNewPurchase = newPurchase;
    }

    public boolean isCannotLowerStrength() {
        return cannotLowerStrength;
    }

    public void setCannotLowerStrength(boolean cannotLowerStrength) {
        this.cannotLowerStrength = cannotLowerStrength;
    }

    public boolean isCannotLowerDefense() {
        return cannotLowerDefense;
    }

    public void setCannotLowerDefense(boolean cannotLowerDefense) {
        this.cannotLowerDefense = cannotLowerDefense;
    }

    public boolean isCannotLowerRanged() {
        return cannotLowerRanged;
    }

    public void setCannotLowerRanged(boolean cannotLowerRanged) {
        this.cannotLowerRanged = cannotLowerRanged;
    }

    public boolean isCannotLowerMove() {
        return cannotLowerMove;
    }

    public void setCannotLowerMove(boolean cannotLowerMove) {
        this.cannotLowerMove = cannotLowerMove;
    }

    //Override Equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return id == unit.id && teamId == unit.teamId && baseCost == unit.baseCost && wounds == unit.wounds &&
                defense == unit.defense && mettle == unit.mettle && move == unit.move && ranged == unit.ranged &&
                melee == unit.melee && strength == unit.strength && emptySkills == unit.emptySkills &&
                spentExperience == unit.spentExperience && unspentExperience == unit.unspentExperience &&
                totalAdvances == unit.totalAdvances && tenPointAdvances == unit.tenPointAdvances &&
                cannotLowerDefense == unit.cannotLowerDefense && unit.cannotLowerRanged == cannotLowerRanged &&
                cannotLowerStrength == unit.cannotLowerStrength && cannotLowerMove == unit.cannotLowerMove &&
                Objects.equals(name, unit.name) && Objects.equals(unitClass, unit.unitClass) &&
                Objects.equals(rank, unit.rank) && Objects.equals(species, unit.species) &&
                Objects.equals(specialRules, unit.specialRules) &&
                Objects.equals(availableSkillsets, unit.availableSkillsets) && Objects.equals(skills, unit.skills) &&
                new HashSet<>(injuries).containsAll(unit.injuries) && new HashSet<>(unit.injuries).containsAll(injuries) &&
                new HashSet<>(inventory).containsAll(unit.inventory) && new HashSet<>(unit.inventory).containsAll(inventory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamId, name, unitClass, rank, species, baseCost, wounds, defense, mettle, move, ranged,
                melee, strength, emptySkills, specialRules, spentExperience, unspentExperience, totalAdvances,
                tenPointAdvances, availableSkillsets, skills, injuries, inventory, cannotLowerDefense, cannotLowerStrength,
                cannotLowerRanged, cannotLowerMove);
    }
}