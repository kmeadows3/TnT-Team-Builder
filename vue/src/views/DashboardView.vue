<template>
    <TeamList v-if="$store.state.showTeamList"/>
</template>


<script>
import TeamList from '../components/TeamList.vue';

export default {
    data (){
        return {
        }
    },
    components: {
        TeamList
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
    section.main-display{
        border: solid 3px black;
        border-radius: 7px;
    }
</style>