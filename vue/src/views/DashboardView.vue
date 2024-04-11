<template>
    <TeamList v-if="$store.state.showTeamList"/>
    <TeamDetail v-if="$store.state.showTeamDetail"/>
    <UnitDetail v-if="$store.state.showUnitDetail" />
</template>


<script>
import TeamList from '../components/TeamList.vue';
import TeamDetail from '../components/TeamDetail.vue';
import UnitDetail from '../components/UnitDetail.vue';

export default {
    data (){
        return {
        }
    },
    components: {
        TeamList,
        TeamDetail,
        UnitDetail
    }

}
</script>

<style>
    h1.page-title{
        text-align: center;
        font-size: 3rem;
        margin: auto;
        width: 80vw;
        border: solid 3px black;
        border-radius: 7px;
        margin-top: 10px;

    }
    h1.section-title{
        text-align: center;
        font-size: 2rem;
        margin: auto;
        width: 98%;
        margin-top: 10px;

    }
    section.main-display{
        border: solid 3px black;
        border-radius: 7px;
    }
    
</style>