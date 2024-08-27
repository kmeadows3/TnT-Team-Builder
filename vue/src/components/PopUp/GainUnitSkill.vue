<template>
    <div>
        <h1 class="section-title">
            {{ onlyDetriments ? 'Pick Detriments' : (isMutant ? 'Pick Skill or Mutation' : 'Pick Skill') }}
        </h1>
        <div class="radio-tab-wrapper" v-show="isMutant && !onlyDetriments">
            <input type="radio" class="tab" name="filterTab" value="Skill" id="skillTab" checked
                v-model="filter.skillsetCategory" @change="resetPotentialSkill()"/>
            <label for="skillTab">Skills</label>
            <input type="radio" class="tab" name="filterTab" value="Mutation" id="mutatationTab"
                v-model="filter.skillsetCategory" @change="resetPotentialSkill()"/>
            <label for="mutatationTab">Mutations</label>
            <input type="radio" class="tab" name="filterTab" value="Detriment" id="deterimentTab"
                v-model="filter.skillsetCategory" @change="resetPotentialSkill()"/>
            <label for="deterimentTab">Detriments</label>
        </div>

        <div class="gain-skill-container">
            <div class="skill-purchase-info">
                <div class="skillsets" v-show="filter.skillsetCategory=='Skill'"><strong>Available Skillsets: </strong>
                    <span v-for="(skillset, index) in filteredSkillsets"
                        :key="'skillset-list-span-' + skillset.id">
                        {{ index == 0 ? '' : ', ' }}
                        {{ skillset.name }}
                    </span>
                </div>

                <div v-show="filter.skillsetCategory != 'Detriment'">
                    <strong>Remaining Skills
                        <span v-show="isMutant">or Mutations</span>
                        to Purchase: </strong>{{ $store.state.currentUnit.emptySkills }}
                </div>

                <div v-show="$store.state.currentUnit.newPurchase && $store.state.currentUnit.specialRules">
                    <strong>Note for New Units: </strong>{{ $store.state.currentUnit.specialRules }}
                </div>

                <div v-show="filter.skillsetCategory =='Mutation' ">
                    <strong> BS Remaining:</strong> {{ $store.state.currentTeam.money }}
                </div>

                <form>
                    <div class="skill-selection">
                        <div class="finder-label">
                            <h2 class="subsection-title">Lookup {{filter.skillsetCategory}}: </h2>
                            <span  @change="resetPotentialSkill()">
                                <label>Filter: </label>
                                <select v-model.number="filter.skillsetId">
                                    <option selected :value="0">None</option>
                                    <option v-for="skillset in filteredSkillsets"
                                        :key="'skillset-filter-option-' + skillset.id" :value="skillset.id">
                                        {{ skillset.name }} </option>
                                </select>
                            </span>

                        </div>
                        <div class="skill-finder">
                            <select class="skill-name" v-model="newSkill">
                                <option selected disabled :value="{}">
                                    Choose {{filter.skillsetCategory}}
                                </option>
                                <option v-for="potentialSkill in filteredSkills" :key="'potential-skill-'+ potentialSkill.id"
                                    :value="potentialSkill">
                                    {{ potentialSkill.name }} {{ potentialSkill.cost ? '(' + potentialSkill.cost +' BS)' : '' }}</option>
                            </select>
                            <p>
                                {{newSkill.cost ? '(Costs ' + newSkill.cost + ' BS) ':''}}{{ newSkill.description ? newSkill.description : "---" }}
                            </p>

                        </div>
                    </div>
                </form>

            </div>


            <span class="button-container">
                <button @click="addSkill()" :disabled="newSkill.cost > $store.state.currentTeam.money">Gain {{filter.skillsetCategory}}</button>
                <button @click="cancel()">Cancel</button>
            </span>
        </div>



    </div>
</template>

<script>
import UnitService from '../../services/UnitService';

export default {
    data() {
        return {
            potentialSkills: [],
            newSkill: {},
            filter: {
                skillsetCategory: '',
                skillsetId: 0
            },
        }
    },
    computed: {
        filteredSkills() {
            if (!this.filter.skillsetId) {
                return this.potentialSkills.filter(skill => this.skillInCategory(skill));
            } else {
                return this.potentialSkills.filter(skill => skill.skillsetId == this.filter.skillsetId);
            }
        },
        filteredSkillsets() {
            return this.$store.state.currentUnit.availableSkillsets.filter( skillset => skillset.category===this.filter.skillsetCategory);
        },
        isMutant(){
            return this.$store.state.currentUnit.species === "Mutant";
        },
        onlyDetriments(){
            return this.isMutant && this.$store.state.currentUnit.emptySkills <= 0;
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
                    this.cancel();
                })
                .catch(error => this.$store.dispatch('showError', error));
        },
        skillInCategory(skill) {
            return this.filteredSkillsets.filter( skillset => skillset.id == skill.skillsetId).length;
        },
        cancel() {
            this.potentialSkills = [];
            this.newSkill = {};
            this.$store.commit('REMOVE_SHOW_POPUP');
        },
        setDefaultSkillsetCategory(){
            return this.$store.state.currentUnit.emptySkills > 0 ? this.filter.skillsetCategory = 'Skill' : this.filter.skillsetCategory = 'Detriment';
        },
        resetPotentialSkill(){
            this.newSkill = {};
        }
    },
    created() {
        this.getPotentialSkills();
        this.setDefaultSkillsetCategory();
    }
}
</script>



<style scoped>
div.gain-skill-container {
    display: flex;
    flex-direction: column;
    width: 60vw;
    align-items: center;
    justify-content: center;
}

div.skill-purchase-info {
    width: 100%;
    padding: 5px;
    gap: 6px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: start;
    flex-wrap: wrap;
}

div.skillsets {
    width: 100%;
    display: block;
    text-align: start;
}

div.skillsets>span {
    display: inline;
}

div.skill-selection {
    max-width: 100%;
    min-width: 100%
}

div.skill-finder {
    display: flex;
    justify-content: start;
    align-items: center;
    padding-top: 6px;
}

div.skill-finder p {
    padding-left: 6px;
    text-wrap: wrap;
    flex-grow: 1;
    margin: 0px;
}

.skill-name {
    width: 150px;
}

select.skill-name {
    font-size: .8em;
    text-align: center;
}

form {
    width: 100%;
}

div.finder-label {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.radio-tab-wrapper {
    margin-top: var(--wide-padding);
    display: flex;
    border-bottom: var(--thin-border) solid var(--standard-medium);
    padding: 0 var(--wide-padding);
    position: relative;
}

.radio-tab-wrapper>div {
    display: inline-block;
    margin: auto;
    padding-left: var(--wide-padding);
}

input.tab {
    display: none;

    &+label {
        display: flex;
        align-items: center;
        text-align: center;
        cursor: pointer;
        float: left;
        border: 1px solid var(--standard-mid-dark);
        border-bottom: 0;
        background-color: var(--standard-light);
        margin-right: -1px;
        padding: var(--wide-padding);
        position: relative;
        vertical-align: middle;
        border-top-left-radius: var(--border-radius);
        border-top-right-radius: var(--border-radius);

        &:hover {
            background-color: var(--standard-very-light);
        }
    }

    &:checked+label {
        box-shadow: 0 3px 0 -1px var(--section-background),
            inset 0 5px 0 -1px var(--highlight-light);
        background-color: var(--standard-very-light);
        border-color: var(--standard-medium);
        z-index: 1;
    }
}
</style>