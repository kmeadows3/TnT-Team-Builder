<template>
    <section class="main-display">
        <h1 class ="page-title">Choose Your Team</h1>
        <div class="controls">
            <button @click="$store.commit('TOGGLE_NEW_TEAM_FORM')" v-show="!this.$store.state.showNewTeamForm">New Team</button>
            <NewTeamForm v-if="$store.state.showNewTeamForm"/>
        </div>
        
        <div class="card-container">
            <TeamCard v-for ='team in $store.state.teamList' v-bind:key="team.id" v-bind:team='team'/>
        </div>
    </section>
</template>

<script>
import TeamCard from './TeamCard.vue';
import NewTeamForm from './NewTeamForm.vue';
import TeamsService from '../../services/TeamsService';

export default {
    created(){
        TeamsService.retrieveTeamList()
          .then(response => {
            this.$store.commit('SET_TEAM_LIST', response.data);
          })
          .catch(err => {
            this.$store.dispatch('showError', err);
          });
    },
    components: {
        TeamCard,
        NewTeamForm
    }
}
</script>