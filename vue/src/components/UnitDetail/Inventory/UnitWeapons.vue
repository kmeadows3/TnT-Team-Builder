<template>
    <div class="item-container" v-show="weapons.length > 0">
        <h2 class="subsection-title">Weapons</h2>
        <div class="item-table">
            <div class="item-list table-label">
                <div class="weapon-med">Type</div>
                <div class="weapon-small">Cost</div>
                <div class="weapon-small">Range </div>
                <div class="weapon-small">Strength</div>
                <div class="weapon-small">Reliability</div>
                <div class="weapon-small">Hands</div>
                <div class="weapon-large">Special Rules</div>
                <div class="weapon-small">Equipped</div>
                <div class="item-action" v-if="$store.state.manageInventory">
                    Actions</div>
            </div>
            <div class="item-list" v-for="weapon in weapons" :key="'weapon' + weapon.id">
                <div class="weapon-med">{{ weapon.name }}</div>
                <div class="weapon-small">{{ weapon.cost }}</div>
                <div class="weapon-small">{{ weapon.meleeRange }}"/ {{ weapon.rangedRange }}"</div>
                <div class="weapon-small">{{ weapon.strength }}</div>
                <div class="weapon-small">{{ weapon.reliability }}</div>
                <div class="weapon-small">{{ weapon.handsRequired }}</div>
                <div class="weapon-large item-special-rules">
                    <span v-show="weapon.itemTraits.length == 0 || weapon.specialRules != 'N/A'">
                        {{ weapon.specialRules }}<span v-show="weapon.itemTraits.length > 0">, </span>
                    </span>
                    <span v-show="weapon.itemTraits.length > 0">
                        <span v-for="(trait, index) in weapon.itemTraits" :key="'trait' + weapon.id + trait.id">
                            <span v-show="index != 0">,</span>
                            {{ trait.name }}</span>
                    </span>
                </div>
                <div class="weapon-small item-check">
                    <i class="bi bi-check-circle" title="Currently Equipped" v-show="weapon.equipped"></i>
                    <i class="bi bi-x-circle" title="Currently Unequipped" v-show="!weapon.equipped"></i>
                </div>
                <ItemActions class="item-action" :item="weapon"/>
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
        weapons() {
            return this.$store.state.currentUnit.inventory.filter(item => item.category != "Armor"  && item.category != "Equipment");
        }
    }
}
</script>

<style scoped>

div.item-list>.weapon-small {
    min-width: 50px;
    flex-grow: 1;
    flex-basis: 7%;
}

div.item-list>.weapon-med {
    min-width: 75px;
    flex-grow: 1;
    flex-basis: 15%;
}

div.item-list>.weapon-large {
    min-width: 150px;
    flex-grow: 5;
    flex-basis: 50%;
}


</style>