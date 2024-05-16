package my.TNTBuilder.model.inventory;

import my.TNTBuilder.model.Unit;

import java.util.List;
import java.util.Objects;

public abstract class Item {
    private int id;
    private int referenceId;
    private String name;
    private int cost;
    private String specialRules;
    private List<ItemTrait> itemTraits;
    private String rarity;
    private boolean isRelic;
    private int handsRequired;
    private String category;


    //Constructor
    public Item(int id, int referenceId, String type, int cost, String specialRules, List<ItemTrait> itemTraits,
                String rarity, boolean isRelic, int handsRequired, String category) {
        this.id = id;
        this.referenceId = referenceId;
        this.name = type;
        this.cost = cost;
        this.specialRules = specialRules;
        this.itemTraits = itemTraits;
        this.rarity = rarity;
        this.isRelic = isRelic;
        this.handsRequired = handsRequired;
        this.category = category;
    }

    public Item() {
    }

    public int calculateCostToPurchase(Unit unit) {
        int cost = this.cost;
        if (this.category.equals("Armor")){
            if (unit.getWounds() == 2) {
                cost = ((Armor) this).getCost2Wounds();
            } else if (unit.getWounds() > 2) {
                cost = ((Armor) this).getCost3Wounds();
            }
        }
        return cost;
    }

    //Getters


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getHandsRequired() {
        return handsRequired;
    }

    public void setHandsRequired(int handsRequired) {
        this.handsRequired = handsRequired;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getSpecialRules() {
        return specialRules;
    }

    public List<ItemTrait> getItemTraits() {
        return itemTraits;
    }

    public String getRarity() {
        return rarity;
    }

    public boolean isRelic() {
        return isRelic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setSpecialRules(String specialRules) {
        this.specialRules = specialRules;
    }

    public void setItemTraits(List<ItemTrait> itemTraits) {
        this.itemTraits = itemTraits;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public void setRelic(boolean relic) {
        isRelic = relic;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && referenceId == item.referenceId && cost == item.cost && isRelic == item.isRelic
                && handsRequired == item.handsRequired && Objects.equals(name, item.name)
                && Objects.equals(specialRules, item.specialRules) && Objects.equals(itemTraits, item.itemTraits)
                && Objects.equals(rarity, item.rarity) && Objects.equals(category, item.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, referenceId, name, cost, specialRules, itemTraits, rarity, isRelic, handsRequired, category);
    }
}
