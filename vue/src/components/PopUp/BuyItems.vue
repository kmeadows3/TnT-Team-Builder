<template>
    <div>
        <h1 class="section-title">Purchase Items</h1>
        <div class="radio-tab-wrapper">
            <input type="radio" class="tab" name="filterTab" value="Armor" id="armorTab" checked
                v-model="filter.itemCategory" />
            <label for="armorTab">Armors</label>
            <input type="radio" class="tab" name="filterTab" value="Equipment" id="equipmentTab"
                v-model="filter.itemCategory" />
            <label for="equipmentTab">Equipment</label>
            <input type="radio" class="tab" name="filterTab" value="Melee Weapon" id="meleeTab"
                v-model="filter.itemCategory" />
            <label for="meleeTab">Melee Weapons</label>
            <input type="radio" class="tab" name="filterTab" value="Ranged Weapon" id="rangedTab"
                v-model="filter.itemCategory" />
            <label for="rangedTab">Ranged Weapons</label>
            <input type="radio" class="tab" name="filterTab" value="Support Weapon" id="supportTab"
                v-model="filter.itemCategory" />
            <label for="supportTab">Support Weapons</label>
            <input type="radio" class="tab" name="filterTab" value="Grenade" id="grenadeTab"
                v-model="filter.itemCategory" />
            <label for="grenadeTab">Grenades</label>
            <div v-if="$store.state.currentUnit.rank != 'Rank and File'">
                <label for="filterRarity">Max Relic Rarity: </label>
                <select id="filterRarity" v-model.number="filter.maxRarity">
                    <option :value="0">No Relics</option>
                    <option :value="1">Sporadic</option>
                    <option :value="2">Scarce</option>
                    <option :value="3">Rare</option>
                    <option :value="4">Ultra Rare</option>
                </select>
            </div>
        </div>
        <div class='item-purchase-list'>
            <span>Current Barter Scrip: {{ $store.state.currentTeam.money }}</span>
            <div class="grid-row table-label">
                <div class="grid-name">Name</div>
                <div class="grid-rarity">Rarity</div>

                <div class="grid-cost">Cost</div>
                <div class="grid-buttons">Actions</div>
            </div>

            <div class="grid-row" v-for="item in filteredItems" :key="'refItem' + item.referenceId"
                :class="{ 'too-expensive': trueItemCost(item) > $store.state.currentTeam.money }">
                <div class="grid-name">{{ item.name }}</div>
                <div class="grid-rarity">
                    {{ item.rarity }}
                </div>
                <div class="grid-cost">
                    {{ item.category != 'Armor' || !$store.state.showUnitDetail || this.$store.state.currentUnit.wounds
                        == 1 ? item.cost :
                        this.$store.state.currentUnit.wounds == 2 ? item.cost2Wounds : item.cost3Wounds }} BS
                </div>

                <div class="grid-buttons">
                    <button @click="purchaseItem(item.referenceId)"
                        :disabled="trueItemCost(item) > $store.state.currentTeam.money">
                        Buy
                    </button>
                    <button @click="gainItem(item.referenceId)">Gain for Free</button>
                </div>
            </div>
        </div>
        <span class="button-container">
            <button @click="cancel()">Cancel</button>
        </span>
        
    </div>
</template>


<script>

import ItemService from '../../services/ItemService';


export default {
    data() {
        return {
            filter: {
                itemCategory: 'Armor',
                maxRarity: 0
            },
            itemsForPurchase: []
        }
    },
    computed: {
        filteredItems() {
            let filteredList = this.itemsForPurchase;

            filteredList = filteredList.filter(item => item.category == this.filter.itemCategory);

            filteredList = filteredList.filter((item) => {
                if (item.rarity == 'N/A'
                    || (item.rarity == 'Ultra Rare' && this.filter.maxRarity == 4)
                    || (item.rarity == 'Rare' && this.filter.maxRarity >= 3)
                    || (item.rarity == 'Scarce' && this.filter.maxRarity >= 2)
                    || (item.rarity == 'Sporadic' && this.filter.maxRarity >= 1)
                ) {
                    return true;
                }
                return false;
            })


            return filteredList;
        }
    },
    methods: {
        getItemsForPurchase() {

            ItemService.retrieveItemsForPurchase()
                .then(response => {
                    this.itemsForPurchase = response.data;
                }).catch(error => this.$store.dispatch('showError', error));
        },
        purchaseItem(itemId) {
            if (this.$store.state.showUnitDetail) {
                ItemService.purchaseItemForUnit(this.$store.state.currentUnit.id, itemId)
                    .then(response => {
                        this.$store.dispatch('reloadCurrentUnit');
                    })
                    .catch(err => {
                        console.log(err);
                        this.$store.dispatch('showError', err);
                    })
            } else {
                ItemService.purchaseItemForTeam(this.$store.state.currentTeam.id, itemId)
                    .then(response => {
                        this.$store.dispatch('reloadCurrentTeam');
                    })
                    .catch(err => {
                        console.log(err);
                        this.$store.dispatch('showError', err);
                    })
            }
        },
        gainItem(itemId) {
            if (this.$store.state.showUnitDetail) {
                ItemService.gainItemForFree(this.$store.state.currentUnit.id, itemId)
                    .then(response => {
                        this.$store.dispatch('reloadCurrentUnit');
                    })
                    .catch(err => this.$store.dispatch('showError', err));
            } else {
                ItemService.gainItemForFreeForTeam(this.$store.state.currentTeam.id, itemId)
                    .then(response => {
                        this.$store.dispatch('reloadCurrentTeam');
                    })
                    .catch(err => this.$store.dispatch('showError', err));
            }
        },
        trueItemCost(item) {
            let wounds = this.$store.state.currentUnit.wounds;

            if (item.category != 'Armor' || wounds == 1) {
                return item.cost;
            } else if (wounds == 2) {
                return item.cost2Wounds;
            }
            return item.cost3Wounds;
        },
        cancel(){
            this.$store.commit('REMOVE_SHOW_POPUP');
        }
    },
    beforeMount() {
        this.getItemsForPurchase();
    }
}

</script>


<style scoped>
.item-purchase-list {
    padding: 10px 10px 0px 10px;
}

.grid-row {
    display: grid;
    margin: auto;
    margin-top: 5px;
    width: 80%;
    grid-template-columns: 3fr 2fr 1fr 3fr;
    grid-template-areas: "Name Rarity Cost Buttons";
    text-align: center;
}

.grid-name {
    grid-area: "Name";
    text-align: end;
    padding-right: 5px;
}

.grid-cost {
    grid-area: "Cost";
}

.grid-rarity {
    grid-area: "Rarity";
}

.grid-buttons {
    grid-area: "Buttons";
}

.grid-buttons>button {
    margin: 0px 3px;
}

.too-expensive {
    background-color: rgb(236, 173, 173);
}




.radio-tab-wrapper {
    margin-top: 5px;
    display: flex;
    border-bottom: 1px solid #428bca;
    padding: 0 10px;
    position: relative;
}

.radio-tab-wrapper>div {
    display: inline-block;
    margin: auto;
    padding-left: 5px;

}

input.tab {
    display: none;

    &+label {
        display: flex;
        align-items: center;
        cursor: pointer;
        float: left;
        border: 1px solid #aaa;
        border-bottom: 0;
        background-color: #fff;
        margin-right: -1px;
        padding: .5em 1em;
        position: relative;
        vertical-align: middle;
        border-top-left-radius: 5px;
        border-top-right-radius: 5px;


        &:hover {
            background-color: #eee;
        }
    }

    &:checked+label {
        box-shadow: 0 3px 0 -1px #fff,
            inset 0 5px 0 -1px #13CD4A;
        background-color: #fff;
        border-color: #428bca;
        z-index: 1;
    }
}
</style>