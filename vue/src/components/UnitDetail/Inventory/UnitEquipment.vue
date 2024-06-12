<template>
    <div class="item-container" v-show="equipments.length > 0">
        <h2 class="subsection-title">Equipment</h2>
        <div class="item-table">
            <div class="table-label item-list">
                <div class="equipment-med">Type</div>
                <div class="equipment-small">Cost</div>
                <div class="equipment-large">Special Rules</div>
                <div class="equipment-small">Equipped</div>
                <div class="item-action" v-if="$store.state.manageInventory">
                    Actions</div>
            </div>
            <div class="item-list" v-for="equipment in equipments" :key="'equipment' + equipment.id">
                <div class="equipment-med">{{ equipment.name }}</div>
                <div class="equipment-small">{{ equipment.cost }}</div>
                <div class="equipment-large item-special-rules">
                    <span v-show="equipment.itemTraits.length == 0 || equipment.specialRules != 'N/A'">
                        {{ equipment.specialRules }}<span v-show="equipment.itemTraits.length > 0">, </span>
                    </span>
                    <span v-show="equipment.itemTraits.length > 0">
                        <span v-for="(trait, index) in equipment.itemTraits" :key="'trait' + equipment.id + trait.id">
                            <span v-show="index != 0">,</span>
                            {{ trait.name }}</span>
                    </span>
                </div>
                <div class="equipment-small item-check">
                    <i class="bi bi-check-circle" title="Currently Equipped" v-show="equipment.equipped"></i>
                    <i class="bi bi-x-circle" title="Currently Unequipped" v-show="!equipment.equipped"></i>
                </div>
                <ItemActions class ="item-action" :item='equipment'/>
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
        equipments() {
            return this.$store.state.currentUnit.inventory.filter(item => item.category == "Equipment");
        }
    }
}
</script>

<style scoped>

div.item-list>.equipment-small {
    min-width: 75px;
    flex-grow: 1;
    flex-basis: 5%;
    display: flex;
    justify-content: center;
    align-content: baseline;
}

div.item-list>.equipment-med {
    min-width: 75px;
    flex-grow: 1;
    flex-basis: 15%;
}

div.item-list>.equipment-large {
    min-width: 150px;
    flex-grow: 5;
    flex-basis: 50%;
}
</style>