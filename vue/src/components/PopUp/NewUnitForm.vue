<template>
    <div class="popup-form">
        <h1 class="section-title">New Unit Details</h1>
        <form>
            <span>
                <label for="name">Unit Name: </label>
                <input id="name" type="text" v-model="newUnit.name" />
            </span>
            <span>
                <label for="unitSelect">Unit Type: </label>
                <select id="unitSelect" v-model.number="newUnit.id">
                    <option></option>
                    <option v-for="unit in possibleUnits" :key="unit.id" :value="unit.id">{{ unit.unitClass }} ({{
                        unit.baseCost }} BS)</option>
                </select>
            </span>
            <span>
                <button @click.prevent="buyUnit()">Buy Unit</button>
                <button @click.prevent="clearForm()">Cancel</button>
            </span>

        </form>
    </div>
</template>

<script>
import UnitService from '../../services/UnitService';

export default {
    data() {
        return {
            possibleUnits: [],
            newUnit: {},
        }
    },
    methods: {
        loadPossibleUnits() {
            UnitService.getUnitsForTeam(this.$store.state.currentTeam)
                .then(response => {
                    this.possibleUnits = response.data;
                }).catch(error => this.$store.dispatch('showError', error));
        },
        clearForm() {
            this.newUnit = {};
            this.$store.commit('REMOVE_SHOW_POPUP');
        },
        buyUnit() {
            this.newUnit.teamId = this.$store.state.currentTeam.id;
            UnitService.buyUnit(this.newUnit)
                .then(response => {
                    this.$store.dispatch('loadTeams');
                    this.$store.commit('SET_CURRENT_UNIT', response.data);
                }).catch(error => this.$store.dispatch('showError', error));
            this.clearForm();
        }
    },
    created() {
        this.loadPossibleUnits();
    }
}
</script>

<style></style>