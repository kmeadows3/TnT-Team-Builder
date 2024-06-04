<template>
    <div v-if="($store.state.currentUnit.id && $store.state.manageUnitInventory) || (!$store.state.currentUnit.id && $store.state.manageTeamInventory)">
        <i class="bi inventory-icon bi-cash" title="Sell"></i>
        <i class="bi inventory-icon bi-trash" title="Delete"></i>
        <i class="bi inventory-icon bi-arrow-left-right" title="Transfer" @click="transferItem()"></i>
    </div>
</template>

<script>
import ItemService from '../../services/ItemService';

export default {
    props: ['item'],
    methods: {
        transferItem(){
            ItemService.transferItem(this.item.id, this.$store.state.currentUnit.id)
                .then( () => this.$store.dispatch('reloadCurrentUnit'))
                .catch(error => this.$store.dispatch('showHttpError', error))
        }
    }
}


</script>

<style>
i {
    margin: 2px;
    height: max-content;
}
</style>


