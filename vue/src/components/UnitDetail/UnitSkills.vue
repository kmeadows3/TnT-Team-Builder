<template>
    <div>
        <div class="reference">
            <h1 class="section-title">Special Abilities</h1>
            <div v-for="skill in $store.state.unitSkillsSorted" :key="skill.id">
                <h2 class="reference-label">{{ skill.name }}</h2>
                <p class="reference-desc">{{ skill.description }}</p>
            </div>


        </div>


    </div>
</template>


<script>
import UnitService from '../../services/UnitService';

export default {
   
    created() {
        this.$store.dispatch('sortUnitSkills');
    },
}
</script>


<style scoped>
div.skillsets {
    display: inline-block;
    text-align: center;
}

div.skillFinder {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-basis: auto;
    width: 100%;
    gap: 20px;
}

div.skillFinder>select {
    flex-grow: 1;
    max-width: 150px;
}

div.skillFinder>p {
    flex-grow: 3;
    margin: 5px 0px;
}

div.skill-purchase-info {
    width: 100%;
    padding: 5px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

div.purchaseSkills {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-bottom: 0px;
}

form {
    width: 85%;
}

form button {
    margin: auto;
}
</style>