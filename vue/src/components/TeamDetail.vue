<template>
    <section class="main-display">
        <h1 class ="page-title">{{this.$store.state.currentTeam.name}}</h1>
        <div class = "team-box">
            <TeamSummary/>
            <UnitList/>
            <MoneyManagment/>

        </div>
    </section>
</template>

<script>
import UnitList from './UnitList.vue';
import TeamSummary from './TeamSummary.vue';
import MoneyManagment from './MoneyManagement.vue'

export default {
    components: {
        UnitList,
        TeamSummary,
        MoneyManagment
    }
}
</script>

<style>

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


</style>