<template>
    <div>
        <form>
            <label for="name">Unit Name: </label>
            <input id="name" type="text" v-model="newUnit.name" />
            <label for="unitSelect">Unit Type: </label>
            <select id="unitSelect" v-model.number="newUnit.id">
                <option></option>
                <option v-for="unit in possibleUnits" :key="unit.id" :value="unit.id">{{ unit.unitClass }} ({{
            unit.baseCost }} BS)</option>
            </select>
            <button @click.prevent="buyUnit()">Buy Unit</button>
            <button @click.prevent="clearForm()">Cancel</button>
        </form>
    </div>
</template>

<script>
import TeamsService from '../services/TeamsService';

export default {
    data() {
        return {
            possibleUnits: [],
            newUnit: {},
        }
    },
    methods: {
        loadPossibleUnits() {
            TeamsService.getUnitsForTeam(this.$store.state.currentTeam)
                .then(response => {
                    this.possibleUnits = response.data;
                }).catch ( error => console.error(error));
        },
        clearForm(){
            this.newUnit = {};
            this.$store.commit('TOGGLE_NEW_UNIT_FORM');
        },
        buyUnit(){
            this.newUnit.teamId = this.$store.state.currentTeam.id;
            TeamsService.buyUnit(this.newUnit)
                .then(response => {
                    this.$store.dispatch('loadTeams');
                    this.$store.commit('SET_CURRENT_UNIT', response.data);
                }).catch (error => console.error(error.response.data.message));
            this.clearForm();
        }
    },
    created() {
        this.loadPossibleUnits();
    }
}
</script>