<template>
        <div v-show="!showExp && !showAdvance">
            <button @click="toggleShowExp()">Gain Experience</button>
            <button @click="toggleShowAdvance()" v-show="$store.state.currentUnit.unspentExperience >= $store.state.currentUnit.costToAdvance">Gain Advance</button>
            <button>Add Injury</button>
        </div>
        <div class="gain-exp" v-show="showExp">
            <form>
                <label for="expToGain">Experience Gained </label>
                <input type="number" id="expToGain" v-model.number="expGained" />
                <button @click.prevent="gainExp(expGained)">Gain</button>
                <button @click.prevent="clearForm()">Cancel</button>
            </form>
        </div>
        <div>
            <AdvanceUnit v-show="showAdvance" @exit-advance="toggleShowAdvance()"/>
        </div>
        
</template>

<script>
import TeamsService from '../../services/TeamsService';
import AdvanceUnit from './AdvanceUnit.vue';

export default {
    data() {
        return {
            showExp: false,
            showAdvance: false,
            expGained: ''
        }
    },
    components: {
        AdvanceUnit
    },
    methods: {
        toggleShowExp() {
            this.showExp = !this.showExp
        },
        toggleShowAdvance() {
            this.showAdvance = !this.showAdvance
        },
        clearForm() {
            this.expGained = '';
            this.showExp = false;
            this.showAdvance = false;
        },
        gainExp(experience) {
            try {
                this.$store.commit('GAIN_UNSPENT_EXP', experience);
                TeamsService.updateUnit(this.$store.state.currentUnit)
                    .then(response => {
                        this.$store.commit('SET_CURRENT_UNIT', response.data);
                    }).catch(error => console.error(error));
            } catch (error) {
                console.error(error);
            }
            this.clearForm();
        }
    }

}
</script>