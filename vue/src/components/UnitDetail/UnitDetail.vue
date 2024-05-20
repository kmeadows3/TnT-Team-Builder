<template>
    <UnitBasics/>
    <UnitStats/>
    <UnitInventory/>
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
import UnitInventory from './UnitInventory.vue';

export default {
    components: {
        UnitBasics,
        UnitStats,
        UnitExperience,
        UnitSkills,
        UnitActions,
        UnitInventory
    }
}
</script>

<style scoped>

</style>