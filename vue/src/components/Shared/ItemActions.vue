<template>
    <div v-if="$store.state.manageInventory">
        <i class="bi inventory-icon bi-explicit-fill button" v-show="$store.state.showUnitDetail" 
            title="Equip" @click="equipItem()"></i>
        <i class="bi inventory-icon bi-capslock-fill button" @click="upgrade()"
        v-show="$store.state.showUnitDetail && canUpgrade"></i>

        <i class="bi inventory-icon bi-currency-dollar button" title="Sell" @click="sellItem()"></i>
        <i class="bi inventory-icon bi-trash button" title="Delete" @click="deleteItem()"></i>
        <i class="bi inventory-icon bi-arrow-left-right button" title="Transfer to/from Team Inventory" 
            @click="transferButton()"></i>


        <span v-show="!$store.state.showUnitDetail">
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
    computed: {
        canUpgrade() {
            let canUpgrade = false;
            if (! this.item.relic && 
                (   (this.item.category == 'Melee Weapon' && !this.item.masterwork)
                    || (this.item.category == 'Ranged Weapon' && !this.item.largeCaliber)
                    || (this.item.category == 'Ranged Weapon' && this.canHaveAmmo))
                ){
                    canUpgrade = true;
                }
                
            

            return canUpgrade;
        },
        canHaveAmmo() {
            let inventory = this.$store.state.currentUnit.inventory;
            let inventoryHasPrefall = false;
            let preFallEquipped = false;
            inventory.forEach( inventoryItem => {
                if (inventoryItem.hasPrefallAmmo && inventoryItem.id != this.item.id){
                    preFallEquipped = true;
                } else if (inventoryItem.name == 'Pre-Fall Ammo'){
                    inventoryHasPrefall = true;
                }
            });

            preFallEquipped ? inventoryHasPrefall = false :  inventoryHasPrefall;
            return inventoryHasPrefall;
        }
    },
    methods: {
        transferButton() {
            if (this.$store.state.showUnitDetail) {
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
        sellItem(){
            ItemService.sellItem(this.item.id)
                .then( () => {
                    if(this.$store.state.showUnitDetail){
                        this.$store.dispatch('reloadCurrentUnit');
                    } else {
                        this.$store.dispatch('reloadCurrentTeam');
                    }
                }).catch( error => this.$store.dispatch('showError', error) )
        },
        deleteItem(){
            ItemService.deleteItem(this.item.id)
                .then( () => {
                    if(this.$store.state.showUnitDetail){
                        this.$store.dispatch('reloadCurrentUnit');
                    } else {
                        this.$store.dispatch('reloadCurrentTeam');
                    }
                }).catch( error => this.$store.dispatch('showError', error) )
        },
        equipItem(){
            ItemService.toggleEquip(this.item.id, this.$store.state.currentUnit.id)
                .then(() => this.$store.dispatch('reloadCurrentUnit'))
                .catch(error => this.$store.dispatch('showError', error));
        },
        upgrade() {
            this.$store.commit('SET_ITEM_TO_MODIFY', this.item);
            this.$store.commit('SET_POPUP_SUBFORM', 'UpgradeWeapon');
            this.$store.commit('TOGGLE_SHOW_POPUP');
        }
    }
}


</script>

<style>
i {
    margin: 1px;
    height: max-content;
}
</style>
