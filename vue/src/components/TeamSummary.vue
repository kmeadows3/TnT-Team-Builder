<template>
    <div class="team-summary">
        <div class="bigger-box">
            <p>FACTION</p>
            <p>{{ this.$store.state.currentTeam.faction }}</p>
        </div>
        <div class="little-box">
            <p>BS COST</p>
            <p>{{ this.$store.state.currentTeam.bscost }}</p>
        </div>
        <div class="bigger-box">
            <p>UNSPENT BARTER SCRIP</p>
            <p>{{ this.$store.state.currentTeam.money }}</p>
        </div>
        <div class="little-box">
            <p>UPKEEP</p>
            <p>{{ this.$store.state.currentTeam.upkeep }}</p>
        </div>
    </div>
</template>

<style scoped>

div.team-summary {
    display: flex;
    justify-content: space-between;
}

div.team-summary>div {
    text-align: center;
    flex-basis: auto;
}

div.team-summary>div.little-box {
    flex-grow: 1;
    min-width: 100px;
}

div.team-summary>div.bigger-box {
    flex-grow: 2;
    min-width: 200px;
}
</style>