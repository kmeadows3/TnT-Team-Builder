<template>
    <div class="inventory">
        <h2>UNIT INVENTORY</h2>
        <UnitWeapons />
        <UnitArmor />
        <UnitEquipment />
        <UnitInventoryActions />
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
import UnitInventoryActions from './UnitInventoryActions.vue';

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
        UnitInventoryActions
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
