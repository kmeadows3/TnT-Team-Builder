<template>
    <section class="team-summary">
        <div class="team-summary-grid title">
            <div class="faction title">
                Faction
            </div>
            <div class="bs-cost title">
                BS Cost
            </div>
            <div class="money title">
                Unspent Barter Scrip
            </div>
            <div class="upkeep title">
                Upkeep
            </div>
        </div>
        <div class="team-summary-grid">
            <div class="faction">
                {{ this.$store.state.currentTeam.faction }}
            </div>
            <div class="bs-cost">
                {{ this.$store.state.currentTeam.bscost }}
            </div>
            <div class="money">
                {{ this.$store.state.currentTeam.money }}
            </div>
            <div class="upkeep">
                {{ this.$store.state.currentTeam.upkeep }}
            </div>
        </div>
    </section>

</template>

<style scoped>

/* section.team-summary{
    margin-top: var(--wide-padding);
} */

div.team-summary-grid {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;
    grid-template-areas: 'faction bs-cost money upkeep';
    padding: var(--wide-padding) var(--standard-padding);

    &.title{
        border-top: solid var(--thick-border) var(--border-color);
        background-color: var(--standard-light);
    }
}

div.team-summary-grid>div {
    text-align: center;
    font-size: 1.1em;

    &.title {
        font-weight: bold;
    }
}

div.team-summary-grid>div.faction {
    grid-area: faction;
}

div.team-summary-grid>div.bs-cost {
    grid-area: bs-cost;
}

div.team-summary-grid>div.money {
    grid-area: money;
}

div.team-summary-grid>div.upkeep {
    grid-area: upkeep;
}
</style>