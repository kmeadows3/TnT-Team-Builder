<template>
    <section class="main-display">
        <h1 class ="page-title">Choose Your Team</h1>
        <div class="button-container">
            <button @click="openForm()">Create New Team</button>
        </div>
        
        <div class="card-container">
            <TeamCard v-for ='team in $store.state.teamList' v-bind:key="team.id" v-bind:team='team'/>
        </div>
    </section>
</template>

<script>
import TeamCard from './TeamCard.vue';
import TeamsService from '../../services/TeamsService';

export default {
    created(){
        TeamsService.retrieveTeamList()
          .then(response => {
            this.$store.commit('SET_TEAM_LIST', response.data);
          })
          .catch(err => {
            if (err.response && err.response.status == 401){
                this.$store.commit("LOGOUT");
                this.$router.push("/login");
            } else {
                this.$store.dispatch('showError', err);
            }
            
          });
    },
    components: {
        TeamCard
    },
    methods: {
        openForm(){
            this.$store.commit('TOGGLE_NEW_TEAM_FORM');
            this.$store.commit('TOGGLE_SHOW_POPUP');
        }
    }
}
</script>