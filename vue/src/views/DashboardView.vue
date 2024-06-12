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

/* 
GENERAL
*/

section.main-display {
    border: solid 3px black;
    border-radius: 7px;
    min-width: 200px;
    flex-basis: 85;
    flex-grow: 15;
}

form {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
}



input#changeName {
    margin: 3px;
    padding: 2px 20px;
    font-size: 1.4rem;
    font-style: italic;
    font-weight: 100;
    text-align: center;
    align-self: center;
}

/*
TITLES
*/

h1.page-title {
    text-align: center;
    font-size: 3rem;
    margin: auto;
    width: 80%;
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

h2.subsection-title {
    margin: 0px;
    padding: 10px;
}


/*
Div Table
*/
div.table-label {
    text-align: center;
    font-weight: bold;
}

div.table-label>div {
    padding: 1px;
    align-content: center;
}


/*
CARDS
*/

div.card-container {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
}

div.card {
    border: solid 3px black;
    flex-basis: 21%;
    flex-grow: 1;

    min-width: 200px;
    max-width: 33%;
    
    border-radius: 10px;
    padding: 10px;
    margin: 10px;

    text-align: center;
    cursor: pointer;
    box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);;

}

div.card:hover{
    transform: translateY(-1px);
    background-color:#eee;
}

div.card.team{
    min-width: 300px;
}

div.card>{
    overflow-wrap: break-word;
}

h1.card-title {
    font-size: 1.8rem;
    margin: 10px;
    text-wrap: wrap;
}

h2.card-subtitle {
    font-size: 1.4rem;
    margin: 10px;
}

h3.card-small {
    font-size: 1.2rem;
    margin: 10px;
}



/*
BUTTONS
*/
button:hover{
    transform: translateY(-1px);
}


/* 
ICONS
 */


i.bi {
    border: solid 1px black;
}



i.button {
    cursor: pointer;
    box-shadow: 2px 2px 3px rgba(0, 0, 0, .1),
              2px 2px 5px rgba(0, 0, 0, .1);

}

i.button:hover{
    transform: translateY(-1px);
    background-color:#eee;

}

i.inventory-icon {
    font-size: .8em;
    border-radius: 3px;
    padding: 3px 3px 1px 3px;
}

i.bi-pencil-square {
    font-size: 1.2rem;
    border-radius: 5px;
    padding: 3px 3px 0px 3px;
}

.item-check>i.bi {
    border: none;
}

div.item-table .bi-check-circle {
    color: green;
}

div.item-table .bi-x-circle {
    color: red;
}
span.name-change-buttons {
    display: flex;
    flex-direction: column;
}

span.name-change-buttons i {
    font-size: 1.1rem;
    padding: 3px 3px 0px 3px;
    margin: 2px;
    border: solid 1px black;
    border-radius: 5px;
}



/*
REFERENCE LIST
*/
div.reference {
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
}

div.reference>h1{
    margin-bottom: 5px;
}

div.reference>div {
    display: flex;
    text-align: left;
    gap: 20px;
    padding: 0px;
    border-bottom: dotted 1px black;
}

div.reference>div:last-child{
    border-bottom: 0px;
}

div.reference h2.reference-label {
    min-width: 120px;
    max-width: 120px;
    margin: 5px;
    margin-left: 15px;
    text-align: center;
    text-wrap: wrap;
    font-size: 1.3rem;
}

div.reference p.reference-desc {
    min-width: 100px;
    margin: 5px;
    text-align: left;
}



/*
INVENTORY
*/

div.inventory {
    display: flex;
    flex-direction: column;
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
    align-items: center;
}

div.item-container {
    width: 95%;
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
    display: flex;
    justify-content: center;
    align-items: center;
    overflow-wrap: anywhere;
    border-style: dotted;
    border-width: 0px 1px 0px 0px;
}

div.item-list>div:last-child {
    border-style: none;
}

div.item-list>div.item-special-rules{
    display: block;
    text-align: start;
    padding-left: 3px;
}

div>div.item-action {
    padding: 0px 3px 0px 3px;
    min-width: 105px;
    flex-grow: 1;
    flex-basis: 3%;
    display: flex;
    justify-content: center;
    align-items: center;
}


</style>