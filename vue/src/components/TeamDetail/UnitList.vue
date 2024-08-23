<template>
    <section class="unit-list">
        <h1 class="section-title">Unit List</h1>

        <div class="button-container">
                <button @click="openNewUnitForm()">Add Unit</button>
        </div>


        <div>
            <div v-show="$store.state.currentTeam.unitList.length == 0" class="card-container no-values">
                This team has no units.
            </div>

        <div class="card-container" v-show="$store.state.currentTeam.unitList.length != 0">  
            <UnitCard v-for="unit in sortedUnits" v-bind:key="unit.id" v-bind:unit="unit"/>
        </div>  
        </div>
      
    </section>

</template>

<script>
import UnitCard from './UnitCard.vue';

export default {
    components: {
        UnitCard
    },
    computed: {
        sortedUnits(){
            let units = this.$store.state.currentTeam.unitList;
            return units.sort((a, b) =>{
                const ranks = {'Leader':1, 'Elite':2, 'Specialist': 3, 'Rank and File': 4, 'Freelancer': 5};
                return ranks[a.rank] - ranks[b.rank] ||a.name.localeCompare(b.name);  
            });
        }
    },
    methods: {
        openNewUnitForm() {
            this.$store.commit('TOGGLE_NEW_UNIT_FORM');
            this.$store.commit('TOGGLE_SHOW_POPUP');
        }
    }
}
</script>

<style>
section.unit-list{
    margin: 0px;
}
</style>