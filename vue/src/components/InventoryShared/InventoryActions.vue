<template>
    <div class='unit-inventory-actions button-container' v-show="!$store.state.manageInventory">
        <button @click="toggleBuyItems()">Add Item</button>
        <button @click="toggleManageInventory()">Manage Inventory</button>
    </div>
    <div class='unit-inventory-actions button-container' v-show="$store.state.manageInventory">
        <button @click="resetActions()">Cancel</button>
    </div>


</template>
<script>

export default {
    methods: {
        toggleBuyItems() {
            this.$store.commit('SET_SHOW_BUY_ITEMS', true);
            this.$store.commit('TOGGLE_SHOW_POPUP');
        },
        toggleManageInventory() {
            this.$store.commit('SET_MANAGE_INVENTORY', true);
        },
        resetActions() {
            this.$store.commit('SET_SHOW_BUY_ITEMS', false);
            this.$store.commit('SET_MANAGE_INVENTORY', false);
        }
    }
}

</script>
