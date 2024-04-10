<template>
    <section class="main-display">
        <h1 class ="page-title">Choose Your Team</h1>
        <div class="team-list">
            <TeamCard v-for ='team in teams' v-bind:key="team.id" v-bind:team='team'/>
        </div>
    </section>
</template>

<script>
import TeamCard from './TeamCard.vue';
import TeamsService from '../services/TeamsService.js';

export default {
    data() {
        return {
            teams: []
        }
    },
    created(){
        TeamsService.retrieveTeamList()
            .then( response => {
                this.teams = response.data;
            })
            .catch( err => {
                console.error(err)
            });
    },
    components: {
        TeamCard
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