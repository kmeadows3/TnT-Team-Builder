<template>
        <div class ='unit-inventory-actions' v-show="!buyItems && !manageCurrentInventory">
            <button @click="toggleBuyItems()">Add Item</button>
            <button >Manage Inventory</button>
        </div>
        <div v-show="buyItems">
            <div>
                <input type="checkbox" id="filterMeleeWeapons" v-model="filter.showMeleeWeapons" />
                <label for="filterWeapons">Melee Weapons</label>
                <input type="checkbox" id="filterRangedWeapons" v-model="filter.showRangedWeapons" />
                <label for="filterWeapons">Ranged Weapons</label>
                <input type="checkbox" id="filterGrenades" v-model="filter.showGrenades" />
                <label for="filterWeapons">Grenades</label>
                <input type="checkbox" id="filterArmor" v-model="filter.showArmor" />
                <label for="filterWeapons">Armor</label>
                <input type="checkbox" id="filterEquipment" v-model="filter.showEquipment" />
                <label for="filterWeapons">Equipment |</label>

                <label for="filterWeapons"> Max Relic Rarity</label>
                <select id="filterRarity" v-model.number="filter.maxRarity">
                    <option :value="0">None</option>
                    <option :value="1">Sporadic</option>
                    <option :value="2">Scarce</option>
                    <option :value="3">Rare</option>
                    <option :value="4">Ultra Rare</option>
                </select>

            </div>
            <div v-for="item in filteredItems" :key="'refItem' + item.referenceId">
            {{ item.name }} - {{ item.rarity }} - {{ item.category }}
            </div>
        </div>



</template>
<script>
import ItemService from '../../../services/ItemService';


export default {

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
                        this.$store.commit('SET_ITEMS_FOR_PURCHASE', response.data);
                        this.itemsForPurchase = response.data;
                    }).catch(error => console.error(error));
            } catch (error) {
                console.error(error);
            }
        }
    }
}

</script>


<style>

div.unit-inventory-actions {
    padding: 10px;
}

</style>