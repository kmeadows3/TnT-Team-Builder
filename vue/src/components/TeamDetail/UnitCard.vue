<template>
    <div class="card" @click="toUnitDetail()">
        <div class="card-title">
           <h1 class="card-title" :class="{nameless: !unit.name}">{{ unit.name? unit.name : 'Nameless ' + unit.unitClass }}</h1>
            <h2 class="card-subtitle">{{ unit.unitClass }}</h2> 
        </div>
        
        <h3 class="card-small">{{ unit.rank }}</h3>
        <h3 class="card-small">BS cost: {{ unit.bscost }}</h3>
        <div class="card-footer"></div>

    </div>
</template>

<script>
export default {
    props: ['unit'],
    methods: {
        toUnitDetail() {
            this.$store.commit('SET_MANAGE_INVENTORY', false);
            this.$store.commit('SET_CURRENT_UNIT', this.unit);
        }
    }
}
</script>
