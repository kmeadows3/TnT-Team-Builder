<template>
        <div class ='unit-inventory-actions' v-show="!buyItems && !manageCurrentInventory">
            <button @click="toggleBuyItems()">Add Item</button>
            <button >Manage Inventory</button>
        </div>
        <div v-show="buyItems">
            <div>
                <input type="checkbox" id="filterMeleeWeapons" v-model="filter.showMeleeWeapons" />
                <label for="filterMeleeWeapons">Melee Weapons</label>
                <input type="checkbox" id="filterRangedWeapons" v-model="filter.showRangedWeapons" />
                <label for="filterRangedWeapons">Ranged Weapons</label>
                <input type="checkbox" id="filterSupportWeapons" v-model="filter.showSupportWeapons" />
                <label for="filterSupportWeapons">Support Weapons</label>
                <input type="checkbox" id="filterGrenades" v-model="filter.showGrenades" />
                <label for="filterGrenades">Grenades</label>
                <input type="checkbox" id="filterArmor" v-model="filter.showArmor" />
                <label for="filterArmor">Armor</label>
                <input type="checkbox" id="filterEquipment" v-model="filter.showEquipment" />
                <label for="filterEquipment">Equipment </label>
                <span> | </span>
                <label for="filterRarity"> Max Relic Rarity: </label>
                <select id="filterRarity" v-model.number="filter.maxRarity">
                    <option :value="0">No Relics</option>
                    <option :value="1">Sporadic</option>
                    <option :value="2">Scarce</option>
                    <option :value="3">Rare</option>
                    <option :value="4">Ultra Rare</option>
                </select>

            </div>

            <div v-for="item in filteredItems" :key="'refItem' + item.referenceId">
            {{ item.name }} - 
            {{ item.category != 'Armor' || this.$store.state.currentUnit.wounds == 1 ? item.cost : this.$store.state.currentUnit.wounds == 2 ? item.cost2Wounds : item.cost3Wounds}} BS
             - <button @click = "purchaseItem(item.referenceId)">Buy</button> - Gain for Free

            </div>
        </div>



</template>
<script>
import ItemService from '../../../services/ItemService';


export default {

    emits: ['update'],
    data() {
        return {
            buyItems: false,
            manageCurrentInventory: false,
            filter: {
                showMeleeWeapons: true,
                showGrenades: false,
                showRangedWeapons: false,
                showArmor: false,
                showEquipment: false,
                showSupportWeapons: false,
                maxRarity: 0
            },
            itemsForPurchase: []
        }
    },
    computed: {
        filteredItems() {
            let filteredList = this.itemsForPurchase;
            filteredList = filteredList.filter( item => {
                if (item.category == 'Armor'){
                    return this.filter.showArmor;
                } else if (item.category == 'Equipment'){
                    return this.filter.showEquipment;
                } else if (item.category == 'Melee Weapon'){
                    return this.filter.showMeleeWeapons;
                } else if (item.category == 'Support Weapon'){
                    return this.filter.showSupportWeapons;
                } else if (item.category == 'Ranged Weapon'){
                    return this.filter.showRangedWeapons;
                } else {
                    return this.filter.showGrenades;
                }
            })

            filteredList = filteredList.filter( (item) => {
                if (item.rarity == 'N/A'
                    || (item.rarity == 'Ultra Rare' && this.filter.maxRarity == 4)
                    || (item.rarity == 'Rare' && this.filter.maxRarity >= 3)
                    || (item.rarity == 'Scarce' && this.filter.maxRarity >= 2)
                    || (item.rarity == 'Sporadic' && this.filter.maxRarity >= 1)
                ){
                    return true;
                } 
                console.log(item.rarity + " : " + this.filter.maxRarity)
                return false;
            })


            return filteredList;
        }
    },
    methods: {
        toggleBuyItems() {
            this.buyItems = !this.buyItems;
            this.getItemsForPurchase();
        },
        getItemsForPurchase() {
            try {
                ItemService.retrieveItemsForPurchase()
                    .then(response => {
                        this.itemsForPurchase = response.data;
                    }).catch(error => console.error(error));
            } catch (error) {
                console.error(error);
            }
        },
        purchaseItem(itemId){
            ItemService.purchaseItemForUnit(this.$store.state.currentUnit.id, itemId)
                .then(response => {
                    const unit = this.$store.dispatch('reloadCurrentUnit').then( (x) =>{
                        console.log(x);
                    })                    
                })
                .catch( err => console.error(err))
        }
    }
}

</script>


<style>

div.unit-inventory-actions {
    padding: 10px;
}

</style>