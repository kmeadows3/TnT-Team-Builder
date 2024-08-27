<template>
    <section class="inventory">
        <div class="item-container">
            <h1 class="section-title">Team Inventory</h1>
            <InventoryActions />
            <div class="item-table team-inventory" v-show="$store.state.currentTeam.inventory.length != 0">
                <div class="table-label item-list team-inventory" :class="{action:$store.state.manageInventory}">
                    <div class="item-med type">Type</div>
                    <div class="item-small cost">BS Cost</div>
                    <div class="item-large special">Special Rules</div>
                    <div class="item-small relic">Relic</div>
                    <div class="item-small support">Support</div>
                    <div class="item-action" v-if="$store.state.manageInventory"> Actions</div>
                </div>
                <div class="item-list" v-for="item in sortedInventory" :key="'teamInventory' + item.id" :class="{action:$store.state.manageInventory}">
                    <div class="item-med type">{{ item.name }}</div>
                    <div class="item-small cost">{{ item.cost }}</div>
                    <div class="item-large item-special-rules special">
                        <span v-show="item.itemTraits.length == 0 || item.specialRules != 'N/A'">
                            {{ item.specialRules }}<span v-show="item.itemTraits.length > 0">, </span>
                        </span>
                        <span v-show="item.itemTraits.length > 0">
                            <span v-for="(trait, index) in item.itemTraits" :key="'trait-' + item.id + '-' + trait.id">
                                <span v-show="index != 0">,</span>
                                {{ trait.name }}</span>
                        </span>
                    </div>
                    <div class="item-small item-check relic">
                        <i class="bi bi-check-circle" title="Relic" v-show="item.relic"></i>
                        <i class="bi bi-x-circle" v-show="!item.relic"></i>

                    </div>
                    <div class="item-small item-check support">
                        <i class="bi bi-check-circle" title="Support" v-show="item.category == 'Support Weapon'"></i>
                        <i class="bi bi-x-circle" v-show="!(item.category == 'Support Weapon')"></i>
                    </div>
                    <ItemActions class="item-action" :item='item' />
                </div>
            </div>
            <div v-show="$store.state.currentTeam.inventory.length == 0" class="no-values">
                This team has no unassigned items.
            </div>
        </div>        
    </section>
</template>

<script>
import InventoryActions from '../Shared/InventoryActions.vue';
import ItemActions from '../Shared/ItemActions.vue';

export default {
    components: {
        InventoryActions,
        ItemActions
    },
    computed: {
        sortedInventory() {
            let inventory = this.$store.state.currentTeam.inventory;
            inventory = inventory.sort((a, b) => a.name.localeCompare(b.name)).sort((a, b) => {
                if (a.relic == true && b.relic == false) {
                    return -1;
                } else if (b.relic == true && a.relic == false) {
                    return 1;
                }

                return 0;
            });
            return inventory;
        }
    }
}
</script>


<style scoped>

div.item-table.team-inventory > div.item-list{

    display: grid;
    grid-template-columns: 2fr 1fr 5fr 1fr 1fr;
    grid-template-areas: 'type cost special relic support';

    &.action {
        grid-template-columns: 2fr 1fr 5fr 1fr 1fr 3fr;
        grid-template-areas: 'type cost special relic support action';
    }
}

div.item-list > div {
    &.type{
        grid-area: type;
        text-align: center;
    }
    &.cost{
        grid-area: cost;
    }
    &.special{
        grid-area: special;
    }
    &.relic{
        grid-area: relic;
    }
    &.support{
        grid-area: support;
    }
    &.item-action{
        grid-area: action;
    }
}

div.team-inventory div.item-special-rules {
    border-top: none;
    border-right: dotted var(--thin-border) var(--border-color);
}
</style>