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
                <p class="reference-desc">{{ injury.description }}
                    <i class="bi bi-x-square button"  v-show="injury.removable" @click="removeTempInjury(injury)" title="Remove"></i>
                </p>              
            </div>
        </div>
    </div>
</template>


<script>
import UnitService from '../../services/UnitService';

export default {
   
    created() {
        this.$store.dispatch('sortUnitSkills');
        this.$store.dispatch('sortInjuries');
    },
    methods: {
        removeTempInjury(injury){
            UnitService.removeInjury(injury.id, this.$store.state.currentUnit.id)
                .then(response => {
                    this.$store.dispatch('reloadCurrentUnit');
                }).catch(error => {
                    this.$store.dispatch('showError', error);
                });
        }
    }
}
</script>


<style scoped>

div.ref-container {
    display: flex;
}

div.reference {
    min-width: calc(50% - 10px);
    flex-grow: 1;
}

i.bi.bi-x-square {
    border: none;
}


</style>