<template>
    <div class="unit-card" @click="toUnitDetail()">
        <h1>{{ unit.name }}</h1>
        <h2>{{ unit.unitClass }}</h2>
        <h2>BS cost: {{ unit.bscost }}</h2>
        <h3>{{ unit.rank }}</h3>
        
    </div>
</template>

<script>
export default {
    props: ['unit'],
    methods: {
        toUnitDetail(){
            this.$store.commit('SET_MANAGE_INVENTORY', false);
            this.$store.commit('SET_CURRENT_UNIT', this.unit);
        }
    }
}
</script>

<style scoped>

div.unit-card {
    border: solid 3px black;
    flex-basis: 21%;
    min-width: 200px;
    max-width: 50%;
    border-radius: 10px;
    padding: 10px;
    margin: 10px;
    text-align: center;
}

h1{
    font-size: 1.8rem;
    margin: 10px;
    text-wrap: wrap;
    overflow-wrap: break-word;
}

h2 {
    font-size: 1.4rem;
    margin: 10px;
}

h3 {
    font-size: 1.2rem;
    margin: 10px;
}
</style>