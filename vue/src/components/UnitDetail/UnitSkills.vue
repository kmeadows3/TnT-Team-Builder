<template>
    <div>
        <div class="abilities">
            <h2>Special Abilities</h2>
            <div v-for="skill in $store.state.currentUnit.skills" :key="skill.id">
                <h3>{{ skill.name }}</h3>
                <p>{{ skill.description }}</p>
            </div>
        </div>
        <div class="skillsets"><strong>Available Skillsets: </strong>
            <span v-for="(skillset, index) in $store.state.currentUnit.availableSkillsets" :key="skillset.id">
                {{ index == 0 ? '' : ', ' }}
                {{ skillset.name }}
            </span>
        </div>
        <div class="purchaseSkills" v-show="$store.state.currentUnit.emptySkills > 0">

            <strong>Remaining Skills to Purchase: </strong>{{ $store.state.currentUnit.emptySkills }}
            <form>
                <div class="skillFinder">
                    <label for="skillSelect">Skill </label>
                    <select id="skillSelect" v-model="newSkill">
                        <option value=""></option>
                        <option v-for="potentialSkill in potentialSkills" :key="potentialSkill.id"
                            :value="potentialSkill">{{ potentialSkill.name }} </option>
                    </select>
                    <p>{{ newSkill.description }}</p>
                </div>
                <div>
                    <button @click.prevent="addSkill()">Purchase Skill</button>
                </div>
            </form>
        </div>
    </div>
</template>


<script>
import TeamsService from '../../services/TeamsService';

export default {
    data() {
        return {
            potentialSkills: [],
            newSkill: {}
        }
    },
    methods: {
        getPotentialSkills() {
            TeamsService.getPotentialSkills(this.$store.state.currentUnit.id)
                .then(response => this.potentialSkills = response.data)
                .catch(error => console.error(error));
        },
        addSkill() {
            TeamsService.addSkill(this.$store.state.currentUnit.id, this.newSkill)
                .then(() => {
                    this.$store.dispatch('loadTeams');
                    TeamsService.getUnit(this.$store.state.currentUnit.id)
                        .then(response => this.$store.commit('SET_CURRENT_UNIT', response.data))
                        .catch(error => console.error(error));
                    this.getPotentialSkills();
                }).catch(error => console.error(error));
        }
    },
    created() {
        this.getPotentialSkills();
    },
}
</script>


<style scoped>
div.abilities {
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
}

div.abilities>div {
    display: flex;
    text-align: left;
    gap: 20px;
    padding: 0px;
    border-bottom: solid 1px black;
}

div.abilities>div:last-child{
    border-bottom: 0px;
}

div.abilities>div>h3 {
    min-width: 100px;
    margin: 5px;
    margin-left: 20px;
    text-align: center;
}

div.abilities>div>p {
    min-width: 100px;
    margin: 5px;
    text-align: left;
}

div.skillsets {
    border: solid 3px black;
    border-radius: 7px;
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

div.skillFinder>label {
    flex-grow: 1;
    max-width: 100px;
    text-align: center;
    font-weight: bold;
}

div.skillFinder>select {
    flex-grow: 1;
    max-width: 150px;
}

div.skillFinder>p {
    flex-grow: 3;
}


div.purchaseSkills {
    border: solid 3px black;
    border-radius: 7px;
}
</style>