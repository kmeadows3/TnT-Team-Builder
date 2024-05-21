<template>
    <div class="inventory">
        <h2>UNIT INVENTORY</h2>
        <UnitWeapons :weapons="this.weapons" />
        <UnitArmor :armors="this.armors" />
        <UnitEquipment :equipments="this.equipments" />
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

export default {
    data() {
        return {
            weapons: [],
            armors: [],
            equipments: [],
            itemTraitReference: []
        }
    },
    components: {
        UnitWeapons,
        UnitArmor,
        UnitEquipment
    },
    methods: {
        loadInventory() {

            let inventory = this.$store.state.currentUnit.inventory;
            inventory = this.setupItemTraits(inventory);

            this.armors = inventory.filter(item => item.category == "Armor");
            this.equipments = inventory.filter(item => item.category == "Equipment");
            this.weapons = inventory.filter(item => item.category != "Armor" && item.category != "Equipment");

            inventory.forEach(item => item.itemTraits.forEach(
                trait => {
                    if (!this.itemTraitReference.includes(trait)) {
                        this.itemTraitReference.push(trait)
                    }
                })
            );
            this.itemTraitReference = this.itemTraitReference.sort((a, b) => a.name.localeCompare(b.name));
        },
        setupItemTraits(inventory) {
            inventory.forEach(item => item.itemTraits.forEach(
                trait => {
                    if (!this.itemTraitReference.includes(trait)) {
                        this.itemTraitReference.push(trait)
                    }
                })
            );

            return inventory;
        }
    },
    beforeMount() {
        this.loadInventory();
    }
}

</script>

<style scoped>
div.inventory {
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
}
</style>