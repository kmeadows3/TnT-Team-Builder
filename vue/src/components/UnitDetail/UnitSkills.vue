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
                    <span class="button" v-show="injury.removeable"><i class="bi bi-x-square"  @click="removeTempInjury(injury)" title="Remove"></i> Remove</span>
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
            if (injury.name=='Banged Up'){
                this.$store.state.currentUnit.bangedUp = false;
            }
            if (injury.name=="Long Recovery"){
                this.$store.state.currentUnit.longRecovery = false;
            }

            UnitService.updateUnit(this.$store.state.currentUnit)
                .then(response => {
                    this.$store.commit('SET_CURRENT_UNIT', response.data);
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

p>span.button{
    padding: 3px 2px 1px 3px;
    border-radius: 3px;
    justify-self: end;
    border: 1px black solid;
    cursor: pointer;
    box-shadow: 2px 2px 3px rgba(0, 0, 0, .1),
              2px 2px 5px rgba(0, 0, 0, .1);
}

p>span.button:hover{
    transform: translateY(-1px);
    background-color:#eee;
}

</style>