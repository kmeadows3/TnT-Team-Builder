package my.TNTBuilder.model.inventory;

import my.TNTBuilder.model.Skill;

import java.util.List;
import java.util.Objects;

public class Armor extends Item {
    private int meleeDefenseBonus;
    private int rangedDefenseBonus;
    private boolean isShield;
    private int cost2Wounds;
    private int cost3Wounds;


    //Constructor

    public Armor(int id, int referenceId, String type, int cost, String specialRules, List<ItemTrait> itemTraits,
                 String rarity, boolean isRelic, int meleeDefenseBonus, int rangedDefenseBonus, boolean isShield,
                 int cost2Wounds, int cost3Wounds, int handsRequired, String category, boolean isEquipped, Skill grants) {
        super(id, referenceId, type, cost, specialRules, itemTraits, rarity, isRelic, handsRequired, category, isEquipped, grants);
        this.meleeDefenseBonus = meleeDefenseBonus;
        this.rangedDefenseBonus = rangedDefenseBonus;
        this.isShield = isShield;
        this.cost2Wounds = cost2Wounds;
        this.cost3Wounds = cost3Wounds;
    }

    public Armor() {
    }


//Getters and Setters

    public void setMeleeDefenseBonus(int meleeDefenseBonus) {
        this.meleeDefenseBonus = meleeDefenseBonus;
    }

    public void setRangedDefenseBonus(int rangedDefenseBonus) {
        this.rangedDefenseBonus = rangedDefenseBonus;
    }

    public void setShield(boolean shield) {
        isShield = shield;
    }

    public void setCost2Wounds(int cost2Wounds) {
        this.cost2Wounds = cost2Wounds;
    }

    public void setCost3Wounds(int cost3Wounds) {
        this.cost3Wounds = cost3Wounds;
    }

    public int getMeleeDefenseBonus() {
        return meleeDefenseBonus;
    }

    public int getRangedDefenseBonus() {
        return rangedDefenseBonus;
    }

    public boolean isShield() {
        return isShield;
    }

    public int getCost2Wounds() {
        return cost2Wounds;
    }

    public int getCost3Wounds() {
        return cost3Wounds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Armor armor = (Armor) o;
        return meleeDefenseBonus == armor.meleeDefenseBonus && rangedDefenseBonus == armor.rangedDefenseBonus
                && isShield == armor.isShield && cost2Wounds == armor.cost2Wounds && cost3Wounds == armor.cost3Wounds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), meleeDefenseBonus, rangedDefenseBonus, isShield, cost2Wounds, cost3Wounds);
    }
}
