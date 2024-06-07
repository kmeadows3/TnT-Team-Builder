<template>
        <section class="flex-nav">
        <h1>Change Team</h1>
        <div class="flex-nav-option" v-for="team in filteredTeams" :key="'team-select-' + team.id"
            @click="changeTeam(team)">{{ team.name }}
        </div>
    </section>
    <section class="flex-nav" v-show="!$store.state.showTeamDetail">
            <h1>Return to Team</h1>
            <div class="flex-nav-option" @click="changeTeam($store.state.currentTeam)">
                {{ $store.state.currentTeam.name }}
            </div>
    </section>

</template>

<script>
export default {
    computed: {
        filteredTeams() {
            let teams = this.$store.state.teamList;
            teams = teams.filter((team) => team.id != this.$store.state.currentTeam.id);
            return teams;
        }
    },
    methods: {
        changeTeam(team) {
            this.$store.commit('SET_CURRENT_TEAM', team);
        }
    }
}

</script>