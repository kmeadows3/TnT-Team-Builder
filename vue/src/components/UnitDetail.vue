<template>
    <UnitBasics/>
    <UnitStats/>
    <UnitSkills/>
    <UnitExperience />
</template>

<script>
import UnitBasics from './UnitBasics.vue';
import UnitStats from './UnitStats.vue';
import UnitExperience from './UnitExperience.vue';
import UnitSkills from './UnitSkills.vue';

export default {
    components: {
        UnitBasics,
        UnitStats,
        UnitExperience,
        UnitSkills
    }
}
</script>

<style scoped>

</style>