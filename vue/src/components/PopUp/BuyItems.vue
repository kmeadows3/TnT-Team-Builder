<template>
    <div class="buybox">
        <h1 class="section-title popup">Purchase Items</h1>
        <div class="radio-tab-wrapper">
            <input type="radio" class="tab" name="filterTab" value="Armor" id="armorTab" checked
                v-model="filter.itemCategory" />
            <label for="armorTab" v-show="$store.state.currentUnit.unitClass != 'Berserker'">Armors</label>
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
            <input type="radio" class="tab" name="filterTab" value="Other" id="otherTab" v-model="filter.itemCategory"
                v-if="$store.state.currentUnit.species == 'Mutant'" />
            <label for="otherTab" v-if="$store.state.currentUnit.species == 'Mutant'">Mutation Attacks</label>
            <div v-if="$store.state.currentUnit.rank != 'Rank and File'" class="relic-filter">
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
            <p v-show="filter.itemCategory == 'Other'">Must have appropriate mutation to use.</p>
            <p><strong>Current Barter Scrip: </strong>{{ $store.state.currentTeam.money }}</p>
            <div class="option-list">
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
                        {{ item.category != 'Armor' || !$store.state.showUnitDetail ||
                            this.$store.state.currentUnit.wounds
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

        </div>
        <span class="popup-buttons">
            <button @click="cancel()" class="danger">Cancel</button>
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

            filteredList = filteredList.sort((a, b) => a.name.localeCompare(b.name));



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
                        this.$store.dispatch('showError', err);
                    })
            } else {
                ItemService.purchaseItemForTeam(this.$store.state.currentTeam.id, itemId)
                    .then(response => {
                        this.$store.dispatch('reloadCurrentTeam');
                    })
                    .catch(err => {
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
        cancel() {
            this.$store.commit('REMOVE_SHOW_POPUP');
        }
    },
    beforeMount() {
        this.getItemsForPurchase();
    }
}

</script>


<style scoped>
div.buybox {
    display: flex;
    flex-direction: column;
    max-height: 50%;
    overflow: auto;
    min-width: 50vw;
}

div.buybox p {
    margin: 0px;
    padding: var(--standard-padding);
    text-align: center;
}

span.popup-buttons {
    padding-bottom: var(--wide-padding);
}

.item-purchase-list {
    padding: var(--wide-padding) 0px;
    align-content: center;
}

div.option-list {
    margin: auto;
    width: 90%;
    border: solid var(--thick-border) var(--border-color);
    border-radius: var(--border-radius-card);
    background-color: var(--list-background);

    &>div:nth-child(odd) {
        background-color: var(--alternative-background);
    }

    &>div.too-expensive {
    font-style: italic;
    color: var(--unselected);
    }

    &>div.grid-row.table-label {
        border-bottom: solid var(--thick-border) var(--border-color);
        background-color: var(--standard-medium);
        border-radius: var(--border-radius-card-title) var(--border-radius-card-title) 0px 0px;
    }
}

.grid-row {
    display: grid;
    grid-template-columns: 3fr 2fr 1fr 3fr;
    grid-template-areas: "Name Rarity Cost Buttons";
    text-align: center;
    padding: 1px 0px;

    >div {
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .grid-name {
        grid-area: "Name";
        justify-content: end;
    }

    .grid-cost {
        grid-area: "Cost";
    }

    .grid-rarity {
        grid-area: "Rarity";
    }

    .grid-buttons {
        grid-area: "Buttons";
        display: flex;
        align-items: center;
        justify-content: center;
        gap: var(--standard-padding);

        & >button {
            background-color: var(--highlight-medium);
            
            &:hover {
                background-color: var(--highlight-light);
            }
            &:disabled{
                font-style: italic;
            }
            &:disabled:hover{
                background-color: var(--highlight-medium);
                transform: translateY(0px);
            }
        }
    }

    &:last-child{
        border-radius: 0px 0px var(--border-radius-card-title) var(--border-radius-card-title);
    }
}


.radio-tab-wrapper {
    margin-top: var(--wide-padding);
    display: flex;
    border-bottom: var(--thin-border) solid var(--standard-medium);
    padding: 0 var(--wide-padding);
    position: relative;
}

.radio-tab-wrapper>div {
    display: inline-block;
    margin: auto;
    padding-left: var(--wide-padding);
}

input.tab {
    display: none;

    &+label {
        display: flex;
        align-items: center;
        text-align: center;
        cursor: pointer;
        float: left;
        border: 1px solid var(--standard-mid-dark);
        border-bottom: 0;
        background-color: var(--standard-light);
        margin-right: -1px;
        padding: var(--wide-padding) var(--standard-padding) var(--standard-padding) var(--standard-padding);
        position: relative;
        vertical-align: middle;
        border-top-left-radius: var(--border-radius);
        border-top-right-radius: var(--border-radius);

        &:hover {
            background-color: var(--standard-very-light);
        }
    }

    &:checked+label {
        box-shadow: 0 3px 0 -1px var(--section-background),
            inset 0 5px 0 -1px var(--highlight-light);
        background-color: var(--standard-very-light);
        border-color: var(--standard-medium);
        z-index: 1;
    }
}

div.relic-filter {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
}
</style>