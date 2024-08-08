<template>
    <div class="popup-form">
        <h1 class="section-title">New Unit Details</h1>
        <form>
            <span>
                <label for="name">Unit Name: </label>
                <input id="name" type="text" v-model="newUnit.name" />
            </span>
            <span>
                <label for="unitSelect">Unit Type: </label>
                <select id="unitSelect" v-model="previewUnit">
                    <option></option>
                    <option v-for="unit in possibleUnits" :key="'new-unit-select-' + unit.id" :value="unit">{{
                        unit.unitClass }} ({{ unit.rank }}) - {{
                            unit.baseCost }} BS</option>
                </select>
            </span>
            <div class="unit-preview-box" v-show="previewUnit.id">
                <div class="basic-box title">
                    <div class="class">Title</div>
                    <div>Type</div>
                    <div>Defense</div>
                    <div>Wounds</div>
                </div>
                <div class="basic-box">
                    <div class="class">{{ previewUnit.unitClass }}</div>
                    <div>{{ previewUnit.species }}</div>
                    <div>{{ previewUnit.defense }}</div>
                    <div>{{ previewUnit.wounds }}</div>
                </div>
                <div class="basic-box title">
                    <div>Move</div>
                    <div>Melee</div>
                    <div>Ranged</div>
                    <div>Strength</div>
                    <div>Mettle</div>
                </div>
                <div class="basic-box">
                    <div>{{ previewUnit.move }}</div>
                    <div>{{ previewUnit.melee }}</div>
                    <div>{{ previewUnit.ranged }}</div>
                    <div>{{ previewUnit.strength }}</div>
                    <div>{{ previewUnit.mettle }}</div>
                </div>
                <div class="bigger-box">
                    <div class="title">Skillsets</div>
                    <div class="content">
                        <span v-for="(skillset, index) in previewUnit.availableSkillsets" :key="'skillset-id-'+skillset.id">
                            {{skillset.name}}{{index == previewUnit.availableSkillsets.length - 1 ? '' : ',&nbsp;' }}
                        </span>
                    </div>
                </div>
                <div class="bigger-box">
                    <div class="title">Special Ablities</div>
                    <div class="content">
                        <template v-for="(skill, index) in previewUnit.skills" :key="'skill-id-'+skill.id">
                            {{skill.skillsetId !=16 ? skill.name : skill.description }}{{index == previewUnit.skills.length - 1 ? '' : ',&nbsp;' }}
                        </template>
                    </div>
                </div>
                <div class="bigger-box">
                    <div class="title">Starting Skills</div>
                    <div class="content">
                        {{ previewUnit.emptySkills }}
                    </div>
                </div>
                <div class="bigger-box" v-show="previewUnit.specialRules != ''">
                    <div class="title">Purchase Note</div>
                    <div class="content">
                        {{ previewUnit.specialRules }}
                    </div>
                </div>
            </div>
            <span>
                <button @click.prevent="buyUnit()">Buy Unit</button>
                <button @click.prevent="clearForm()">Cancel</button>
            </span>

        </form>
    </div>
</template>

<script>
import UnitService from '../../services/UnitService';

export default {
    data() {
        return {
            possibleUnits: [],
            newUnit: {},
            previewUnit: {},
        }
    },
    methods: {
        loadPossibleUnits() {
            UnitService.getUnitsForTeam(this.$store.state.currentTeam)
                .then(response => {
                    this.possibleUnits = response.data;
                }).catch(error => this.$store.dispatch('showError', error));
        },
        clearForm() {
            this.newUnit = {};
            this.$store.commit('REMOVE_SHOW_POPUP');
        },
        buyUnit() {
            this.newUnit.teamId = this.$store.state.currentTeam.id;
            this.newUnit.id = this.previewUnit.id;
            UnitService.buyUnit(this.newUnit)
                .then(response => {
                    this.$store.dispatch('reloadCurrentTeam');
                    this.$store.commit('SET_CURRENT_UNIT', response.data);
                }).catch(error => this.$store.dispatch('showError', error));
            this.clearForm();
        }
    },
    created() {
        this.loadPossibleUnits();
    }
}
</script>

<style scoped>
div.unit-preview-box div{
    border: solid 1px black;
}

div.unit-preview-box {
    max-width: 50vw;
    min-width: 50vw;
}

div.basic-box {
    width: 100%;
    display: flex;
}

div.basic-box>div {
    display: flex;
    flex-basis: 20%;
    align-items: center;
    justify-content: center;
}

div.basic-box>div.class {
    flex-basis: 40%;
}

div.bigger-box {
    display: flex;
}

div.title {
    font-weight: bold;
}

div.bigger-box > div.title {
    padding-left: 3px;
    padding-right: 3px;
    flex-basis: 25%;
}

div.bigger-box > div.content {
    padding-left: 3px;
    padding-right: 3px;
    flex-basis: 75%;
    max-width: 75%;
    text-wrap: wrap;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
}

</style>