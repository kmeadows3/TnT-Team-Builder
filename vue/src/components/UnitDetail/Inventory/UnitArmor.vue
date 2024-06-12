<template>
    <div class="item-container" v-show="armors.length > 0">
        <h2 class="subsection-title">Armor</h2>
        <div class="item-table">
            <div class="table-label item-list">
                <div class="armor-med">Type</div>
                <div class="armor-small">Cost</div>
                <div class="armor-small">Melee Defense</div>
                <div class="armor-small">Ranged Defense</div>
                <div class="armor-large">Special Rules</div>
                <div class="armor-small">Equipped</div>
                <div class="item-action" v-if="$store.state.manageInventory">
                    Actions</div>
            </div>
            <div class="item-list" v-for="armor in armors" :key="'armor' + armor.id">
                <div class="armor-med">{{ armor.name }}</div>
                <div class="armor-small">{{ $store.state.currentUnit.wounds == 1 ? armor.cost :
                    ($store.state.currentUnit.wounds == 2 ? armor.cost2Wounds : armor.cost3Wounds) }}</div>
                <!--TODO: Logic around armor bonuses once items can be equipped-->
                <div class="armor-small"> {{ armor.meleeDefenseBonus > 0 ? "+"+armor.meleeDefenseBonus : "No Bonus" }}</div>
                <div class="armor-small">{{ armor.rangedDefenseBonus > 0 ? "+"+armor.rangedDefenseBonus : "0" }} </div>
                <div class="armor-large item-special-rules">
                    <span v-show="armor.itemTraits.length == 0 || armor.specialRules != 'N/A'">
                        {{ armor.specialRules }}<span v-show="armor.itemTraits.length > 0">, </span>
                    </span>
                    <span v-show="armor.itemTraits.length > 0">
                        <span v-for="(trait, index) in armor.itemTraits" :key="'trait' + armor.id + trait.id">
                            <span v-show="index != 0">,</span>
                            {{ trait.name }}</span>
                    </span>
                </div>
                <div class="armor-small item-check"> 
                    <i class="bi bi-check-circle" title="Currently Equipped" v-show="armor.equipped"></i>
                    <i class="bi bi-x-circle" title="Currently Unequipped" v-show="!armor.equipped"></i>
                </div>
                <ItemActions class ="item-action" :item='armor'/>
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
div.item-list>.armor-small {
    min-width: 75px;
    flex-grow: 1;
    flex-basis: 5%;
    display: flex;
    justify-content: center;
    align-content: baseline;
}

div.item-list>.armor-med {
    min-width: 80px;
    flex-grow: 2;
    flex-basis: 15%;
}

div.item-list>.armor-large {
    min-width: 150px;
    flex-grow: 5;
    flex-basis: 50%;
}

</style>