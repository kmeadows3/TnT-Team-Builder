<template>
    <TeamList v-if="$store.state.showTeamList" />
    <TeamDetail v-if="$store.state.showTeamDetail" />
    <UnitDetail v-if="$store.state.showUnitDetail" />
</template>


<script>
import TeamList from '../components/TeamList.vue';
import TeamDetail from '../components/TeamDetail.vue';
import UnitDetail from '../components/UnitDetail.vue';

export default {
    data() {
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
    h1.page-title {
        text-align: center;
        font-size: 3rem;
        margin: auto;
        width: 80vw;
        border: solid 3px black;
        border-radius: 7px;
        margin-top: 10px;
    }

    h1.section-title {
        text-align: center;
        font-size: 2rem;
        margin: auto;
        width: 98%;
        margin-top: 10px;

    }

    section.main-display {
        border: solid 3px black;
        border-radius: 7px;
    }

    span.action-buttons {
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

    input#changeName {
    margin: 3px;
    padding: 2px 20px;
    font-size: .9em;
    font-style: italic;
    font-weight: 100;
    text-align: center;
}
</style>