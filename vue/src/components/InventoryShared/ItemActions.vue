<template>
    <div v-if="$store.state.manageInventory">

        <i class="bi inventory-icon bi-coin" title="Sell"></i>
        <i class="bi inventory-icon bi-trash" title="Delete"></i>
        <i class="bi inventory-icon bi-arrow-left-right" title="Transfer to/from Team Inventory" @click="transferButton()"></i>

        <span v-show="!$store.state.currentUnit.id">
            <select :id="'unitTransferSelect' + item.id" v-model.number="transferTarget">
                <option value="-1">Transfer to: </option>
                <option v-for="unit in $store.state.currentTeam.unitList" :key="'transferTarget' + unit.id"
                    :value="unit.id">{{ unit.name }}</option>
            </select>
        </span>
    </div>
</template>

<script>
import ItemService from '../../services/ItemService';

export default {
    props: ['item'],
    data() {
        return {
            transferTarget: -1
        }
    },
    methods: {
        transferButton() {
            if (this.$store.state.currentUnit.id) {
                this.transferItemFromUnit();
            } else {
                this.transferItemToUnit();
            }
        },
        transferItemToUnit(){
            if (this.transferTarget != -1){
                ItemService.transferItem(this.item.id, this.transferTarget)
                .then(() => this.$store.dispatch('reloadCurrentTeam'))
                .catch(error => this.$store.dispatch('showError', error))
            } else {
                this.$store.dispatch('showError', "You must select which unit will be receiving the item.")
            }
        },
        transferItemFromUnit() {
            ItemService.transferItem(this.item.id, this.$store.state.currentUnit.id)
                .then(() => this.$store.dispatch('reloadCurrentUnit'))
                .catch(error => this.$store.dispatch('showError', error))
        },

    }
}


</script>

<style>
i {
    margin: 2px;
    height: max-content;
}
</style>
