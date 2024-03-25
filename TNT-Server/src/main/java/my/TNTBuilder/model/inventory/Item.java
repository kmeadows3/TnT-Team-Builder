package my.TNTBuilder.model.inventory;

import java.util.List;
import java.util.Objects;

public abstract class Item {
    private int id;
    private String name;
    private int cost;
    private String specialRules;
    private List<ItemTrait> itemTraits;
    private String Rarity;
    private boolean isRelic;

    //Constructor
    public Item(int id, String type, int cost, String specialRules, List<ItemTrait> itemTraits, String rarity, boolean isRelic) {
        this.id = id;
        this.name = type;
        this.cost = cost;
        this.specialRules = specialRules;
        this.itemTraits = itemTraits;
        Rarity = rarity;
        this.isRelic = isRelic;
    }

    public Item() {
    }

    //Getters


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
        return Rarity;
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
        Rarity = rarity;
    }

    public void setRelic(boolean relic) {
        isRelic = relic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item that = (Item) o;
        return id == that.id && cost == that.cost && isRelic == that.isRelic && name.equals(that.name) &&
                specialRules.equals(that.specialRules) && Objects.equals(itemTraits, that.itemTraits) &&
                Rarity.equals(that.Rarity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cost, specialRules, itemTraits, Rarity, isRelic);
    }
}
