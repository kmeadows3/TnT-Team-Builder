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
        <div v-for="trait in itemTraitReference" :key="'itemTrait' + trait.id">
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
import ItemService from '../../../services/ItemService';

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
            let inventory = this.$store.state.currentUnit.inventory;
            inventory.forEach(item => item.itemTraits.forEach(
                trait => {
                    if (!this.itemTraitReference.some((x) => x.id == trait.id)) {
                        this.itemTraitReference.push(trait)
                    }
                })
            );

            this.itemTraitReference = this.itemTraitReference.sort((a, b) => a.name.localeCompare(b.name));

            return inventory;
        }
    },
    beforeMount() {
        this.setupItemTraits();
    }
}

</script>

<style scoped>


</style>