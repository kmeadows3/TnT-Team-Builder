<template>
        <div class ='unit-inventory-actions' v-show="!buyItems && !manageCurrentInventory">
            <button >Buy Items</button>
            <button >Manage Inventory</button>
        </div>



</template>
<script>

export default {
    data() {
        return {
            buyItems: false,
            manageCurrentInventory: false,
        }
    }
}

</script>


<style>

div.unit-inventory-actions {
    padding: 10px;
}

</style>