<template>
    <section class="main-display">
        <h1 class ="page-title">Choose Your Team</h1>
        <div class="controls">
            <button @click="$store.commit('TOGGLE_NEW_TEAM_FORM')" v-show="!this.$store.state.showNewTeamForm">New Team</button>
            <NewTeamForm v-show="$store.state.showNewTeamForm"/>
        </div>
        
        <div class="team-list">
            <TeamCard v-for ='team in teams' v-bind:key="team.id" v-bind:team='team'/>
        </div>
    </section>
</template>

<script>
import TeamCard from './TeamCard.vue';
import NewTeamForm from './NewTeamForm.vue';
import TeamsService from '../../services/TeamsService';

export default {
    data() {
        return {
            teams: []
        }
    },
    created(){
        TeamsService.retrieveTeamList()
          .then(response => {
            this.teams = response.data;
          })
          .catch(err => {
            this.$store.dispatch('showHttpError', err);
          });
    },
    components: {
        TeamCard,
        NewTeamForm
    }
}
</script>

<style scoped>

div.team-list {
    display: flex;
    justify-content: space-around;
    width:auto;
    flex-wrap: wrap;
}
</style>