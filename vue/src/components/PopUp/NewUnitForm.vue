<template>
    <div class="popup-form">
        <h1 class="section-title">New Unit Details</h1>
        <form>
            <span>
                <label for="name">Unit Name: </label>
                <input id="name" type="text" v-model="newUnit.name" />
            </span>
            <div class="dropdowns">
                <label for="unitSelect">Unit Type: </label>
                <select id="unitSelect" v-model="previewUnit">
                        <option></option>
                        <option v-for="unit in filteredUnits" :key="'new-unit-select-' + unit.id" :value="unit">{{
                            unit.unitClass }} ({{ unit.rank }}) - {{
                                unit.baseCost }} BS</option>

                </select>
                <span v-show="!buyLeaders">
                    <label for="filter">Filter Units:</label>
                <select id="filter" v-model="filter">
                        <option selected :value="''">None</option>
                        <option :value="'Leader'" v-show="buyLeaders">Leaders</option>
                        <option :value="'Elite'" v-show="buyElites">Elites</option>
                        <option :value="'Specialist'" v-show="buySpecialists">Specialists</option>
                        <option :value="'Rank and File'">Rank and File</option>
                        <option :value="'Freelancer'" v-show="buyFreelancers">Freelancers</option>

                </select>
                </span>
                
            </div>
            <UnitPreview :preview-unit="previewUnit" v-if="previewUnit.id" />
            <span>
                <button @click.prevent="buyUnit()">Buy Unit</button>
                <button @click.prevent="clearForm()">Cancel</button>
            </span>

        </form>
    </div>
</template>

<script>
import UnitService from '../../services/UnitService';
import UnitPreview from './UnitPreview.vue';

export default {
    data() {
        return {
            possibleUnits: [],
            newUnit: {},
            previewUnit: {},
            filter: '',
        }
    },
    components: {
        UnitPreview
    },
    computed: {
        filteredUnits() {
            if (!this.filter) {
                return this.possibleUnits;
            } else {
                return this.possibleUnits.filter(possibleUnit => possibleUnit.rank == this.filter);
            }
        },
        buyLeaders() {
            return this.canBuyRank('Leader');
        },
        buyElites() {
            return this.canBuyRank('Elite');
        },
        buySpecialists() {
            return this.canBuyRank('Specialist');
        },
        buyFreelancers() {
            return this.canBuyRank('Freelancer');
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
            this.newUnit.id = this.previewUnit.id;
            UnitService.buyUnit(this.newUnit)
                .then(response => {
                    this.$store.dispatch('reloadCurrentTeam');
                    this.$store.commit('SET_CURRENT_UNIT', response.data);
                }).catch(error => this.$store.dispatch('showError', error));
            this.clearForm();
        },
        canBuyRank(rank) {
            let teamCanPurchase = false;

            this.possibleUnits.forEach(unit => {
                if (unit.rank == rank) {
                    teamCanPurchase = true;
                }
            });

            return teamCanPurchase;
        }
    },
    created() {
        this.loadPossibleUnits();
    }
}
</script>

<style scoped>
div.dropdowns {
    display: flex;
    font-size: .8em;
}

div.dropdowns>label {
    font-size: 1rem;
}

</style>