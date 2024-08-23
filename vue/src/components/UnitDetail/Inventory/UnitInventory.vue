<template>
    <section class="inventory-container">
        <div class="inventory">
            <h1 class="section-title">Inventory</h1>
            <InventoryActions />
            <div v-show="$store.state.currentUnit.inventory.length == 0" class="no-values">
                This unit has no items.
            </div>
            <UnitWeapons />
            <UnitArmor />
            <UnitEquipment />
        </div>
    </section>

</template>

<script>
import UnitWeapons from './UnitWeapons.vue';
import UnitArmor from './UnitArmor.vue';
import UnitEquipment from './UnitEquipment.vue';
import InventoryActions from '../../Shared/InventoryActions.vue';

export default {
    data() {
        return {
            itemTraitReference: []
        }
    },
    components: {
        UnitWeapons,
        UnitArmor,
        UnitEquipment,
        InventoryActions
    },
    methods: {
        setupItemTraits() {
            this.$store.dispatch('updateUnitInventoryTraits');
        }
    },
    beforeMount() {
        this.setupItemTraits();
    }
}

</script>
