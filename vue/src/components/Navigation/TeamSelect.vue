<template>
        <h2 class="subsection-title">Team Selection</h2>
        <div class="flex-nav-option selected" @click="changeTeam($store.state.currentTeam)">
            {{ $store.state.currentTeam.name }}
        </div>
        <div class="flex-nav-option" v-for="team in filteredTeams" :key="'team-select-' + team.id"
            @click="changeTeam(team)">{{ team.name }}
        </div>

</template>

<script>
export default {
    computed: {
        filteredTeams() {
            let teams = this.$store.state.teamList;
            if (teams.length){
                teams = teams.filter((team) => team.id != this.$store.state.currentTeam.id);
            }
            return teams;
        }
    },
    methods: {
        changeTeam(team) {
            this.$store.commit('SET_CURRENT_TEAM', team);
            this.$store.commit('CLEAR_CURRENT_UNIT', team);
        }
    }
}

</script>