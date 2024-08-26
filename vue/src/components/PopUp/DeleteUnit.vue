<template>
    <div class="popup-form">
        <h1 class="section-title popup">Delete Unit</h1>

        <div>
            <p>Type 'Dead' below if the unit is dead, type 'Dismissed' if they are dismissed.</p>
            <p class="rules-text">Dead units die with all inventory, while dismissed units who are not freelancers
                return their inventory to the team before leaving.</p>
        </div>

        <input id="deleteUnit" type="text" v-model="deleteType" class="delete-input" placeholder="Type Dead or Dismissed"/>

        <span>
            <button @click="submit()">Submit</button>
            <button @click="clearForm()">Cancel</button>
        </span>
    </div>

</template>

<script>
import UnitService from '../../services/UnitService';

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
            if(this.deleteType.toUpperCase() == 'DEAD'){
                UnitService.killUnit(this.$store.state.currentUnit)
                    .then(response => {
                        this.$store.commit('CLEAR_CURRENT_UNIT');
                        this.clearForm();
                    })
                    .catch(err => {
                        this.$store.dispatch('showError', err);
                    });
            } else if (this.deleteType.toUpperCase() == 'DISMISSED'){
                UnitService.dismissUnit(this.$store.state.currentUnit)
                    .then(response => {
                        this.$store.commit('CLEAR_CURRENT_UNIT');
                        this.clearForm();
                    })
                    .catch(err => {
                        this.$store.dispatch('showError', err);
                    });
            } else {
                this.$store.dispatch('showError', "You must type either Dead or Dismissed to remove a unit.");
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
    
    &.rules-text{
        margin: auto;
        font-style: italic;
        width: 40vw;
        font-size: .9em;
        padding-top: 0px;
    }
}

</style>