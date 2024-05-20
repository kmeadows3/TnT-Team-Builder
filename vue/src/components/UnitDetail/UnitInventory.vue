<template>
    <div>
        <h1>UNIT INVENTORY</h1>
        <div>
            <h3>Weapons</h3>
            <div class="labels weapon-list">
                <div>Type</div>
                <div>Cost</div>
                <div>Range </div>
                <div>Strength</div>
                <div>Reliability</div>
                <div>Hands Required</div>
                <div>Special Rules</div>
            </div>

            <div class="weapon-list" v-for="weapon in weapons" :key="weapon.id">
                <div>{{ weapon.name }}</div>
                <div>{{ weapon.cost }}</div>
                <div>{{ weapon.meleeRange }} / {{ weapon.rangedRange }}</div>
                <div>{{ weapon.strength }}</div>
                <div>{{ weapon.reliability }}</div>
                <div>{{ weapon.handsRequired }}</div>
                <div>{{ weapon.specialRules }}</div>
            </div>
        </div>
        <div>
            <h3>Armor</h3>
            <div v-for="armor in armors" :key="armor.id">
                {{ armor }}
            </div>
        </div>
        <div>
            <h3>Equipment</h3>
            <div v-for="equipment in equipments" :key="equipment.id">
                {{ equipment }}
            </div>
        </div>
        <div class="reference">
            <h2>Item Rules Reference</h2>
            <div v-for="trait in itemTraitReference" :key="trait.id">
                <h3> {{ trait.name }}</h3>
                <p>{{ trait.effect }}</p>
            </div>
        </div>
    </div>

</template>

<script>

export default {
    data() {
        return {
            weapons: [],
            armors: [],
            equipments: [],
            itemTraitReference: []
        }
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
div.weapon-list {
    display: flex;
}

div.weapon-list>div {
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
}
</style>