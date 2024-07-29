<template>
    <div class="stat-container">
        <div class="stat-box">
            <p>SPENT EXPERIENCE</p>
            <p>{{ $store.state.currentUnit.spentExperience }}</p>
        </div>
        <div class="stat-box">
            <p>UNSPENT EXPERIENCE</p>
            <p>{{ $store.state.currentUnit.unspentExperience }}</p>
        </div>
        <div class="stat-box">
            <p>COST TO ADVANCE</p>
            <p>{{ $store.state.currentUnit.costToAdvance }}</p>
        </div>
        <div class="stat-box">
            <p>UPKEEP</p>
            <p>{{ $store.state.currentUnit.unitUpkeep }}</p>
        </div>
    </div>
</template>

<style scoped>

div.stat-container{
    display: flex;
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
    margin: 3px;
}

div.stat-box {
    flex-grow: 1;
    flex-basis: 25%;
    border-radius: 7px;
    margin: 7px;
}

div.stat-container p{
    margin: 3px;
}

@media only screen and (max-width: 768px) {
    div.stat-container {
        font-size: .9em;
    }

    div.stat-container>div.stat-box{
        margin: 3px;
    }

    div.stat-box>p{
        word-wrap: break-word;
        overflow-wrap: anywhere;
    }
}
</style>