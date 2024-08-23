<template>
    <section class="main-display" :class="$store.state.viewNavigation ? 'view-nav' : ''">
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

.nameless {
    font-style: italic;
}

section.main-display {
    position: relative;
    min-width: 200px;
    width: 80%;
    flex-grow: 1;
    background-color: var(--standard-mid-dark);
    border: solid var(--thick-border) var(--border-color);
}

section.subsection {
    background-color: var(--section-background);
}

input#changeName {
    margin: var(--standard-padding);
    padding: 2px 20px;
    font-size: .9em;
    font-style: italic;
    font-weight: 100;
    text-align: center;
    align-self: center;
}

div.no-values{
    padding: calc(var(--wide-padding) * 2 ) var(--standard-padding);
    text-align: center;
}

@media only screen and (max-width: 992px) {
    body {
        font-size: .9em;
    }
}

@media only screen and (max-width: 600px) {
    section.main-display.view-nav {
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
    width: 100%;
    background-color: var(--standard-mid-dark);

    &.name-display{
        display: block;
        word-wrap: break-word;
        position: relative;
    }

    &.name-entry{
        display: flex;
        justify-content: center;
    }
    
}

h1.section-title {
    text-align: center;
    font-size: 2em;
    margin: 0px;
    width: 100%;
    background-color: var(--standard-mid-dark);
    border-top: solid var(--thick-border) var(--border-color);
}

h2.subsection-title {
    margin: 0px;
    padding: 10px;
    text-align: center;
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
    align-items: center;
    border-top: solid var(--thick-border) var(--border-color);
}

div.card {
    flex-basis: 21%;
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;


    min-width: 200px;
    max-width: 33%;
    width: 30%;

    border: solid var(--thick-border) var(--border-color);
    border-radius: var(--border-radius-card);
    margin: var(--wide-padding);
    background-color: var(--standard-light);

    text-align: center;
    cursor: pointer;
    box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);

    &:hover {
        transform: translateY(-1px);
        background-color: var(--alternative-light);
    }

    &.team {
        min-width: 300px;
    }
}

div.card * {
    overflow-wrap: break-word;
    margin: 0px;
    padding: var(--card-padding) 0px 0px 0px;

    &.card-footer {
        width: 100%;
        border-radius: 0px 0px var(--border-radius-card-title) var(--border-radius-card-title);
        background-color: var(--standard-mid-dark);
        padding-bottom: var(--card-padding);
        border-top: solid var(--thick-border) var(--border-color);
    }
}

div.card div.card-title {
    padding: 0px;
}


div.card:hover {
    h1.card-title {
        background-color: var(--alternative-mid-dark);
    }

    h2.card-subtitle {
        background-color: var(--alternative-medium);
    }

    div.card-footer {
        background-color: var(--alternative-mid-dark);
    }
}

h1.card-title {
    font-size: 1.8em;
    padding: var(--wide-padding) 0px;
    background-color: var(--standard-mid-dark);
    border-radius: var(--border-radius-card-title) var(--border-radius-card-title) 0px 0px;
    border-bottom: solid var(--thick-border) var(--border-color);
}

h2.card-subtitle {
    font-size: 1.4em;
    background-color: var(--standard-medium);
    padding: var(--standard-padding);
}

h3.card-small {
    font-size: 1.2em;
    padding: var(--wide-padding);
}



/*
BUTTONS
*/

button:hover {
    transform: translateY(-1px);
}

button {
    box-shadow: 2px 2px 3px rgba(0, 0, 0, .2),
        2px 2px 5px rgba(0, 0, 0, .2);
    border-radius: var(--border-radius-button);
    padding: var(--standard-padding) var(--wide-padding);
    cursor: pointer;
    justify-self: center;
    
    background-color: var(--standard-light);
        &:hover {
            background-color: var(--alternative-very-light);
        }
    
}

div.solo-button {
    display: flex;
    justify-content: center;
    padding: var(--wide-padding);

    button {
        font-size: 1.1em;
    }
}

.button-container {
    width: 100%;
    display: flex;
    justify-content: center;
    gap: 10px;
    padding: var(--standard-padding);
    border-top: solid var(--thick-border) var(--border-color);
    background-color: var(--standard-medium);

    &.delete {
        bottom:0px;
        width: 100%;
        background-color: var(--standard-mid-dark);
        justify-content: flex-end;

        button{
            background-color: var(--highlight-mid-dark);

            &:hover{
                background-color: var(--highlight-medium);
            }
        }
    }
}


/* 
ICONS
 */


i.bi {
    border: solid var(--thin-border) var(--border-color);
}

i.button {
    cursor: pointer;
    box-shadow: 2px 2px 3px rgba(0, 0, 0, .1),
        2px 2px 5px rgba(0, 0, 0, .1);
    background-color: var(--standard-light);

    &:hover {
            transform: translateY(-1px);
            background-color: var(--alternative-very-light);
    }

    &.bi-pencil-square {
        position: absolute;
        top: 50%;
        transform: translate(0%, -50%);
        margin-left: 20px;

        font-size: .4em;
        border-radius: 5px;
        padding: 3px 3px 0px 3px;
        border-width: var(--thick-border);

        &:hover {
            transform: translate(0%, calc(-50% - 1px));
        }
    }

    
}

i.inventory-icon {
    font-size: .8em;
    border-radius: 3px;
    padding: 3px 3px 1px 3px;

    &.button {
        background-color: var(--highlight-mid-dark);

        &:hover {  
            transform: translateY(-1px);
            background-color: var(--highlight-medium);
        }
    }
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
    border: solid var(--thin-border) var(--border-color);
    border-radius: 5px;
    background-color: var(--standard-light);

    &:hover{
        background-color: var(--alternative-very-light);
    }
}

@media only screen and (max-width: 768px) {
    i.inventory-icon {
        font-size: .9rem;
    }
}

/*
INVENTORY
*/
div.inventory {
    display: flex;
    flex-direction: column;
    text-align: center;
    align-items: center;
}

div.inventory-actions {
    border-bottom: solid var(--thick-border) var(--border-color);
}

div.item-container {
    width: 100%;
}

div.item-table {
    border: solid var(--thick-border) var(--border-color);
    border-radius: var(--border-radius-card);
    margin: var(--wide-padding);

    >div{
        border-bottom: solid var(--thin-border) var(--border-color);
        
        &:first-child{
            background-color: var(--standard-mid-dark);
            border-radius: var(--border-radius-card-title) var(--border-radius-card-title) 0 0;
        }

        &.title{
            font-weight: bold;
            font-size: 1.3em;
            padding: var(--standard-padding);
            background-color: var(--standard-mid-dark);
            border-radius: var(--border-radius-card-title) var(--border-radius-card-title) 0 0;

        }

    }
    
}

div.item-table >div.item-list {
    display: flex;
    min-width: 100%;
    background-color: var(--section-background);

    &:nth-child(even) {
        background-color: var(--standard-very-light);
    }
    &.table-label {
        background-color: var(--standard-medium);
    }
    &:last-child {
        border-bottom: none;
        border-radius: 0px 0px var(--border-radius-card-title) var(--border-radius-card-title);
    }
}

div.item-list >div {
    display: flex;
    justify-content: center;
    align-items: center;
    overflow-wrap: anywhere;
    border-right: dotted var(--thin-border) var(--border-color);

    &.item-name {
        font-weight: bold;
    }

    &.item-special-rules {
        display: block;
        text-align: start;
        font-size: .9em;

        padding: 0px var(--standard-padding);
        border-top: dotted var(--thin-border) var(--border-color);
        border-right: none;

        & em {
            text-decoration: underline;
            font-style: normal;
        }

        & span {
            align-items: center;
            font-style: italic;
            vertical-align: middle;
        }
    }

    &.item-action {
        padding: 0px var(--standard-padding);
        min-width: 105px;
        flex-grow: 1;
        flex-basis: 3%;
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
    }

    &:last-child {
        border-style: none;
    }

}


@media only screen and (max-width: 768px) {
    div.item-container {
        width: 100%;
        padding: 0px 10px;
    }

    div.item-container div.item-table {
        font-size: 1em;

        div.action-mode>div.item-name{
            font-size: .9em;
        }
    }
}

@media only screen and (max-width: 475px) {
    div.item-container div.item-table >div.table-label {
            font-size: .7em;
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