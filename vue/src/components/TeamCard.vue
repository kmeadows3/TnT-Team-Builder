<template>
    <div class = "team-card" @click="toTeamDetail()">
        <h2>{{ team.name }}</h2>
        <h3>{{ team.faction }}</h3>
        <p>Barter Scrip: {{ team.money }}</p>
        <p>Units: {{ team.unitList.length }}</p>
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

<style>
div.team-card {
    border: solid 3px black;
    min-width: 300px;
    border-radius: 10px;
    padding: 10px;
    margin: 10px;
    text-align: center;
}

div.team-card h2{
    margin: 5px;
}
</style>