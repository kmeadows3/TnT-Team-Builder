<template>
    <h1 class="section-title">Unit List</h1>
    <div class="controls">
        <button @click="$store.commit('TOGGLE_NEW_UNIT_FORM')" v-show="!this.$store.state.showNewUnitForm">Add Unit</button>
        <NewUnitForm v-show="this.$store.state.showNewUnitForm"/>
    </div>
    <div id="unit-list">  
        <UnitCard v-for="unit in this.$store.state.currentTeam.unitList" v-bind:key="unit.id" v-bind:unit="unit"/>
    </div>
</template>

<script>
import UnitCard from './UnitCard.vue';
import NewUnitForm from './NewUnitForm.vue';

export default {
    components: {
        UnitCard,
        NewUnitForm
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