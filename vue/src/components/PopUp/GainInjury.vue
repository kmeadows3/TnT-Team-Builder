<template>
    <div>
        <h1 class="section-title">Select Injury</h1>
        <div class="gain-injury-container">
            <p>Injury should be rolled on the injury table then selected here.</p>
            <form>
                <div class="injury-picker">
                    <select class="injury-name" v-model="newInjury">
                        <option selected disabled :value="{}">Choose Injury</option>
                        <option v-for="potentialInjury in potentialInjuries" :key="'injury-' + potentialInjury.id"
                            :value="potentialInjury">
                            {{ potentialInjury.name }} </option>
                    </select>
                    <p>{{ newInjury.description ? newInjury.description : "---" }}</p>

                </div>
            </form>

            <span class="button-container">
                <button @click="addInjury()">Gain Injury</button>
                <button @click="cancel()">Cancel</button>
            </span>
        </div>



    </div>
</template>

<script>
import UnitService from '../../services/UnitService';

export default {
    data() {
        return {
            potentialInjuries: [],
            newInjury: {}
        }
    },
    methods: {
        getPotentialInjuries() {
            UnitService.getPotentialInjuries(this.$store.state.currentUnit.id)
                .then(response => {
                    this.potentialInjuries = response.data.sort((a, b) => a.name.localeCompare(b.name));
                }).catch(error => this.$store.dispatch('showError', error));
        },
        addInjury() {
            UnitService.addInjury(this.newInjury.id, this.$store.state.currentUnit.id)
                .then(response => {
                    this.$store.dispatch('reloadCurrentUnit');
                    this.cancel();
                })
                .catch(error => this.$store.dispatch('showError', error));
        },
        cancel() {
            this.potentialInjuries = [];
            this.newInjury = {};
            this.$store.commit('REMOVE_SHOW_POPUP');
        }
    },
    created() {
        this.getPotentialInjuries();
    }
}
</script>


<style scoped>
div.gain-injury-container {
    display: flex;
    flex-direction: column;
    width: 60vw;
    align-items: center;
    justify-content: center;
}

div.gain-injury-container>form {
    width: 100%;
    padding: 3px;
}

div.gain-injury-container p {
    padding-left: 6px;
    text-wrap: wrap;
    flex-grow: 1;
    margin: 0px;
}

div.injury-picker {
    display: flex;
    justify-content: start;
    align-items: center;
    padding-top: 6px;
}

.injury-name {
    width: 150px;
}

select.injury-name {
    font-size: 1em;
    text-align: center;
}
</style>