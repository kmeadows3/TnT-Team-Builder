package my.TNTBuilder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import my.TNTBuilder.model.inventory.Item;

import java.util.*;

public class Unit {
    private int id;
    private int teamId;
    private String name = "";
    private String unitClass;
    private String rank = "Rank and File";
    private String species;
    private int baseCost = 0;
    private int wounds;
    private int defense;
    private int mettle;
    private int move;
    private int ranged;
    private int melee;
    private int strength;
    private int emptySkills;
    private String specialRules;
    private int spentExperience = 0;
    private int unspentExperience = 0;
    private int totalAdvances = 0;
    private int tenPointAdvances = 0;
    private List<Skillset> availableSkillsets;
    private List<Skill> skills = new ArrayList<>();
    private List<Item> inventory = new ArrayList<>();


    //constructors

    public Unit(){};

    public Unit(int id, int teamId, String name, String unitClass, String rank, String species, int baseCost, int wounds, int defense,
                int mettle, int move, int ranged, int melee, int strength, int emptySkills, String specialRules,
                int spentExperience, int unspentExperience, int totalAdvances, int tenPointAdvances,
                List<Skillset> availableSkillsets, List<Skill> skills, List<Item> inventory) {
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
        this.inventory = inventory;
    }
    //Methods

    public int getBSCost() {
        int bsCost = baseCost;
        //TODO calculate this after inventory is implemented
        return bsCost;
    }

    public int getUnitUpkeep() {
        int upkeep = 0;

        List<String> skillNames = new ArrayList<>();
        for (Skill skill : skills) {
            skillNames.add(skill.getName());
        }
        boolean isScavenger = skillNames.contains("Scavenger");

        if (isScavenger) {
            upkeep += 0;
        } else if (rank.equals("Leader")) {
            upkeep += 3;
        } else if (rank.equals("Elite") || rank.equals("Specialist")) {
            upkeep += 2;
        } else if (rank.equals("Rank and File")) {
            upkeep += 1;
        }
        // TODO: Deal with relics in inventory
        return upkeep;
    }

    public int costToAdvance() {
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

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMettle() {
        return mettle;
    }

    public void setMettle(int mettle) {
        this.mettle = mettle;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
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
                Objects.equals(name, unit.name) && Objects.equals(unitClass, unit.unitClass) &&
                Objects.equals(rank, unit.rank) && Objects.equals(species, unit.species) &&
                Objects.equals(specialRules, unit.specialRules) &&
                Objects.equals(availableSkillsets, unit.availableSkillsets) && Objects.equals(skills, unit.skills) &&
                Objects.equals(inventory, unit.inventory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamId, name, unitClass, rank, species, baseCost, wounds, defense, mettle, move, ranged,
                melee, strength, emptySkills, specialRules, spentExperience, unspentExperience, totalAdvances,
                tenPointAdvances, availableSkillsets, skills, inventory);
    }
}