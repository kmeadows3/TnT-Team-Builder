<template>
    <h1 class="section-title">Team Inventory</h1>
    <div class="item-table" v-show="$store.state.currentTeam.inventory.length != 0">
        <div class="table-label item-list">
            <div class="item-med">Type</div>
            <div class="item-small">BS Cost</div>
            <div class="item-large">Special Rules</div>
            <div class="item-small">Relic</div>
            <div class="item-small">Support</div>
            <div class="item-action" v-if="$store.state.manageInventory"> Actions</div>
        </div>
        <div class="item-list" v-for="item in sortedInventory" :key="'teamInventory' + item.id">
            <div class="item-med">{{ item.name }}</div>
            <div class="item-small">{{ item.cost }}</div>
            <div class="item-large">
                <span v-show="item.itemTraits.length == 0 || item.specialRules != 'N/A'">
                    {{ item.specialRules }}<span v-show="item.itemTraits.length > 0">, </span>
                </span>
                <span v-show="item.itemTraits.length > 0">
                    <span v-for="(trait, index) in item.itemTraits" :key="'trait' + item.id + trait.id">
                        <span v-show="index != 0">,</span>
                        {{ trait.name }}</span>
                </span>
            </div>
            <div class="item-small">
                <i class="bi bi-check-circle" title="Relic" v-show="item.relic"></i>
                <i class="bi bi-x-circle" v-show="!item.relic"></i>

            </div>
            <div class="item-small">
                <i class="bi bi-check-circle" title="Support" v-show="item.category == 'Support Weapon'"></i>
                <i class="bi bi-x-circle" v-show="!(item.category == 'Support Weapon')"></i>

            </div>
            <ItemActions class="item-action" :item='item' />
        </div>
    </div>
    <div v-show="!$store.state.currentTeam.inventory.length != 0">
        <span>The team has no unassigned items.</span>
    </div>

    <InventoryActions />

</template>

<script>
import InventoryActions from '../InventoryShared/InventoryActions.vue';
import ItemActions from '../InventoryShared/ItemActions.vue';

export default {
    components: {
        InventoryActions,
        ItemActions
    },
    computed: {
        sortedInventory() {
            let inventory = this.$store.state.currentTeam.inventory;
            inventory = inventory.sort((a, b) => a.name.localeCompare(b.name)).sort((a, b) => {
                if (a.relic == true && b.relic == false) {
                    return -1;
                } else if (b.relic == true && a.relic == false) {
                    return 1;
                }

                return 0;
            });
            return inventory;
        }
    }
}
</script>


<style scoped>
div.item-list>.item-small {
    min-width: 75px;
    flex-grow: 1;
    flex-basis: 3%;
    display: flex;
    justify-content: center;
    align-items: center;
}

div.item-list>.item-action {
    min-width: 200px;
    flex-grow: 1;
    flex-basis: 3%;
    display: flex;
    justify-content: center;
    align-items: center;
}

div.item-list>.item-small>.bi {
    border: 0px;
}

div.item-list>.item-small>.bi-check-circle {
    color: green;
}

div.item-list>.item-small>.bi-x-circle {
    color: red;
}

div.item-list>.item-med {
    min-width: 75px;
    flex-grow: 1;
    flex-basis: 15%;
    align-content: center;
}

div.item-list>.item-large {
    min-width: 150px;
    flex-grow: 5;
    flex-basis: 50%;
}
</style>