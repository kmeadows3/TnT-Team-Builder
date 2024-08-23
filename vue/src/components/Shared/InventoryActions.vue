<template>
    <div class='inventory-actions button-container'>
        <button @click="toggleBuyItems()">Add Item</button>
        <button @click="toggleManageInventory()" v-show=" !$store.state.manageInventory && $store.state.currentTeam.inventory.length > 0">Manage Inventory</button>
        <button @click="resetActions()" v-show="$store.state.manageInventory">Close Inventory Actions</button>        
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