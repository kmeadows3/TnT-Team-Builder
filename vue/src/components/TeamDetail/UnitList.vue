<template>
    <h1 class="section-title">Unit List</h1>
    <div class="card-container">  
        <UnitCard v-for="unit in sortedUnits" v-bind:key="unit.id" v-bind:unit="unit"/>
    </div>
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
    }
}
</script>

<style>
div#unit-list{
    display: flex;
    justify-content: space-evenly;
    flex-wrap: wrap;
}
</style>