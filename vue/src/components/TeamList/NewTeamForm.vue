<template>
    <div>
        <form>
            <label for="name">Team Name: </label>
            <input id="name" type="text" v-model="newTeam.name" />
            <label for="factionSelect">Faction: </label>
            <select id="factionSelect" v-model.number="newTeam.factionId">
                <option value=""></option>
                <option v-for="faction in factions" :key="faction.id" :value="faction.factionId">{{ faction.factionName }} </option>
            </select>
            <label for="startingMoney">Starting Money</label>
            <input id="startingMoney" type="number" v-model.number="newTeam.money"/>
            <button @click.prevent="createTeam()">Create Team</button>
            <button @click.prevent="clearForm()">Cancel</button>
        </form>
    </div>
</template>

<script>
import TeamsService from '../../services/TeamsService';

export default {
    data() {
        return {
            factions: [],
            newTeam: {},
        }
    },
    methods: {
        loadFactions() {
            TeamsService.getFactionList()
                .then(response => {
                    this.factions = response.data;
                }).catch ( error => this.$store.dispatch('showHttpError', error));
        },
        clearForm(){
            this.newTeam = {};
            this.$store.commit('TOGGLE_NEW_TEAM_FORM');
        },
        createTeam(){
            this.newTeam.userId = this.$store.state.user.id;
            TeamsService.createNewTeam(this.newTeam)
                .then(response => {
                    this.$store.dispatch('loadTeams');
                    this.$store.commit('SET_CURRENT_TEAM', response.data);
                }).catch (error => this.$store.dispatch('showHttpError', error));
            this.clearForm();
        }
    },
    created() {
        this.loadFactions();
    }
}
</script>