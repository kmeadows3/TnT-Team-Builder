<template>
    <div class="item-container">
            <h3>Weapons</h3>
            <div class="weapon-list table-label">
                <div class="weapon-med">Type</div>
                <div class="weapon-small">Cost</div>
                <div class="weapon-small">Range </div>
                <div class="weapon-small">Strength</div>
                <div class="weapon-small">Reliability</div>
                <div class="weapon-small">Hands</div>
                <div class="weapon-large">Special Rules</div>
            </div>
            <div class="weapon-list" v-for="weapon in weapons" :key="'weapon' + weapon.id">
                <div class="weapon-med">{{ weapon.name }}</div>
                <div class="weapon-small">{{ weapon.cost }}</div>
                <div class="weapon-small">{{ weapon.meleeRange }}"/ {{ weapon.rangedRange }}"</div>
                <div class="weapon-small">{{ weapon.strength }}</div>
                <div class="weapon-small">{{ weapon.reliability }}</div>
                <div class="weapon-small">{{ weapon.handsRequired }}</div>
                <div class="weapon-large">
                    <span v-show="weapon.itemTraits.length == 0 || weapon.specialRules != 'N/A'">
                        {{weapon.specialRules}}<span v-show="weapon.itemTraits.length > 0">, </span>
                    </span>
                    <span v-show="weapon.itemTraits.length > 0">
                        <span v-for="(trait, index) in weapon.itemTraits" :key="'trait' + weapon.id + trait.id">
                            <span v-show="index != 0">,</span>
                        {{ trait.name }}</span>
                    </span>
                </div>
            </div>
        </div>
</template>

<script>
export default {
    props:['weapons']
}
</script>

<style scoped>

div.weapon-list {
    display: flex;
    min-width: 100%;
}

div.weapon-list>div {
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
    overflow-wrap: break-word;
}

div.weapon-list>.weapon-small {
    min-width: 50px;
    flex-grow: 1;
    flex-basis: 5%;
}

div.weapon-list>.weapon-med {
    min-width: 75px;
    flex-grow: 2;
    flex-basis: 15%;
}

div.weapon-list>.weapon-large {
    min-width: 150px;
    flex-grow: 5;
    flex-basis: 33%;
}


</style>