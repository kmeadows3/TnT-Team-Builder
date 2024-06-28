<template>
    <section class="main-display">
        <h1 class="page-title name-display" v-show="!showChangeNameForm">
            <span>{{ this.$store.state.currentTeam.name }}</span>
            <i class="bi bi-pencil-square button" @click="toggleEditName()" title="Edit Name"></i>
        </h1>
        <h1 class="page-title name-entry" v-show="showChangeNameForm">
            <form>
                <input type="text" id="changeName" v-model="teamName">
            </form>
            <span class="name-change-buttons">
                <i class="bi bi-check-circle button" @click="changeName()" title="Submit"></i>
                <i class="bi bi-x-square button" @click="resetName()" title="Cancel"></i>
            </span>
        </h1>
        <div class="button-container">
            <button @click="openNewUnitForm()">Add Unit</button>
            <button @click="toggleShowGainForm()">Gain Money</button>
            <button @click="toggleShowLossForm()">Lose Money</button>
            <button @click="payUpkeep()">Pay Upkeep</button>
        </div>
        <div class="team-box">
            <TeamSummary />
            <UnitList />
                </div>

            <TeamInventory />
    </section>
</template>

<script>
import UnitList from './UnitList.vue';
import TeamSummary from './TeamSummary.vue';
import TeamInventory from './TeamInventory.vue';
import TeamsService from '../../services/TeamsService';

export default {
    data() {
        return {
            showChangeNameForm: false,
            teamName: '',
        }
    },
    components: {
        UnitList,
        TeamSummary,
        TeamInventory
    },
    methods: {
        toggleEditName() {
            this.showChangeNameForm = !this.showChangeNameForm;
        },
        resetName() {
            this.teamName = this.$store.state.currentTeam.name;
            this.toggleEditName();
        },
        changeName() {
            this.$store.commit('CHANGE_TEAM_NAME', this.teamName);
            TeamsService.updateTeam(this.$store.state.currentTeam)
                .then(response => {
                    this.$store.commit('SET_CURRENT_TEAM', response.data);
                    this.$store.dispatch('loadTeams');
                }).catch(error => this.$store.dispatch('showError', error));
            this.toggleEditName();
        },
        payUpkeep() {
            const upkeep = this.$store.state.currentTeam.upkeep;
            try {
                this.$store.commit('LOSE_MONEY', upkeep);
                TeamsService.updateTeam(this.$store.state.currentTeam)
                    .then(response => {
                        this.$store.commit('SET_CURRENT_TEAM', response.data);
                    })
                    .catch(error => this.$store.dispatch('showError', error))
            } catch (error) {
                this.$store.dispatch('showError', error);
            }
        },
        toggleShowGainForm() {
            this.$store.commit('SET_SHOW_GAIN_MONEY_FORM', true);
            this.$store.commit('TOGGLE_SHOW_POPUP');
        },
        toggleShowLossForm() {
            this.$store.commit('SET_SHOW_LOSE_MONEY_FORM', true);
            this.$store.commit('TOGGLE_SHOW_POPUP');
        },
        openNewUnitForm() {
            this.$store.commit('TOGGLE_NEW_UNIT_FORM');
            this.$store.commit('TOGGLE_SHOW_POPUP');
        }

    },
    beforeMount() {
        this.teamName = this.$store.state.currentTeam.name;
    }
}
</script>

<style scoped>
div.team-box {
    border: solid 3px black;
    border-radius: 7px;
    margin: 3px
}

div.controls {
    border: solid 1px black;
    text-align: center;
}

div.controls>button {
    margin: 10px;
}

h1.page-title {
    display: flex;
    justify-content: center;
    align-items: center;
}

h1.page-title span {
    padding-right: 20px;
}

.main-display form {
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>