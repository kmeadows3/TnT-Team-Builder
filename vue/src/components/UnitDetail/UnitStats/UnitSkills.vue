<template>
    <div class="keyword-row" v-show="$store.state.unitSkillsSorted.length > 0" :class="{'bottom': !$store.state.unitInjuriesSorted.length}">
        <div class="label" >
            ABILITIES
        </div>
        <div class="text">
            <span v-for="(skill,index) in $store.state.unitSkillsSorted" :key="'skill-name-' + skill.id">
                <span v-show="index>0">, </span>
                {{skill.name}}{{ skill.count > 1 ? '(x'+ skill.count+')' : '' }}
            </span>
        </div>
    </div>
    <div class="keyword-row bottom" v-show="$store.state.unitInjuriesSorted.length > 0">
        <div class="label">
            INJURIES
        </div>
        <div class="text">
            <span v-for="(injury, index) in $store.state.unitInjuriesSorted" :key="'injury-name-' + injury.id">
                <span v-show="index>0">, </span>
                {{injury.name}}{{ injury.count > 1 ? '(x'+ injury.count+')' : '' }}<i class="bi bi-x-lg button inventory-icon"  v-show="injury.removable" @click="removeInjury(injury)" title="Remove"></i>
            </span>
        </div>
    </div>
</template>

<script>
import UnitService from '../../../services/UnitService';

export default {
    data(){
        return{
            injuries: []
        }
    },
    methods: {
        removeInjury(item){
            UnitService.removeInjury(item.id, this.$store.state.currentUnit.id)
                .then(response => {
                    this.$store.dispatch('reloadCurrentUnit');
                }).catch(error => {
                    this.$store.dispatch('showError', error);
                });
        },
    },

}
</script>

<style scoped>

div.keyword-row{
    border-top: solid var(--thin-border) var(--border-color);
    display: grid;
    grid-template-columns: 1fr 5fr;
    grid-template-areas: 'label text';

    >div {
        padding: var(--wide-padding) var(--standard-padding);

        &.label{
            text-align: center;
            grid-area: label;
            font-weight: bold;
            background-color: var(--standard-medium);
            border-right: solid var(--thin-border) var(--border-color);
        }

        &.text{
            grid-area: text;
            background-color: var(--list-background);
        }
    }

    &.bottom{
        div.label{
            border-radius: 0 0 0 var(--border-radius-card-title);
        }
    }
}

@media only screen and (max-width: 600px) {
    div.keyword-row {
        display: block;

        border-top: solid var(--thick-border) var(--border-color);

        > div{
            &.label{
                border-right: none;
                border-radius: none;
            }
  
        } 

        &.bottom{
            div.label{
                border-right: none;
                border-radius: 0px;
            }
        }
    }
}

i.bi-x-lg {
    margin-left:var(--standard-padding);
    padding: 1px 1px 0px 0px;
    border-radius: 3px;
}

</style>