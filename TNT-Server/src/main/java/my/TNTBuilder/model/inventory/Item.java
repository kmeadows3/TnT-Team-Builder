package my.TNTBuilder.model.inventory;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import my.TNTBuilder.exception.ValidationException;
import my.TNTBuilder.model.Unit;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;


@JsonTypeInfo(use = Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
@JsonSubTypes({
        @Type(value = Armor.class),
        @Type(value = Weapon.class),
})

public class Item {
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
    private boolean isEquipped;


    //Constructor
    public Item(int id, int referenceId, String type, int cost, String specialRules, List<ItemTrait> itemTraits,
                String rarity, boolean isRelic, int handsRequired, String category, boolean isEquipped) {
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
        this.isEquipped = isEquipped;
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

    //Validation Methods

    private boolean validateEquipItem(Unit unit) throws ValidationException{
        int handsUsed = unit.getInventory().stream().filter( (item) ->  item.isEquipped ).mapToInt(Item::getHandsRequired).sum();
        handsUsed += this.handsRequired;

        validateUnitNotWearing2SetsOfArmor(unit);

        //TODO: Add logic here once mutations are added
        boolean cannotHoldItems = false; //Weapons Growth (2 natural weapons), Crushing Claws
        boolean canOnlyHoldOneItem = false; //No Arms, Weapons Growth (one natural weapon)

        if (cannotHoldItems){
            throw new ValidationException("Unit is incapable of holding items.");
        } else if (handsUsed >= 2 && canOnlyHoldOneItem) {
            throw new ValidationException("Unit can only equip a single 1-handed item.");
        } else if (handsUsed > 2){
            throw new ValidationException("Unit's hands are full, cannot equip any more items.");
        }

        return true;

    }

    private void validateUnitNotWearing2SetsOfArmor(Unit unit) throws ValidationException {

        if (this.category.equals("Armor") && !( ((Armor)this).isShield()) ){

            int armorWorn = (int) unit.getInventory().stream()
                    .filter( (item) -> item.isEquipped && item.category.equals("Armor") && !((Armor)item).isShield())
                    .count();

            if(armorWorn > 0){
                throw new ValidationException("Unit is already wearing armor.");
            }
        }
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

    public boolean isEquipped() {
        return isEquipped;
    }

    public void setEquipped(boolean equipped) {
        isEquipped = equipped;
    }

    public void setEquippedValidated(boolean equipped, Unit unit) throws ValidationException {

        if (!equipped || this.validateEquipItem(unit)){
            isEquipped = equipped;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && referenceId == item.referenceId && cost == item.cost && isRelic == item.isRelic
                && isEquipped == item.isEquipped && handsRequired == item.handsRequired
                && Objects.equals(name, item.name)
                && Objects.equals(specialRules, item.specialRules) && Objects.equals(rarity, item.rarity)
                && Objects.equals(category, item.category)
                && new HashSet<>(itemTraits).containsAll(item.itemTraits)
                && new HashSet<>(item.itemTraits).containsAll(itemTraits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, referenceId, name, cost, specialRules, itemTraits, rarity, isRelic, handsRequired, category);
    }
}
