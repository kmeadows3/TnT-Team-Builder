<template>
    <div class="stat-container">
        <div class="stat-box">
            <p>MOVE</p>
            <p>{{ $store.state.currentUnit.move }}</p>
        </div>
        <div class="stat-box">
            <p>MELEE</p>
            <p>{{ $store.state.currentUnit.melee }}</p>
        </div>
        <div class="stat-box">
            <p>RANGED</p>
            <p>{{ $store.state.currentUnit.ranged }}</p>
        </div>
        <div class="stat-box">
            <p>STRENGTH</p>
            <p>{{ $store.state.currentUnit.strength }}</p>
        </div>
        <div class="stat-box">
            <p>DEFENSE</p>
            <p>{{ $store.state.currentUnit.defense }}</p>
        </div>
    </div>
</template>

<style scoped>

div.stat-container{
    display: flex;
}

div.stat-box {
    flex-grow: 1;
    flex-basis: auto;
    border-radius: 7px;
    margin: 7px;
}
</style>