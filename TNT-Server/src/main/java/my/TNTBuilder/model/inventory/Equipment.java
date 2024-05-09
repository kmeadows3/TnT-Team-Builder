package my.TNTBuilder.model.inventory;

import java.util.List;

public class Equipment extends Item{
    public Equipment(int id, int referenceId, String type, int cost, String specialRules, List<ItemTrait> itemTraits,
                     String rarity, boolean isRelic, int handsRequired, String category) {
        super(id, referenceId, type, cost, specialRules, itemTraits, rarity, isRelic, handsRequired, category);
    }

    public Equipment() {
    }


}
