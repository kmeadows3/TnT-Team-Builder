<template>
    <div class="team card" @click="toTeamDetail()">
        <h1 class="card-title">{{ team.name }}</h1>
        <h2 class="card-subtitle">{{ team.faction }}</h2>
        <h3 class="card-small">Barter Scrip: {{ team.money }}</h3>
        <h3 class="card-small">Units: {{ team.unitList.length }}</h3>
    </div>
    
</template>

<script>

export default {
    props: ['team'],
    methods: {
        toTeamDetail(){
            this.$store.commit('SET_CURRENT_TEAM', this.team);
        }
    }
}

</script>
