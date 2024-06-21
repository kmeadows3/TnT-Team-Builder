<template>
    <div>
        <div v-show="$store.state.showGainMoneyForm" class="popup-form">
            <h1 class="section-title">Gain Money</h1>
            <form>
                <span><label for="bsGained">BS Gained: </label>
                    <input id="bsGained" type="number" v-model.number="bsGained" /></span>

                <span>
                    <button @click.prevent="gainMoney(bsGained)">Gain Barter Scrip</button>
                    <button @click.prevent="clearForm()">Cancel</button>
                </span>

            </form>
        </div>
        <div v-show="$store.state.showLoseMoneyForm" class="popup-form">
            <h1 class="section-title">Lose Money</h1>
            <form>
                <span>
                    <label for="bsGained">BS Lost: </label>
                    <input id="bsGained" type="number" v-model.number="bsLost" />

                </span>
                <span>
                    <button @click.prevent="loseMoney(bsLost)">Lose Barter Scrip</button>
                    <button @click.prevent="clearForm()">Cancel</button>
                </span>

            </form>
        </div>
        <div v-show="$store.state.showGainExpForm" class="popup-form">
            <h1 class="section-title">Gain Experience</h1>
            <form>
                <span>
                    <label for="expToGain">Experience Gained </label>
                    <input type="number" id="expToGain" v-model.number="expGained" />
                </span>
                <span>
                    <button @click.prevent="gainExp(expGained)">Gain</button>
                    <button @click.prevent="clearForm()">Cancel</button>
                </span>
            </form>
        </div>

    </div>
</template>

<script>

import TeamsService from '../../services/TeamsService';
import UnitService from '../../services/UnitService';

export default {
    data() {
        return {
            showGainForm: false,
            showLossForm: false,
            bsGained: '',
            bsLost: '',
            expGained: ''
        }
    },
    methods: {
        payUpkeep() {
            const upkeep = this.$store.state.currentTeam.upkeep;
            try {
                this.$store.commit('LOSE_MONEY', upkeep);
                this.updateTeam()
            } catch (error) {
                this.$store.commit('SHOW_ERROR_ON', error);
            }
        },
        loseMoney(money) {
            if (money > 0) {
                try {
                    this.$store.commit('LOSE_MONEY', money);
                    this.updateTeam();
                } catch (error) {
                    this.$store.commit('SHOW_ERROR_ON', error);
                }
            } else {
                this.$store.commit('SHOW_ERROR_ON', 'You may not lose negative money.');
            }
            this.clearForm();
        },
        gainMoney(money) {
            if (money > 0) {
                this.$store.commit('GAIN_MONEY', money);
                this.updateTeam();
            } else {
                this.$store.dispatch('showError', 'You must gain positive money.')
            }
            this.clearForm();
        },
        updateTeam() {
            TeamsService.updateTeam(this.$store.state.currentTeam)
                .then(response => {
                    this.$store.commit('SET_CURRENT_TEAM', response.data);
                })
                .catch(error => this.$store.dispatch('showError', error))
        },
        gainExp(experience) {
            try {
                this.$store.commit('GAIN_UNSPENT_EXP', experience);
                UnitService.updateUnit(this.$store.state.currentUnit)
                    .then(response => {
                        this.$store.commit('SET_CURRENT_UNIT', response.data);
                    }).catch(error => {
                        this.$store.dispatch('showError', error);
            });
            } catch (error) {
                this.$store.dispatch('showError', error);
            }
            this.clearForm();
        },
        clearForm() {
            this.bsGained = '';
            this.bsLost = '';
            this.expGained = '';
            this.$store.commit('REMOVE_SHOW_POPUP');
        }
    }
}
</script>