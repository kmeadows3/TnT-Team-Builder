<template>
    <div class="item-container" v-show="armors.length > 0">
        <h3>Armor</h3>
        <div class="item-table">
            <div class="table-label item-list">
                <div class="armor-med">Type</div>
                <div class="armor-small">Cost</div>
                <div class="armor-med">Defense Bonus (melee/ranged)</div>
                <div class="armor-large">Special Rules</div>
                <div class="armor-small" v-if="$store.state.manageInventory">
                    Actions</div>
            </div>
            <div class="item-list" v-for="armor in armors" :key="'armor' + armor.id">
                <div class="armor-med">{{ armor.name }}</div>
                <div class="armor-small">{{ $store.state.currentUnit.wounds == 1 ? armor.cost :
                    ($store.state.currentUnit.wounds == 2 ? armor.cost2Wounds : armor.cost3Wounds) }}</div>
                <!--TODO: Logic around armor bonuses once items can be equipped-->
                <div class="armor-med">{{ armor.meleeDefenseBonus }} / {{ armor.rangedDefenseBonus }}</div>
                <div class="armor-large">
                    <span v-show="armor.itemTraits.length == 0 || armor.specialRules != 'N/A'">
                        {{ armor.specialRules }}<span v-show="armor.itemTraits.length > 0">, </span>
                    </span>
                    <span v-show="armor.itemTraits.length > 0">
                        <span v-for="(trait, index) in armor.itemTraits" :key="'trait' + armor.id + trait.id">
                            <span v-show="index != 0">,</span>
                            {{ trait.name }}</span>
                    </span>
                </div>
                <ItemActions class ="armor-small" :item='armor'/>
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
    min-width: 75px;
    flex-grow: 1;
    flex-basis: 15%;
}

div.item-list>.armor-large {
    min-width: 150px;
    flex-grow: 5;
    flex-basis: 33%;
}
</style>