<template>
    <section class="main-display">
        <h1 class="page-title" v-show="!showChangeNameForm">
            <span>{{this.$store.state.currentTeam.name}}</span>
            <i class="bi bi-pencil-square" @click="toggleEditName()"></i>
        </h1>
        <h1 class="page-title" v-show="showChangeNameForm">
            <form>
                <input type="text" id="changeName" v-model="teamName">
            </form>
            <span class="action-buttons">
            <i class="bi bi-check-circle" @click="changeName()"></i>
            <i class="bi bi-x-square" @click="resetName()"></i>
        </span>
        </h1>
        <div class="team-box">
            <TeamSummary/>
            <UnitList/>
            <MoneyManagment/>
        </div>
    </section>
</template>

<script>
import UnitList from './UnitList.vue';
import TeamSummary from './TeamSummary.vue';
import MoneyManagment from './MoneyManagement.vue';
import TeamsService from '../services/TeamsService';

export default {
    data() {
        return {
            showChangeNameForm:false,
            teamName: '',
        }
    },
    components: {
        UnitList,
        TeamSummary,
        MoneyManagment
    },
    methods: {
        toggleEditName(){
            this.showChangeNameForm = !this.showChangeNameForm;
        },
        resetName(){
            this.teamName = this.$store.state.currentTeam.name;
            this.toggleEditName();
        },
        changeName(){
            this.$store.commit('CHANGE_TEAM_NAME', this.teamName);
            TeamsService.updateTeam(this.$store.state.currentTeam)
                .then( response => {
                    this.$store.commit('SET_CURRENT_TEAM', response.data);
                }).catch(error => console.error(error));
            this.toggleEditName();
        }
    },
    beforeMount() {
        this.teamName = this.$store.state.currentTeam.name;
    }
}
</script>

<style scoped>

div.team-box {
    border: solid 3px black;
    border-radius: 7px;
    margin: 7px;
}

div.controls{
    border: solid 1px black;
    text-align: center;
}

div.controls>button{
    margin: 10px;
}

h1.page-title {
    display: flex;
    justify-content: center;
    align-items: center;
}

h1.page-title span{
    padding-right: 20px;
}

span.action-buttons{
    display: flex;
    flex-direction: column;
}

span.action-buttons i {
    font-size: 1.1rem;
    cursor: pointer;
    padding: 1px 3px;
    margin: 2px;
    border: solid 1px black;
    border-radius: 5px;
}

i.bi-pencil-square {
    font-size: 1.2rem;
    cursor: pointer;
    padding: 3px 4px;
    border: solid 1px black;
    border-radius: 5px;
}



.main-display form {
    display: flex;
    justify-content: center;
    align-items: center;
}

input#changeName {
    margin: 3px;
    padding: 2px 20px;
    font-size: .9em;
    font-style: italic;
    font-weight: 100;
}

</style>