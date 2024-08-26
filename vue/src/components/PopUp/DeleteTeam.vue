<template>
    <div class="popup-form">
        <h1 class="section-title popup">Delete Team</h1>

        <div>
            <p>Type 'Delete' below to confirm you want to delete your team.</p>
            <p class="rules-text">This action cannot be undone and will delete all your units and inventory on this team.</p>
        </div>

        <input id="deleteUnit" type="text" v-model="deleteType" class="delete-input" placeholder="Delete"/>

        <span>
            <button @click="submit()">Submit</button>
            <button @click="clearForm()">Cancel</button>
        </span>
    </div>

</template>

<script>
import TeamsService from '../../services/TeamsService.js';

export default {
    data() {
        return {
            deleteType: ''
        }
    },
    methods: {
        clearForm() {
            this.deleteType = '';
            this.$store.commit('REMOVE_SHOW_POPUP');
        },
        submit() {
            if(this.deleteType.toUpperCase() == 'DELETE'){
                TeamsService.deleteTeam(this.$store.state.currentTeam.id)
                .then( response => {
                this.$store.dispatch('loadTeams');
                this.clearForm();
                this.$store.commit('CLEAR_CURRENT_TEAM');
                }).catch(err => this.$store.dispatch('showError', err));
                
            } else {
                this.$store.dispatch('showError', "You must type 'Delete' to delete your team.");
                this.clearForm();
            }
        }
    }
}

</script>

<style scoped>
div.popup-form {
    justify-content: center;
    align-items: center;
}

input.delete-input {
    margin: var(--standard-padding);
    width: 300px;
}

input.delete-input::placeholder{
    font-size: .8em;
    font-style: italic;
}

p {
    margin: 0px;
    padding: var(--standard-padding);
    width: 50vw;
    text-align: center;
}

p.rules-text {
    font-style: italic;
    font-size: .9em;
}
</style>