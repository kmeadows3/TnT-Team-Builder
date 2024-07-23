<template>
    <div class="ref-container">
        <div class="reference special">
            <h1 class="section-title">Special Rules Reference</h1>
            <div v-for="skill in $store.state.unitSkillsSorted" :key="'skill-'+skill.id">
                <h2 class="reference-label">
                    {{ skill.name }}
                    <span>{{ skill.addedString }}</span>
                </h2>
                <p class="reference-desc">{{ skill.description }}</p>
            </div>
        </div>
        <div class="reference injuries" v-show="this.$store.state.unitInjuriesSorted.length">
            <h1 class="section-title">Injuries</h1>
            <div v-for="injury in $store.state.unitInjuriesSorted" :key="'injury-'+ injury.id">
                <h2 class="reference-label">{{ injury.name }} <span v-show="injury.count > 1">(x {{ injury.count }})</span></h2>
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

div.reference.special{
    min-width: calc(50% - 10px);
    flex-grow: 2;
}

div.reference.injuries{
    max-width: calc(50% - 10px);
    min-width: 33%;
    flex-grow: 1;
}

i.bi.bi-x-square {
    border: none;
}

div.reference h2.reference-label{
    flex-direction: column;
}

div.reference h2.reference-label span{
    font-size: .8rem;
    font-weight: normal;
}


</style>