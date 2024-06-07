<template>
    <div class="card" @click="toUnitDetail()">
        <h3 class="card-small">{{ unit.rank }}</h3>
        <h1 class="card-title">{{ unit.name }}</h1>
        <h2 class="card-subtitle">{{ unit.unitClass }}</h2>
        <h3 class="card-small">BS cost: {{ unit.bscost }}</h3>

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
