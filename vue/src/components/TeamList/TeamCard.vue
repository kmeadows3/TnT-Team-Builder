<template>
    <div class="team card" @click="toTeamDetail()">
        <div class="card-title">
            <h1 class="card-title" :class="{nameless: !team.name}">{{ team.name ? team.name : 'Nameless '+ team.faction }}</h1>
            <h2 class="card-subtitle">Faction: {{ team.faction }}</h2>     
        </div>
        
        <h3 class="card-small">Barter Scrip: {{ team.money }}</h3>
        <h3 class="card-small">Units: {{ team.unitList.length }}</h3>
        <div class="card-footer"></div>
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
