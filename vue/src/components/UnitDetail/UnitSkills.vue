<template>
    <div>
        <div class="reference">
            <h1 class="section-title">Special Abilities</h1>
            <div v-for="skill in $store.state.unitSkillsSorted" :key="skill.id">
                <h2 class="reference-label">{{ skill.name }}</h2>
                <p class="reference-desc">{{ skill.description }}</p>
            </div>

            <div class="purchaseSkills" v-if="$store.state.currentUnit.emptySkills > 0">
                <h2 class="subsection-title">Select New Skill</h2>
                <div class="skill-purchase-info">
                    <div>
                        <strong>Remaining Skills to Purchase: </strong>{{ $store.state.currentUnit.emptySkills }}
                    </div>
                    <div class="skillsets"><strong>Available Skillsets: </strong>
                        <span v-for="(skillset, index) in $store.state.currentUnit.availableSkillsets"
                            :key="skillset.id">
                            {{ index == 0 ? '' : ', ' }}
                            {{ skillset.name }}
                        </span>
                    </div>
                </div>
                <form>
                    <strong>Skill Options:</strong>
                    <div class="skillFinder">
                        <select id="skillSelect" v-model="newSkill">
                            <option value=""></option>
                            <option v-for="potentialSkill in potentialSkills" :key="potentialSkill.id"
                                :value="potentialSkill">{{ potentialSkill.name }} </option>
                        </select>
                        <p>{{ newSkill.description }}</p>
                    </div>
                    <button @click.prevent="addSkill()">Purchase Skill</button>
                </form>



            </div>
        </div>


    </div>
</template>


<script>
import UnitService from '../../services/UnitService';

export default {
    data() {
        return {
            potentialSkills: [],
            newSkill: {}
        }
    },
    methods: {
        getPotentialSkills() {
            UnitService.getPotentialSkills(this.$store.state.currentUnit.id)
                .then(response => this.potentialSkills = response.data)
                .catch(error => this.$store.dispatch('showError', error));
        },
        addSkill() {
            UnitService.addSkill(this.$store.state.currentUnit.id, this.newSkill)
                .then(() => {
                    this.$store.dispatch('reloadCurrentUnit');
                    this.getPotentialSkills();
                })
                .catch(error => this.$store.dispatch('showError', error));
        },
    },
    created() {
        this.getPotentialSkills();
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