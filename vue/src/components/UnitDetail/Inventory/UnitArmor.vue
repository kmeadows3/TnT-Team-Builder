<template>
    <div class="item-container" v-show="armors.length > 0">
        <h2 class="subsection-title">Armor</h2>
        <div class="item-table">
            <div class="table-label item-list armor-grid">
                <div class="item-name">Type</div>
                <div class="armor-cost">Cost</div>
                <div class="armor-mDef">Melee Defense</div>
                <div class="armor-rDef">Ranged Defense</div>
                <div class="armor-equipped">Equipped</div>
                <div class="item-action armor-action" v-if="$store.state.manageInventory">
                    Actions</div>
            </div>
            <div class="item-list armor-grid" v-for="armor in armors" :key="'armor' + armor.id">
                <div class="item-name">{{ armor.name }}</div>
                <div class="armor-cost">{{ $store.state.currentUnit.wounds == 1 ? armor.cost :
                    ($store.state.currentUnit.wounds == 2 ? armor.cost2Wounds : armor.cost3Wounds) }}</div>
                <!--TODO: Logic around armor bonuses once items can be equipped-->
                <div class="armor-mDef"> {{ armor.meleeDefenseBonus > 0 ? "+"+armor.meleeDefenseBonus : "No Bonus" }}</div>
                <div class="armor-rDef">{{ armor.rangedDefenseBonus > 0 ? "+"+armor.rangedDefenseBonus : "0" }} </div>
                <div class="armor-rules item-special-rules">
                    <span v-show="armor.itemTraits.length == 0 || armor.specialRules != 'N/A'">
                        {{ armor.specialRules }}<span v-show="armor.itemTraits.length > 0">, </span>
                    </span>
                    <span v-show="armor.itemTraits.length > 0">
                        <span v-for="(trait, index) in armor.itemTraits" :key="'trait' + armor.id + trait.id">
                            <span v-show="index != 0">,</span>
                            {{ trait.name }}</span>
                    </span>
                </div>
                <div class="armor-equipped item-check"> 
                    <i class="bi bi-check-circle" title="Currently Equipped" v-show="armor.equipped"></i>
                    <i class="bi bi-x-circle" title="Currently Unequipped" v-show="!armor.equipped"></i>
                </div>
                <ItemActions class ="item-action armor-action" :item='armor'/>
            </div>
        </div>
    </div>
</template>

<script>
import ItemActions from '../../InventoryShared/ItemActions.vue';

export default {
    components: {
        ItemActions
    },
    computed: {
        armors() {
            return this.$store.state.currentUnit.inventory.filter(item => item.category == "Armor");
        }
    }
}
</script>

<style scoped>
div.table-label.armor-grid{
    display: grid;
    grid-template-areas:  "name  cost  melee ranged equipped action";
    grid-template-columns: 2fr 1fr 1fr 1fr 1fr;

}

div.item-list.armor-grid{
    display: grid;
    grid-template-areas:  "name  cost   melee ranged equipped action"
                          "name  rules  rules rules  rules    rules";
    grid-template-columns: 4fr 3fr 3fr 3fr 3fr;
}


div.item-list.armor-grid>div.item-name{
    grid-area: name;
}

div.item-list.armor-grid>div.armor-cost{
    grid-area: cost;
}
div.item-list.armor-grid>div.armor-mDef{
    grid-area: melee;
}
div.item-list.armor-grid>div.armor-rDef{
    grid-area: ranged;
}
div.item-list.armor-grid>div.armor-equipped{
    grid-area: equipped;
}
div.item-list.armor-grid>div.armor-action{
    grid-area: action;
}
div.item-list.armor-grid>div.armor-rules{
    grid-area: rules;
}

</style>