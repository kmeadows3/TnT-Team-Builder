package my.TNTBuilder.model.inventory;

import java.util.List;

public class Equipment extends Item{
    public Equipment(int id, String type, int cost, String specialRules, List<ItemTrait> itemTraits, String rarity, boolean isRelic) {
        super(id, type, cost, specialRules, itemTraits, rarity, isRelic);
    }

    public Equipment() {
    }

}
