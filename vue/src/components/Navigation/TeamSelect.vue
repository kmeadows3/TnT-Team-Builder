<template>
    <section class="flex-nav">
        <h1>Team Selection</h1>
        <div class="flex-nav-option selected" @click="changeTeam($store.state.currentTeam)">
            {{ $store.state.currentTeam.name }}
        </div>
        <div class="flex-nav-option" v-for="team in filteredTeams" :key="'team-select-' + team.id"
            @click="changeTeam(team)">{{ team.name }}
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