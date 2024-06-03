<template>
    <div class='unit-inventory-actions' v-show="!buyItems && !$store.state.manageUnitInventory">
        <button @click="toggleBuyItems()">Add Item</button>
        <button @click="toggleManageInventory()">Manage Inventory</button>
    </div>
    <BuyItems v-show="buyItems" />
    <div class='unit-inventory-actions' v-show="buyItems || $store.state.manageUnitInventory">
        <button @click="resetActions()">Cancel</button>
    </div>


</template>
<script>
import BuyItems from '../../InventoryShared/BuyItems.vue';


export default {
    components: {
        BuyItems
    },
    data() {
        return {
            buyItems: false,
        }
    },
    methods: {
        toggleBuyItems() {
            this.buyItems = !this.buyItems;
        },
        toggleManageInventory() {
            this.$store.commit('SET_MANAGE_UNIT_INVENTORY', true);
        },
        resetActions() {
            this.buyItems = false;
            this.$store.commit('SET_MANAGE_UNIT_INVENTORY', false);
        }
    }
}

</script>


<style scoped>
div.unit-inventory-actions {
    padding: 10px;
}
</style>