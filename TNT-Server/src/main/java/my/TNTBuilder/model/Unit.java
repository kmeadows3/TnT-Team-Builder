package my.TNTBuilder.model;

import my.TNTBuilder.model.inventory.Item;

import java.util.*;

public class Unit implements Cloneable {
    private int id;
    private String unitNickname; //not called name so it's not confusing with getName
    private String title;
    private String faction;
    private String rank;
    private String type;
    private int baseCost;
    private String newPurchaseNote;
    private int additionalStartingSkills;
    private int wounds;
    private int defense;
    private int mettle;
    private int move;
    private int ranged;
    private int melee;
    private int strength;
    private int spentExperience;
    private int unspentExperience;
    private int advances = 0;
    private int tenPointAdvances = 0;
    private List<Skillset> availableSkillsets;
    private List<UnitTrait> unitTraits;
    private List<Item> inventory;


    //constructor

    public Unit(int id, String faction, String title, String rank, String type, int baseCost, String newPurchaseNote,
                int wounds, int defense, int mettle, int move, int ranged, int melee, int strength,
                List<Skillset> availableSkillsets, List<UnitTrait> unitTraits, int additionalStartingSkills, int spentExperience) {
        this.id = id;
        this.faction = faction;
        this.title = title;
        this.rank = rank;
        this.type = type;
        this.baseCost = baseCost;
        this.newPurchaseNote = newPurchaseNote;
        this.wounds = wounds;
        this.defense = defense;
        this.mettle = mettle;
        this.move = move;
        this.ranged = ranged;
        this.melee = melee;
        this.strength = strength;
        this.availableSkillsets = availableSkillsets;
        this.unitTraits = unitTraits;
        this.additionalStartingSkills = additionalStartingSkills;
        this.spentExperience = spentExperience;
        this.inventory = new ArrayList<Item>();
    }

    public Unit() {
    }

    //Methods


    @Override
    public Unit clone() throws CloneNotSupportedException {
        Unit clonedUnit = (Unit) super.clone();
        clonedUnit.inventory = new ArrayList<Item>();
        clonedUnit.unitTraits = new ArrayList<>(unitTraits);
        return clonedUnit;

    }

    public int getBSCost() {
        int bsCost = baseCost;
        //TODO calculate this after inventory is implemented
        return bsCost;
    }

    public int getUnitUpkeep() {
        int upkeep = 0;

        List<String> skillNames = new ArrayList<>();
        for (UnitTrait unitTrait : unitTraits) {
            skillNames.add(unitTrait.getName());
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


    public int getAdvances() {
        return advances;
    }

    public void setAdvances(int advances) {
        this.advances = advances;
    }

    public int getTenPointAdvances() {
        return tenPointAdvances;
    }

    public void setTenPointAdvances(int tenPointAdvances) {
        this.tenPointAdvances = tenPointAdvances;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return title;
    }

    public void setUnitNickname(String unitNickname) {
        this.unitNickname = unitNickname;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBaseCost(int baseCost) {
        this.baseCost = baseCost;
    }

    public void setNewPurchaseNote(String newPurchaseNote) {
        this.newPurchaseNote = newPurchaseNote;
    }

    public void setAdditionalStartingSkills(int additionalStartingSkills) {
        this.additionalStartingSkills = additionalStartingSkills;
    }

    public void setWounds(int wounds) {
        this.wounds = wounds;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setMettle(int mettle) {
        this.mettle = mettle;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public void setRanged(int ranged) {
        this.ranged = ranged;
    }

    public void setMelee(int melee) {
        this.melee = melee;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setSpentExperience(int spentExperience) {
        this.spentExperience = spentExperience;
    }

    public void setUnspentExperience(int unspentExperience) {
        this.unspentExperience = unspentExperience;
    }

    public void setAvailableSkillsets(List<Skillset> availableSkillsets) {
        this.availableSkillsets = availableSkillsets;
    }

    public void setUnitTraits(List<UnitTrait> unitTraits) {
        this.unitTraits = unitTraits;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public String getUnitNickname() {
        return unitNickname;
    }

    public String getTitle() {
        return title;
    }

    public String getFaction() {
        return faction;
    }

    public String getRank() {
        return rank;
    }

    public String getType() {
        return type;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public String getNewPurchaseNote() {
        return newPurchaseNote;
    }

    public int getWounds() {
        return wounds;
    }

    public int getDefense() {
        return defense;
    }

    public int getMettle() {
        return mettle;
    }

    public int getMove() {
        return move;
    }

    public int getRanged() {
        return ranged;
    }

    public int getMelee() {
        return melee;
    }

    public int getStrength() {
        return strength;
    }

    public int getSpentExperience() {
        return spentExperience;
    }

    public int getUnspentExperience() {
        return unspentExperience;
    }

    public List<Skillset> getAvailableSkillsets() {
        return availableSkillsets;
    }

    public List<UnitTrait> getUnitTraits() {
        return unitTraits;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public int getAdditionalStartingSkills() {
        return additionalStartingSkills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return id == unit.id && baseCost == unit.baseCost && additionalStartingSkills == unit.additionalStartingSkills
                && wounds == unit.wounds && defense == unit.defense && mettle == unit.mettle && move == unit.move
                && ranged == unit.ranged && melee == unit.melee && strength == unit.strength
                && spentExperience == unit.spentExperience && unspentExperience == unit.unspentExperience
                && Objects.equals(title, unit.title) && Objects.equals(faction, unit.faction)
                && Objects.equals(rank, unit.rank)
                && Objects.equals(type, unit.type) && Objects.equals(newPurchaseNote, unit.newPurchaseNote)
                && new HashSet<>(unit.getAvailableSkillsets()).containsAll(availableSkillsets);
    }


}