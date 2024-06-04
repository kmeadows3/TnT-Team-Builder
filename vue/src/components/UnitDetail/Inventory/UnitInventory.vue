<template>
    <div class="inventory">
        <h2>UNIT INVENTORY</h2>
        <UnitWeapons />
        <UnitArmor />
        <UnitEquipment />
        <InventoryActions />
    </div>

    <div class="reference">
        <h2>Item Rules Reference</h2>
        <div v-for="trait in $store.state.unitInventoryTraits" :key="'itemTrait' + trait.id">
            <h3> {{ trait.name }}</h3>
            <p>{{ trait.effect }}</p>
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
