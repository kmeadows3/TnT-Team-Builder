<template>
    <div id="exp-grid">
        <div id="spent-exp"> <strong>SPENT EXP: </strong> {{ $store.state.currentUnit.spentExperience }}</div>
        <div id="unspent-exp"><strong>UNSPENT EXP:</strong> {{ $store.state.currentUnit.unspentExperience }}</div>
        <div id="advance-cost"><strong>COST TO ADVANCE:</strong> {{ $store.state.currentUnit.costToAdvance }}</div>
        <div id="unit-upkeep"><strong>UPKEEP:</strong> {{ $store.state.currentUnit.unitUpkeep }} BS</div>
    </div>
</template>


<style scoped>

div#exp-grid {
    display: flex;
    justify-content: center;
    gap: 20px;
    text-align: center;
    background-color: var(--standard-medium);
    padding: var(--standard-padding);
    border-top: solid var(--thin-border) var(--border-color);
}

@media only screen and (max-width: 600px) {
    div#exp-grid >div{
        display: flex;
        align-items: center;
    }
}

</style>