<template>
    <h1 class="section-title">Unit List</h1>
    <div class="controls">
        <button @click="openForm()">Add Unit</button>
    </div>
    <div class="card-container">  
        <UnitCard v-for="unit in this.$store.state.currentTeam.unitList" v-bind:key="unit.id" v-bind:unit="unit"/>
    </div>
</template>

<script>
import UnitCard from './UnitCard.vue';

export default {
    components: {
        UnitCard
    },
    methods: {
        openForm(){
            this.$store.commit('TOGGLE_NEW_UNIT_FORM');
            this.$store.commit('TOGGLE_SHOW_POPUP');
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