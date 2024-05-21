<template>
    <div class="item-container">
            <h3>Armor</h3>
            <div class="table-label armor-list">
                <div class="armor-med">Type</div>
                <div class="armor-small">Cost</div>
                <div class="armor-med">Defense Bonus (melee/ranged)</div>
                <div class="armor-large">Special Rules</div>
            </div>
            <div class="armor-list" v-for="armor in armors" :key="'armor' + armor.id">
                <div class="armor-med">{{ armor.name }}</div>
                <div class="armor-small">{{ $store.state.currentUnit.wounds == 1 ?  armor.cost : ($store.state.currentUnit.wounds == 2 ? armor.cost2Wounds : armor.cost3Wounds) }}</div>
                <!--TODO: Logic around armor bonuses once items can be equipped-->
                <div class="armor-med">{{ armor.meleeDefenseBonus }} / {{ armor.rangedDefenseBonus }}</div>
                <div class="armor-large">
                    <span v-show="armor.itemTraits.length == 0 || armor.specialRules != 'N/A'">
                        {{armor.specialRules}}<span v-show="armor.itemTraits.length > 0">, </span>
                    </span>
                    <span v-show="armor.itemTraits.length > 0">
                        <span v-for="(trait, index) in armor.itemTraits" :key="'trait' + armor.id + trait.id">
                            <span v-show="index != 0">,</span>
                        {{ trait.name }}</span>
                    </span>
                </div>
            </div>
        </div>
</template>

<script>
export default {
    props:['armors']
}
</script>

<style scoped>
div.armor-list {
    display: flex;
    min-width: 100%;
}

div.armor-list>div {
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
    overflow-wrap: break-word;
}
div.armor-list>.armor-small {
    min-width: 50px;
    flex-grow: 1;
    flex-basis: 5%;
}

div.armor-list>.armor-med {
    min-width: 75px;
    flex-grow: 1;
    flex-basis: 15%;
}

div.armor-list>.armor-large {
    min-width: 150px;
    flex-grow: 5;
    flex-basis: 33%;
}
</style>