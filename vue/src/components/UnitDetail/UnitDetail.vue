<template>
    <UnitBasics/>
    <UnitStats/>
    <UnitSkills/>
    <UnitExperience />
    <UnitActions />
</template>

<script>
import UnitBasics from './UnitBasics.vue';
import UnitStats from './UnitStats.vue';
import UnitExperience from './UnitExperience.vue';
import UnitSkills from './UnitSkills.vue';
import UnitActions from './UnitActions.vue';

export default {
    components: {
        UnitBasics,
        UnitStats,
        UnitExperience,
        UnitSkills,
        UnitActions
    }
}
</script>

<style scoped>

</style>