<template>
    <div class="ref-container">
        <div class="reference">
            <h1 class="section-title">Special Abilities</h1>
            <div v-for="skill in $store.state.unitSkillsSorted" :key="'skill-'+skill.id">
                <h2 class="reference-label">{{ skill.name }}</h2>
                <p class="reference-desc">{{ skill.description }}</p>
            </div>
        </div>
        <div class="reference" v-show="this.$store.state.unitInjuriesSorted.length">
            <h1 class="section-title">Injuries</h1>
            <div v-for="injury in $store.state.unitInjuriesSorted" :key="'injury-'+ injury.id">
                <h2 class="reference-label">{{ injury.name }}</h2>
                <p class="reference-desc">{{ injury.description }}</p>
            </div>
        </div>
    </div>
</template>


<script>
export default {
   
    created() {
        this.$store.dispatch('sortUnitSkills');
        this.$store.dispatch('sortInjuries');
    },
}
</script>


<style scoped>

div.ref-container {
    display: flex;
}

div.reference {
    min-width: 50%;
    flex-grow: 1;
}

</style>