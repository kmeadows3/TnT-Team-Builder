<template>
    <h1 class="section-title">Unit List</h1>
    <div id="unit-list">  
        <UnitCard v-for="unit in this.$store.state.currentTeam.unitList" v-bind:key="unit.id" v-bind:unit="unit"/>
    </div>
</template>

<script>
import UnitCard from './UnitCard.vue';

export default {
    components: {
        UnitCard
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