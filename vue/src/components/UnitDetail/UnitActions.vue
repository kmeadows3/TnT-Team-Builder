<template>
        <div class="button-container top-row">
            <button @click="toggleShowExp()">Gain Experience</button>
            <button @click="toggleShowAdvance()" v-show="$store.state.currentUnit.unspentExperience >= $store.state.currentUnit.costToAdvance">
                Gain Advance
            </button>
            <button v-show="showGainSkill" @click="pickSkill()">
                {{$store.state.currentUnit.emptySkills > 0 ? ($store.state.currentUnit.species === 'Mutant' ? 'Pick Skill or Mutation' : 'Pick Skill') : 'Pick Detriment'}}
            </button>
            <button @click="addInjury()">Add Injury</button>
            <button @click="removeUnit()" class="danger">Remove Unit</button>

        </div>

</template>

<script>

export default {
    methods: {
        toggleShowExp() {
            this.$store.commit('SET_SHOW_GAIN_EXP_FORM', true);
            this.showPopUp();
        },
        toggleShowAdvance() {
            this.$store.commit('SET_POPUP_SUBFORM', 'AdvanceUnit');
            this.showPopUp();
        },
        pickSkill() {
            this.$store.commit('SET_POPUP_SUBFORM', 'GainUnitSkill');
            this.showPopUp();
        },
        showPopUp() {
            this.$store.commit('TOGGLE_SHOW_POPUP');
        },
        clearForm() {
            this.showAdvance = false;
        },
        removeUnit() {
            this.$store.commit('SET_POPUP_SUBFORM', 'DeleteUnit');
            this.showPopUp();
        }, 
        addInjury() {
            this.$store.commit('SET_POPUP_SUBFORM', 'GainInjury');
            this.showPopUp();
        }
    },
    computed: {
        showGainSkill(){
            return this.$store.state.currentUnit.emptySkills > 0 || (this.$store.state.currentUnit.specialRules.toUpperCase().includes('DETRIMENT') && this.$store.state.currentUnit.newPurchase);
        }
    }

}
</script>

<style scoped>

.button-container {
    margin-top: 0px;
    border-bottom: solid var(--thick-border) var(--border-color);
}

</style>