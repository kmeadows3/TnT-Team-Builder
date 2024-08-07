<template>
    <section class="main-display" :class="$store.state.viewNavigation?'view-nav':''">
        <TeamList v-if="$store.state.showTeamList" />
        <TeamDetail v-if="$store.state.showTeamDetail" />
        <UnitDetail v-if="$store.state.showUnitDetail" />
    </section>



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
    width: 80%;
    flex-grow: 1;

}

section.subsection {
    margin: 3px;
}


input#changeName {
    margin: 3px;
    padding: 2px 20px;
    font-size: .9em;
    font-style: italic;
    font-weight: 100;
    text-align: center;
    align-self: center;
}

@media only screen and (max-width: 992px) {

    body {
        font-size: .9em;
    }
}

@media only screen and (max-width: 600px) {
    section.main-display.view-nav{
        display: none;
    }
}

/*
TITLES
*/

h1.page-title {
    text-align: center;
    font-size: 3em;
    margin: auto;
    width: 80%;
    border: solid 3px black;
    border-radius: 7px;
}

h1.section-title {
    text-align: center;
    font-size: 2em;
    margin: auto;
    width: 98%;
    margin-top: 10px;

}

h2.subsection-title {
    margin: 0px;
    padding: 10px;
}

@media only screen and (max-width: 992px) {

    h1.page-title {
        font-size: 3em;
    }

    h1.section-title {
        font-size: 1.8em;
    }

    h2.subsection-title {
        font-size: 1.4em;
    }
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
    width: 30%;

    border-radius: 10px;
    padding: 10px;
    margin: 10px;

    text-align: center;
    cursor: pointer;
    box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);
    ;

}

div.card:hover {
    transform: translateY(-1px);
    background-color: #eee;
}

div.card.team {
    min-width: 300px;
}

div.card> {
    overflow-wrap: break-word;
}

h1.card-title {
    font-size: 1.8em;
    margin: 10px;
    word-wrap: break-word;
}

h2.card-subtitle {
    font-size: 1.4em;
    margin: 10px;
}

h3.card-small {
    font-size: 1.2em;
    margin: 10px;
}



/*
BUTTONS
*/
button:hover {
    transform: translateY(-1px);
}

.button-container {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
    margin-top: 6px;
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

i.button:hover {
    transform: translateY(-1px);
    background-color: #eee;

}

i.inventory-icon {
    font-size: .8em;
    border-radius: 3px;
    padding: 3px 3px 1px 3px;
}

i.bi-pencil-square {
    font-size: .4em;
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
    font-size: 0.4em;
    padding: 3px 3px 0px 3px;
    margin: 2px;
    border: solid 1px black;
    border-radius: 5px;
}

@media only screen and (max-width: 768px) {
    i.inventory-icon {
        font-size: .9rem;
    }
}


/*
REFERENCE LIST
*/
div.reference {
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
    margin: 3px;
}

div.reference>h1 {
    margin-bottom: 5px;
}

div.reference>div {
    display: flex;
    text-align: left;
    padding: 0px;
    border-bottom: dotted 1px black;
}

div.reference>div:last-child {
    border-bottom: 0px;
}

div.reference h2.reference-label {
    width: 20%;
    margin: 5px;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
    word-wrap: normal;
    font-size: 1.2em;
}

div.reference p.reference-desc {
    width: 80%;
    margin: 5px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

@media only screen and (max-width: 768px) {

    div.reference h2.reference-label {
        font-size: 1.1em;
        margin: 3px;
    }

    div.reference p.reference-desc {
        margin: 2px;
    }
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
    margin: 3px;
    padding-bottom: 5px
}

div.item-container {
    width: 95%;
    padding: 0px 20px;
}

div.item-container>h3 {
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
    border-width: 0px 0px 2px 0px;
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

div.item-list>div.item-name {
    border-style: solid;
    font-weight: bold;
}

div.item-list>div:last-child {
    border-style: none;
}

div.item-list>div.item-special-rules {
    display: block;
    text-align: start;
    padding-left: 3px;
    border-top: dotted 1px black;
    border-right: none;
    font-size: .9em;

    & em {
        text-decoration: underline;
        font-style: normal;
    }

    & span {
        font-style: italic;
    }
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

@media only screen and (max-width: 768px) {
    div.item-container {
        width: 100%;
        padding: 0px 10px;
    }

    div.item-container div.item-table {
        font-size: 1em;
    }
}

/**
FORMS
*/

form {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
}

div.popup-form {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    flex-wrap: wrap;
    justify-content: center;
    gap: 25px;
}

div.popup-form form {
    flex-direction: column;
    width: 100%;
    flex-grow: 5;
    padding: 10px;
    gap: 20px;
}

div.popup-form>span {
    display: flex;
    width: 80%;
    gap: 10px;
    justify-content: center;
    align-items: center;
}

div.popup-form label {
    font-size: 1.1em;
    min-width: 100px;
    max-width: 100px;
    text-align: right;
    text-wrap: wrap;
}

div.popup-form select {
    font-size: 1.0em;
    flex-grow: 5;
}

div.popup-form input {
    font-size: 1.0em;
    flex-grow: 5;
}
</style>