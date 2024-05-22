<template>
    <TeamList v-if="$store.state.showTeamList" />
    <TeamDetail v-if="$store.state.showTeamDetail" />
    <UnitDetail v-if="$store.state.showUnitDetail" />
</template>


<script>
import TeamList from '../components/TeamList/TeamList.vue';
import TeamDetail from '../components/TeamDetail/TeamDetail.vue';
import UnitDetail from '../components/UnitDetail/UnitDetail.vue';

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

div.table-label {
    text-align: center;
    font-weight: bold;
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


div.inventory {
    display: flex;
    flex-direction: column;
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
    align-items: center;
}

div.inventory>h2{
    margin: 0px;
    padding: 10px;
}

div.item-container {
    width: 85%;
    padding: 0px 20px;
}

div.item-container>h3{
    padding: 10px;
    margin: 0px;
}

div.item-table {
    border: solid 3px black;
    border-radius: 7px;
}

div.item-list {
    border-color: black;
    border-style: solid;
    border-width: 0px 0px 1px 0px;
    border-radius: 2px;
    display: flex;
    min-width: 100%;
}

div.item-table>div.item-list:last-child {
    border-width: 0px;
}

div.item-list>div {
    text-align: center;
    overflow-wrap: break-word;
    border-style: dotted;
    border-width: 0px 1px 0px 0px;
}

div.item-list>div:last-child {
    border-style: none;

}


</style>