<template>
    <div class="controls" v-show="!showGainForm && !showLossForm">
        <button @click="toggleShowGainForm">Gain Money</button>
        <button @click="toggleShowLossForm">Lose Money</button>
        <button @click="payUpkeep()">Pay Upkeep</button>
    </div>
    <div>
        <form v-show="showGainForm">
            <label for="bsGained">BS Gained: </label>
            <input id="bsGained" type="number" v-model.number="bsGained"/>
            <button @click.prevent="gainMoney(bsGained)">Gain Barter Scrip</button>
            <button @click.prevent="clearForm()">Cancel</button>
        </form>
        <form v-show="showLossForm">
            <label for="bsGained">BS Lost: </label>
            <input id="bsGained" type="number" v-model.number="bsLost"/>
            <button @click.prevent="loseMoney(bsLost)">Lose Barter Scrip</button>
            <button @click.prevent="clearForm()">Cancel</button>
        </form>
    </div>
</template>

<script>

    import TeamsService from '../../services/TeamsService';

    export default {
        data() {
            return {
                showGainForm: false,
                showLossForm: false,
                bsGained: '',
                bsLost: ''
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
            loseMoney(money){
                if (money > 0) {
                    try {
                    this.$store.commit('LOSE_MONEY', money);
                    this.updateTeam();
                    } catch (error){
                        this.$store.commit('SHOW_ERROR_ON', error);
                    }    
                } else {
                    this.$store.commit('SHOW_ERROR_ON', 'You may not lose negative money.');
                }
                this.clearForm();
            },
            gainMoney(money){
                if (money > 0) {
                    this.$store.commit('GAIN_MONEY', money);
                    this.updateTeam();
                } else {
                    this.$store.dispatch('showError', 'You must gain positive money.')
                }
                this.clearForm();
            },
            updateTeam(){
                TeamsService.updateTeam(this.$store.state.currentTeam)
                    .then( response => {
                        this.$store.commit('SET_CURRENT_TEAM', response.data);
                    })
                    .catch(error => this.$store.dispatch('showError', error))
            },
            clearForm(){
                this.bsGained='';
                this.bsLost='';
                this.showGainForm=false;
                this.showLossForm=false;
            },
            toggleShowGainForm(){
                this.showGainForm = !this.showGainForm;
            },
            toggleShowLossForm(){
                this.showLossForm = !this.showLossForm;
            }
        }
    }
</script>