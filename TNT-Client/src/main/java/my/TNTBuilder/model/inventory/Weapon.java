package my.TNTBuilder.model.inventory;

import java.util.List;
import java.util.Objects;

public class Weapon extends Item{
    private int meleeRange;
    private int rangedRange;
    private int strength;
    private int reliability;
    private int handsRequired;
    private String Category;

    //Constructor


    public Weapon(int id, String type, int cost, String specialRules, List<ItemTrait> itemTraits, String rarity, boolean isRelic,
                  int meleeRange, int rangedRange, int strength, int reliability, int handsRequired, String category) {
        super(id, type, cost, specialRules, itemTraits, rarity, isRelic);
        this.meleeRange = meleeRange;
        this.rangedRange = rangedRange;
        this.strength = strength;
        this.reliability = reliability;
        this.handsRequired = handsRequired;
        Category = category;
    }

    public Weapon() {
    }

    //Getters

    public void setMeleeRange(int meleeRange) {
        this.meleeRange = meleeRange;
    }

    public void setRangedRange(int rangedRange) {
        this.rangedRange = rangedRange;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setReliability(int reliability) {
        this.reliability = reliability;
    }

    public void setHandsRequired(int handsRequired) {
        this.handsRequired = handsRequired;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getMeleeRange() {
        return meleeRange;
    }

    public int getRangedRange() {
        return rangedRange;
    }

    public int getStrength() {
        return strength;
    }

    public int getReliability() {
        return reliability;
    }

    public int getHandsRequired() {
        return handsRequired;
    }

    public String getCategory() {
        return Category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Weapon weapon = (Weapon) o;
        return meleeRange == weapon.meleeRange && rangedRange == weapon.rangedRange && strength == weapon.strength &&
                reliability == weapon.reliability && handsRequired == weapon.handsRequired && Category.equals(weapon.Category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), meleeRange, rangedRange, strength, reliability, handsRequired, Category);
    }
}