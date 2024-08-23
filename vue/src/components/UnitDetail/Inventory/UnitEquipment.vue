<template>
    <div class="item-container" v-show="equipments.length > 0">
        <div class="item-table">
            <div class="title">Equipment</div>
            <div class="table-label item-list equipment-grid"
                :class="$store.state.manageInventory ? 'action-mode' : ''">
                <div class="equipment-name">Type</div>
                <div class="equipment-cost">Cost</div>
                <div class="equipment-rules">Special Rules</div>
                <div class="equipment-equipped">Equipped</div>
                <div class="item-action" v-if="$store.state.manageInventory">
                    Actions</div>
            </div>
            <div class="item-list equipment-grid" v-for="equipment in equipments" :key="'equipment' + equipment.id"
                :class="$store.state.manageInventory ? 'action-mode' : ''">
                <div class="item-name">{{ equipment.name }}</div>
                <div class="equipment-cost">{{ equipment.cost }}</div>
                <div class="equipment-rules item-special-rules">
                    <span v-show="equipment.itemTraits.length == 0 || equipment.specialRules != 'N/A'">
                        {{ equipment.specialRules }}<span v-show="equipment.itemTraits.length > 0">, </span>
                    </span>
                    <span v-show="equipment.itemTraits.length > 0">
                        <span v-for="(trait, index) in equipment.itemTraits" :key="'trait' + equipment.id + trait.id">
                            <span v-show="index != 0">,</span>
                            {{ trait.name }}</span>
                    </span>
                </div>
                <div class="equipment-equipped item-check">
                    <i class="bi bi-check-circle" title="Currently Equipped" v-show="equipment.equipped"></i>
                    <i class="bi bi-x-circle" title="Currently Unequipped" v-show="!equipment.equipped"></i>
                </div>
                <ItemActions class="item-action equipment-action" :item='equipment' />
            </div>
        </div>
    </div>
</template>

<script>
import ItemActions from '../../Shared/ItemActions.vue';

export default {
    components: {
        ItemActions
    },
    computed: {
        equipments() {
            let equipment =  this.$store.state.currentUnit.inventory.filter(item => item.category == "Equipment");
            equipment.sort((a, b) => {
                const order = {true: 1, false: 2};
                return order[a.equipped] - order[b.equipped] || a.name.localeCompare(b.name)
            });
            return equipment;
        }
    }
}
</script>

<style scoped>
div.item-list.equipment-grid {
    display: grid;
    grid-template-areas: "name  cost  rules equipped";
    grid-template-columns: 2fr 1fr 6fr 1fr;
}


div.item-list.equipment-grid.action-mode {
    display: grid;
    grid-template-areas: "name  cost  rules equipped action";
    grid-template-columns: 2fr 1fr 8fr 2fr 3fr;
}

div.item-list>.item-name {
    grid-area: name;
    font-weight: bold;
}


div.item-list>.equipment-cost {
    grid-area: cost;
}

div.item-list>.equipment-rules {
    grid-area: rules;
    border-top: none;
    border-right: dotted var(--thin-border) var(--border-color);
    padding-right: 3px;

}

div.item-list>.item-action {
    grid-area: action;
}


@media only screen and (max-width: 992px) {
    div.item-list.equipment-grid {
        grid-template-columns: 3fr 1fr 6fr 2fr;
    }

    div.item-list.equipment-grid.action-mode {
    grid-template-areas: "name  cost  rules equipped action";
    grid-template-columns: 3fr 1fr 6fr 2fr 2fr;
    }
}

@media only screen and (max-width: 600px) {
    div.item-list.equipment-grid.action-mode {
        grid-template-areas: "name  cost equipped action" 
                                "name rules rules rules";
        grid-template-columns: 1fr 1fr 1fr 1fr;

        >div.equipment-rules {
            border-right: none;
            border-top: dotted var(--thin-border) var(--border-color);
        }
    }

    div.table-label>div.equipment-rules{
        display: none;
    }

}
</style>