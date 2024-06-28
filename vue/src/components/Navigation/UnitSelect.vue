<template>
        <h2 class="subsection-title">Change Unit</h2>
        <div class="flex-nav-option selected" @click="changeUnit($store.state.currentUnit)">
            {{ $store.state.currentUnit.name }} - {{ $store.state.currentUnit.unitClass }}
        </div>
        <div class="flex-nav-option" v-for="unit in filteredUnits" :key="'unit-select-' + unit.id" @click="changeUnit(unit)">
            {{ unit.name }} - {{ unit.unitClass }}
        </div>
</template>

<script>
    export default {
        computed: {
            filteredUnits() {
                let units = this.$store.state.currentTeam.unitList;

                units = units.filter( (unit) => unit.id != this.$store.state.currentUnit.id);

                return units;
            }
        },
        methods: {
            changeUnit(unit){
                this.$store.commit('SET_CURRENT_UNIT', unit);
            }
        }
    }

</script>