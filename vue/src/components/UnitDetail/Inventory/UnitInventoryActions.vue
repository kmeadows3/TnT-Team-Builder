<template>
    <div class='unit-inventory-actions' v-show="!buyItems && !manageCurrentInventory">
        <button @click="toggleBuyItems()">Add Item</button>
        <button>Manage Inventory</button>
    </div>
    <BuyItems v-show="buyItems" @updateItems="() => $emit('updateTraits')" />
    <div class='unit-inventory-actions' v-show="buyItems || manageCurrentInventory">
        <button @click="resetActions()">Cancel</button>
    </div>


</template>
<script>
import BuyItems from './BuyItems.vue';


export default {
    emits: ['updateTraits'],
    components: {
        BuyItems
    },
    data() {
        return {
            buyItems: false,
            manageCurrentInventory: false,
        }
    },
    methods: {
        toggleBuyItems() {
            this.buyItems = !this.buyItems;
        },
        resetActions() {
            this.buyItems = false;
            this.manageCurrentInventory = false;
        }
    }
}

</script>


<style scoped>
div.unit-inventory-actions {
    padding: 10px;
}
</style>