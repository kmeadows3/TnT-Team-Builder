<template>
    <div id="basic-information">
        <div id="name">
            <h1 class="page-title name-display" v-show="!showChangeNameForm">
            <span>{{$store.state.currentUnit.name}}</span>
            <i class="bi bi-pencil-square button" @click="toggleEditName()" title="Edit Name"></i>
        </h1>
        <h1 class="page-title name-entry" v-show="showChangeNameForm">
            <form>
                <input type="text" id="changeName" v-model="unitName">
            </form>
            <span class="name-change-buttons">
            <i class="bi bi-check-circle button" @click="changeName()" title="Submit"></i>
            <i class="bi bi-x-square button" @click="resetName()" title="Cancel"></i>
        </span>
        </h1>
        </div>
        <div id="unit-actions">
            <UnitActions />
        </div>
        <div id="title">
            <p class="basics-label">TITLE</p>
            <p class="basics">{{ $store.state.currentUnit.unitClass }}</p>
        </div>
        <div id="rank">
            <p class="basics-label">RANK</p>
            <p class="basics">{{ $store.state.currentUnit.rank }}</p>
        </div>
        <div id="type">
            <p class="basics-label">TYPE</p>
            <p class="basics">{{ $store.state.currentUnit.species }}</p>
        </div>
        <div id="bs-cost">
            <p class="basics-label">BS COST</p>
            <p class="basics">{{ $store.state.currentUnit.bscost }}</p>
        </div>
    </div>
</template>

<script>
import UnitService from '../../services/UnitService.js';
import UnitActions from './UnitActions.vue';

export default {
    data() {
        return {
            showChangeNameForm:false,
            untName: '',
        }
    },
    components: {
        UnitActions
    },
    methods: {
        toggleEditName(){
            this.showChangeNameForm = !this.showChangeNameForm;
        },
        resetName(){
            this.unitName = this.$store.state.currentUnit.name;
            this.toggleEditName();
        },
        changeName(){
            this.$store.commit('CHANGE_UNIT_NAME', this.unitName);
            UnitService.updateUnit(this.$store.state.currentUnit)
                .then( response => {
                    this.$store.commit('SET_CURRENT_UNIT', response.data);
                }).catch(error => this.$store.dispatch('showHttpError', error));
            this.toggleEditName();
        }
    },
    beforeMount() {
        this.unitName = this.$store.state.currentUnit.name;
    }
}
</script>


<style scoped>
div#basic-information{
    display: grid;
    width: 100%;
    grid-template-areas:
        "name    name    name    name" 
        "actions actions actions actions"
        "title   rank    type    bs-cost";
    grid-template-columns: 1fr 1fr 1fr 1fr;
}

div#basic-information>div{
    margin: 3px;
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
}
div#basic-information>div#name{
    grid-area: name;
    border-style: none;
}

div#basic-information>div#unit-actions{
    grid-area: actions;
    border: none;
}

div#title{
    grid-area: title;
}
div#rank{
    grid-area: rank;
}
div#type{
    grid-area: type;
}
div#bs-cost{
    grid-area: bs-cost;
}

h1.page-title {
    display: flex;
    justify-content: center;
    align-items: center;
}

h1.page-title span{
    padding-right: 20px;
}

p.basics-label {
    font-weight: bold;
    margin: 0px;
    padding: 10px 0px 5px 0px;
    border-bottom: solid 1px black;
}

p.basics {
    margin: 10px 0px;
}


</style>