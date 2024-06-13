<template>
    <div class="inventory">
        <h1 class="section-title">Inventory</h1>
        <UnitWeapons />
        <UnitArmor />
        <UnitEquipment />
        <InventoryActions />

    </div>

    <div class="reference">
        <h1 class="section-title">Item Rules Reference</h1>
        <div v-for="trait in $store.state.unitInventoryTraits" :key="'itemTrait' + trait.id">
            <h2 class="reference-label"> {{ trait.name }}</h2>
            <p class="reference-desc">{{ trait.effect }}</p>
        </div>
    </div>
</template>

<script>
import UnitWeapons from './UnitWeapons.vue';
import UnitArmor from './UnitArmor.vue';
import UnitEquipment from './UnitEquipment.vue';
import InventoryActions from '../../InventoryShared/InventoryActions.vue';

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
