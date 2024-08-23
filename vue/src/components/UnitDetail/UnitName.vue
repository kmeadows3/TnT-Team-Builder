<template>
    <div id="basic-information">
        <div id="name">
            <h1 class="page-title name-display" v-show="!showChangeNameForm">
                <span class="name-display"
                    :class="{ nameless: !$store.state.currentUnit.name }">{{ $store.state.currentUnit.name ?
                        $store.state.currentUnit.name : 'Nameless'}}</span>
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
        <div id="new-purchase" v-show="$store.state.currentUnit.newPurchase && $store.state.currentUnit.specialRules">
            {{ $store.state.currentUnit.specialRules }}
        </div>
    </div>
</template>

<script>
import UnitService from '../../services/UnitService.js';
import UnitActions from './UnitActions.vue';

export default {
    data() {
        return {
            showChangeNameForm: false,
            untName: '',
        }
    },
    components: {
        UnitActions
    },
    methods: {
        toggleEditName() {
            this.showChangeNameForm = !this.showChangeNameForm;
        },
        resetName() {
            this.unitName = this.$store.state.currentUnit.name;
            this.toggleEditName();
        },
        changeName() {
            this.$store.commit('CHANGE_UNIT_NAME', this.unitName);
            UnitService.updateUnit(this.$store.state.currentUnit)
                .then(response => {
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
div#basic-information {
    display: flex;
    flex-direction: column;
}

div#new-purchase {
    border-top: solid var(--thick-border) var(--border-color);
    background-color: var(--alert-background-pale);
    text-align: center;
    padding: var(--wide-padding) var(--standard-padding);
    font-weight: bold;
}

h1.page-title {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
}


</style>