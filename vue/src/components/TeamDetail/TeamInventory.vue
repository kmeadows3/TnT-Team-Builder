<template>
    <h3>Team Inventory</h3>
    <div class="item-table">
            <div class="table-label item-list">
                <div class="item-med">Type</div>
                <div class="item-small">BS Cost</div>
                <div class="item-large">Special Rules</div>
                <div class="item-small">Relic</div>
                <div class="item-small">Support</div>
                <div class="item-small" v-if="($store.state.currentUnit.id && $store.state.manageUnitInventory) || (!$store.state.currentUnit.id && $store.state.manageTeamInventory)">
                    Actions</div>
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
                    <i class="bi bi-check-circle" title="Relic" v-if="item.relic"></i>
                    <i class="bi bi-x-circle" v-if="!item.relic"></i>

                </div>
                <div class="item-small">
                    <i class="bi bi-check-circle" title="Support" v-if="item.category=='Support Weapon'"></i>
                    <i class="bi bi-x-circle" v-if="!(item.category=='Support Weapon')"></i>

                </div>
                <ItemActions class ="item-small" :item='item'/>
            </div>
        </div>


    <InventoryActions/>
</template>

<script>
import InventoryActions from '../InventoryShared/InventoryActions.vue';
import ItemActions from '../InventoryShared/ItemActions.vue';

export default {
    data () {
        return {
            sortedInventory: []
        }
    },
    components: {
        InventoryActions,
        ItemActions
    },
    methods: {
        sortInventory() {
            let inventory = this.$store.state.currentTeam.inventory;
            this.sortedInventory = inventory.sort( (a,b) => a.name.localeCompare(b.name)).sort( (a,b) => {
                if (a.relic == true && b.relic == false){
                    return -1;
                } else if (b.relic == true && a.relic == false){
                    return 1;
                }

                return 0;
            });
        }
    },
    beforeMount() {
        this.sortInventory();
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
    align-content: baseline;
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
}

div.item-list>.item-large {
    min-width: 150px;
    flex-grow: 5;
    flex-basis: 50%;
}
</style>