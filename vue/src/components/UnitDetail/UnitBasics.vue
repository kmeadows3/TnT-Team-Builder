<template>
    <div id="basic-information">
        <div id="name">
            <h1 class="page-title" v-show="!showChangeNameForm">
            <span>{{$store.state.currentUnit.name}}</span>
            <i class="bi bi-pencil-square" @click="toggleEditName()"></i>
        </h1>
        <h1 class="page-title" v-show="showChangeNameForm">
            <form>
                <input type="text" id="changeName" v-model="unitName">
            </form>
            <span class="action-buttons">
            <i class="bi bi-check-circle" @click="changeName()"></i>
            <i class="bi bi-x-square" @click="resetName()"></i>
        </span>
        </h1>
            <h1></h1>
        </div>
        <div id="title">
            <p>TITLE</p>
            <p>{{ $store.state.currentUnit.unitClass }}</p>
        </div>
        <div id="rank">
            <p>RANK</p>
            <p>{{ $store.state.currentUnit.rank }}</p>
        </div>
        <div id="type">
            <p>TYPE</p>
            <p>{{ $store.state.currentUnit.species }}</p>
        </div>
        <div id="mettle">
            <p>METTLE</p>
            <p>{{ $store.state.currentUnit.mettle }}</p>
        </div>
        <div id="wounds">
            <p>WOUNDS</p>
            <p>{{ $store.state.currentUnit.wounds }}</p>
        </div>
        <div id="bs-cost">
            <p>BS COST</p>
            <p>{{ $store.state.currentUnit.bscost }}</p>
        </div>
    </div>
</template>

<script>
import TeamsService from '../../services/TeamsService.js';

export default {
    data() {
        return {
            showChangeNameForm:false,
            untName: '',
        }
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
            TeamsService.updateUnit(this.$store.state.currentUnit)
                .then( response => {
                    this.$store.commit('SET_CURRENT_UNIT', response.data);
                }).catch(error => console.error(error));
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
        "name   name   name" 
        "title  rank   type"
        "mettle wounds bs-cost";
    grid-template-columns: 1fr 1fr 1fr;
}
div#basic-information>div{
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
}
div#name{
    grid-area: name;
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
div#mettle{
    grid-area: mettle;
}
div#wounds{
    grid-area: wounds;
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



</style>