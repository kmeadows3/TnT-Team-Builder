<template>
    <div class="item-container" v-show="weapons.length > 0">
        <h2 class="subsection-title">Weapons</h2>
        <div class="item-table">
            <div class="item-list table-label weapon-grid">
                <div class="item-name">Type</div>
                <div class="weapon-cost">Cost</div>
                <div class="weapon-range">Range </div>
                <div class="weapon-strength">Strength</div>
                <div class="weapon-reliablity">Reliability</div>
                <div class="weapon-hands">Hands</div>
                <div class="weapon-equip">Equipped</div>
                <div class="item-action weapon-action" v-if="$store.state.manageInventory">
                    Actions</div>
            </div>
            <div class="item-list weapon-grid" v-for="weapon in weapons" :key="'weapon' + weapon.id">
                <div class="item-name">{{ weapon.name }}</div>
                <div class="weapon-cost">{{ weapon.cost }}</div>
                <div class="weapon-range">{{ weapon.meleeRange }}"/ {{ weapon.rangedRange }}"</div>
                <div class="weapon-strength">{{ weapon.strength }}</div>
                <div class="weapon-reliablity">{{ weapon.reliability }}</div>
                <div class="weapon-hands">{{ weapon.handsRequired }}</div>
                <div class="item-special-rules weapon-rules">
                    <span v-show="weapon.itemTraits.length == 0 || weapon.specialRules != 'N/A'">
                        {{ weapon.specialRules }}
                        <span v-show="weapon.itemTraits.length > 0">, </span>
                    </span>
                    <span v-show="weapon.itemTraits.length > 0">
                        <span v-for="(trait, index) in weapon.itemTraits" :key="'trait' + weapon.id + trait.id">
                            <span v-show="index != 0">,</span>
                            {{ trait.name }}</span>
                    </span>
                </div>
                <div class="item-check weapon-equip">
                    <i class="bi bi-check-circle" title="Currently Equipped" v-show="weapon.equipped"></i>
                    <i class="bi bi-x-circle" title="Currently Unequipped" v-show="!weapon.equipped"></i>
                </div>
                <ItemActions class="item-action weapon-action" :item="weapon"/>
            </div>
        </div>
    </div>
</template>

<script>
import ItemActions from '../../InventoryShared/ItemActions.vue';

export default {
    components: {
        ItemActions
    },
    computed: {
        weapons() {
            return this.$store.state.currentUnit.inventory.filter(item => item.category != "Armor"  && item.category != "Equipment");
        }
    }
}
</script>

<style scoped>


div.table-label.weapon-grid{
    display: grid;
    grid-template-areas:  "name  cost  range strength reliablity hands equipped action";
    grid-template-columns: 2fr 1fr 1fr 1fr 1fr 1fr 1fr;

}

div.item-list.weapon-grid{
    display: grid;
    grid-template-areas:  "name  cost  range strength reliablity hands equipped action"
                          "name  rules rules rules    rules      rules rules    rules";
    grid-template-columns: 2fr 1fr 1fr 1fr 1fr 1fr 1fr;
}

div.item-name{
    grid-area: name;
}

div.weapon-cost{
    grid-area: cost;
}
div.weapon-range{
    grid-area: range;
}
div.weapon-strength{
    grid-area: strength;
}
div.weapon-reliability{
    grid-area: reliability;
}
div.weapon-hands{
    grid-area: hands;
}
div.weapon-equipped{
    grid-area: equipped;
}
div.weapon-rules{
    grid-area: rules;
}
div.weapon-action{
    grid-area: action;
}


</style>