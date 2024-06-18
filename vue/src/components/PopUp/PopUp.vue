<template>
    <div v-if="$store.state.showPopup" class="popup-background">
        <div class="popup">
            <i class="bi bi-x-square" @click ="closePopUp()"></i>
            <h2> POPUP</h2>
            <NewTeamForm v-if="$store.state.showNewTeamForm"/>
            <NewUnitForm v-if="$store.state.showNewUnitForm"/>
        </div>


    </div>
</template>


<script>
import NewTeamForm from '../TeamList/NewTeamForm.vue';
import NewUnitForm from '../TeamDetail/NewUnitForm.vue';

export default {
    methods: {
        closePopUp() {
            this.$store.commit('TOGGLE_SHOW_POPUP');
        }
    },
    components: {
        NewTeamForm,
        NewUnitForm
    }
}
</script>

<style scoped>

div.popup-background{
    position:fixed;
    height: 100vh;
    width: 100vw;
    margin: 0px;
    display: flex;
    top: 0px;
    left: 0px;
    z-index: 5;
    background-color: rgba(0, 0, 0, .2);
    justify-content: center;
    align-items: center;
}

div.popup {
    position: absolute;
    background-color: white;
    border: solid 3px black;
    width: 33%;
    height: 50%;
}

i.bi-x-square {
    position: absolute;
    top: 10px;
    right: 10px;
    border: none;
}



</style>