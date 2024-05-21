<template>
    <div class="item-container">
            <h3>Equipment</h3>
            <div class="table-label equipment-list">
                <div class="equipment-med">Type</div>
                <div class="equipment-small">Cost</div>
                <div class="equipment-large">Special Rules</div>
            </div>
            <div class="equipment-list" v-for="equipment in equipments" :key="'equipment' + equipment.id">
                <div class="equipment-med">{{ equipment.name }}</div>
                <div class="equipment-small">{{ equipment.cost }}</div>
                <div class="equipment-large">
                    <span v-show="equipment.itemTraits.length == 0 || equipment.specialRules != 'N/A'">
                        {{equipment.specialRules}}<span v-show="equipment.itemTraits.length > 0">, </span>
                    </span>
                    <span v-show="equipment.itemTraits.length > 0">
                        <span v-for="(trait, index) in equipment.itemTraits" :key="'trait' + equipment.id + trait.id">
                            <span v-show="index != 0">,</span>
                        {{ trait.name }}</span>
                    </span>
                </div>
            </div>
        </div>
</template>

<script>
export default {
    props:['equipments']
}
</script>

<style scoped>
div.equipment-list {
    display: flex;
    min-width: 100%;
}

div.equipment-list>div {
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
    overflow-wrap: break-word;
}
div.equipment-list>.equipment-small {
    min-width: 50px;
    flex-grow: 1;
    flex-basis: 5%;
}

div.equipment-list>.equipment-med {
    min-width: 75px;
    flex-grow: 1;
    flex-basis: 15%;
}

div.equipment-list>.equipment-large {
    min-width: 150px;
    flex-grow: 5;
    flex-basis: 33%;
}
</style>