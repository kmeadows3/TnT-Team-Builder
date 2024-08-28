<template>
    <div class='inventory-actions button-container'>
        <button @click="toggleBuyItems()">Add Item</button>
        <button @click="toggleManageInventory()" v-show="viewManageInventory">Manage Inventory</button>
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
    },
    computed: {
        viewManageInventory() {
            if (!this.$store.state.manageInventory){
                if ( (this.$store.state.currentUnit.id && this.$store.state.currentUnit.inventory.length > 0) 
                    || (this.$store.state.currentTeam.inventory.length > 0)){
                    return true;
                }
            }
            return false;
        }
    }
}

</script>