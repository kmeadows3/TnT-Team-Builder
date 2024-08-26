<template>
    <section id="unit-stats">
        <div class="stat-container" id="basics-grid">
            <div id="title">
                <p class="stat-label">TITLE</p>
                <p class="stat-text">{{ $store.state.currentUnit.unitClass }}</p>
            </div>
            <div id="rank">
                <p class="stat-label">RANK</p>
                <p class="stat-text">{{ $store.state.currentUnit.rank }}</p>
            </div>
            <div id="type">
                <p class="stat-label">TYPE</p>
                <p class="stat-text">{{ $store.state.currentUnit.species }}</p>
            </div>
            <div id="bs-cost">
                <p class="stat-label">BS COST</p>
                <p class="stat-text">{{ $store.state.currentUnit.bscost }}</p>
            </div>
        </div>

        <div class="stat-container" id="big-stats">
            <div id="mettle">
                <p class="stat-label">METTLE</p>
                <p class="stat-text">{{ $store.state.currentUnit.mettle }}</p>
            </div>
            <div id="wounds">
                <p class="stat-label">WOUNDS</p>
                <p class="stat-text">{{ $store.state.currentUnit.wounds }}</p>
            </div>
        </div>

        <div class="stat-container" id="other-stats">
            <div id="move">
                <p class="stat-label">MOVE</p>
                <div>
                    <p class="stat-text">{{ calculatedMove }}{{ calculatedMove == $store.state.currentUnit.move ? "" :
                        "(" + $store.state.currentUnit.move + ")" }}</p>
                </div>
            </div>
            <div id="melee">
                <p class="stat-label">MELEE</p>
                <div v-show="!hasShield">
                    <p class="stat-text">{{ calculatedMelee }}{{ calculatedMelee == $store.state.currentUnit.melee ? ""
                        : " ("
                        +
                        $store.state.currentUnit.melee + ")" }}</p>
                </div>
                <div v-show="hasShield" class="display-split-stat">
                    <div>
                        <p class="stat-text stat-label two-rows">Attacking</p>
                        <p class="stat-text two-rows">{{ $store.state.currentUnit.melee }}</p>
                    </div>
                    <div>
                        <p class="stat-text stat-label two-rows">Defending</p>
                        <p class="stat-text two-rows" two-rows>{{ ($store.state.currentUnit.melee + 1) + " (" +
                            $store.state.currentUnit.melee
                            + ")" }}</p>
                    </div>
                </div>
            </div>
            <div id="ranged">
                <p class="stat-label"><span>RANGE</span><span class="hide-for-acronym">D</span></p>
                <div>
                    <p class="stat-text">{{ $store.state.currentUnit.ranged }}</p>
                </div>
            </div>
            <div id="strength">
                <p class="stat-label"><span>STR</span><span class="hide-for-acronym">ENGTH</span></p>
                <div>
                    <p class="stat-text">{{ calculatedStrength }}{{ calculatedStrength ==
                        $store.state.currentUnit.strength
                        ? ""
                        : " (" + $store.state.currentUnit.strength + ")" }}</p>
                </div>
            </div>


            <div id="defense">
                <p class="stat-label"><span>DEF</span><span class="hide-for-acronym">ENSE</span></p>
                <div class="stat-text">
                    <div v-show="showRegularDefense">
                        <p v-show="!showUnarmoredDefense">{{ $store.state.currentUnit.defense }}</p>
                        <p v-show="showUnarmoredDefense">{{ calculatedRangedDefense + " (" +
                            $store.state.currentUnit.defense + ")" }}</p>
                    </div>

                    <div v-show="!showRegularDefense" class="display-split-stat">
                        <div>
                            <p class="stat-label two-rows">Melee</p>
                            <p class="two-rows">{{ calculatedMeleeDefense + " (" +
                                $store.state.currentUnit.defense +
                                ")" }}</p>
                        </div>
                        <div>
                            <p class="stat-label two-rows">Ranged</p>
                            <p class="two-rows">{{ calculatedRangedDefense + " (" +
                                $store.state.currentUnit.defense +
                                ")" }}</p>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <UnitExperience />
        <UnitSkills />
    </section>
</template>

<script>
import UnitSkills from './UnitSkills.vue';
import UnitExperience from './UnitExperience.vue';

export default {
    components: {
        UnitSkills,
        UnitExperience
    },
    computed: {
        hasStrongPoint() {
            return this.$store.state.currentUnit.skills.filter(skill => skill.name == "Strong Point").length > 0;
        },
        showRegularDefense() {
            return this.$store.state.currentUnit.defense == this.calculatedRangedDefense
                && this.$store.state.currentUnit.defense == this.calculatedMeleeDefense
                || this.calculatedMeleeDefense == this.calculatedRangedDefense;
        },
        showUnarmoredDefense() {
            return this.$store.state.currentUnit.defense != this.calculatedRangedDefense
                || this.$store.state.currentUnit.defense != this.calculatedMeleeDefense;
        },
        hasShield() {
            let inventory = this.$store.state.currentUnit.inventory;
            inventory = inventory.filter((item) => item.equipped && item.shield);
            return inventory.length > 0;
        },
        calculatedMove() {

            let inventory = this.$store.state.currentUnit.inventory;
            inventory = inventory.filter((item) => item.equipped);

            let hasBerserkerBrew = false;

            inventory.forEach(item => {
                if (item.name == "Berserker Brew") {
                    hasBerserkerBrew = true;
                }
            });

            let reducesMovement = false

            inventory = inventory.filter((item) => {
                reducesMovement = false;
                item.itemTraits.forEach((trait) => {
                    if (trait.name == "Reduces Movement") {
                        reducesMovement = true;
                    }
                })
                return reducesMovement;
            });

            let calculatedMovement = this.hasStrongPoint ? this.$store.state.currentUnit.move : this.$store.state.currentUnit.move - inventory.length;

            return hasBerserkerBrew ? calculatedMovement + 1 : calculatedMovement;
        },
        calculatedMelee() {

            let inventory = this.$store.state.currentUnit.inventory;
            inventory = inventory.filter((item) => item.equipped);

            let hasBerserkerBrew = false;

            inventory.forEach(item => {
                if (item.name == "Berserker Brew") {
                    hasBerserkerBrew = true;
                }
            });

            return hasBerserkerBrew ? this.$store.state.currentUnit.melee + 1 : this.$store.state.currentUnit.melee;
        },
        calculatedRangedDefense() {
            let inventory = this.$store.state.currentUnit.inventory;
            inventory = inventory.filter((item) => item.equipped && item.category == "Armor");

            let rangedBonus = 0;

            inventory.forEach((item) => {
                if (item.rangedDefenseBonus > rangedBonus) {
                    rangedBonus = item.rangedDefenseBonus;
                }
            });

            return rangedBonus + this.$store.state.currentUnit.defense;
        },
        calculatedMeleeDefense() {
            let inventory = this.$store.state.currentUnit.inventory;
            inventory = inventory.filter((item) => item.equipped && item.category == "Armor");

            let meleeBonus = 0;
            let hasArmor = false
            let hasShield = false;

            inventory.forEach((item) => {
                if (item.meleeDefenseBonus > meleeBonus) {
                    meleeBonus = item.meleeDefenseBonus;
                }
                if (item.shield) {
                    hasShield = true;
                }
                if (!item.shield && !item.name == "Power Armor") {
                    hasArmor = true;
                }
            });

            if (hasArmor && hasShield) {
                meleeBonus++;
            }

            return meleeBonus + this.$store.state.currentUnit.defense;
        },
        calculatedStrength() {
            let inventory = this.$store.state.currentUnit.inventory;
            inventory = inventory.filter((item) => item.equipped && item.name == "Power Armor");

            let strength = this.$store.state.currentUnit.strength;
            if (inventory.length > 0) {
                strength += 2;
            }
            return strength;
        }
    }
}

</script>

<style>
section#unit-stats {
    border: solid var(--thick-border) var(--border-color);
    border-radius: var(--border-radius-card);
    margin: var(--wide-padding);
}

div.stat-container>div {
    display: flex;
    flex-direction: column;

    >div {
        flex-grow: 1;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    &:last-child>p {
        border-right: none;
    }
}

p.stat-label {
    margin: 0px;
    padding: var(--wide-padding) var(--standard-padding);
    font-weight: bold;
    border: solid var(--thin-border) var(--border-color);
    border-left: none;
    border-bottom: none;
    background-color: var(--standard-medium);
    width: 100%;
}

p.stat-text {
    margin: 0px;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: var(--wide-padding) var(--standard-padding);
    border-right: dotted var(--thin-border) var(--border-color);
}

div.stat-text p {
    margin: 0px;
    width: 100%;
    padding: var(--wide-padding) var(--standard-padding);
}

p.stat-text.stat-label {
    font-weight: normal;
}

@media only screen and (max-width: 375px) {
    p.stat-label{
        word-wrap: break-word;
        word-break: break-all;

        span.hide-for-acronym{
            display: none;
        }
    }
}

div.display-split-stat {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;

    div p {
        background-color: var(--section-background);
        border: none;
        margin: 0px;
        padding: 0px var(--standard-padding);
    }
}

div#basics-grid {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;
    grid-template-areas: 'title rank type bs-cost';
    text-align: center;

    & div#title {
        grid-area: title;

        &>p.stat-label {
            border-radius: var(--border-radius-card-title) 0px 0px 0px;
        }
    }

    & div#rank {
        grid-area: rank;
    }

    & div#type {
        grid-area: type;
    }

    & div#bs-cost {
        grid-area: bs-cost;

        &>p.stat-label {
            border-radius: 0px var(--border-radius-card-title) 0px 0px;
        }
    }
}

div#big-stats {
    display: grid;
    grid-template-columns: 1fr 1fr;
    grid-template-areas: 'mettle wounds';
    text-align: center;

    & div#mettle {
        grid-area: mettle;
    }

    & div#wounds {
        grid-area: wounds;
    }
}

div#other-stats {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
    grid-template-areas: 'move melee ranged strength defense';
    text-align: center;

    & div#move {
        grid-area: move;
    }

    & div#melee {
        grid-area: melee;
    }

    & div#ranged {
        grid-area: ranged;
    }

    & div#strength {
        grid-area: strength;
    }

    & div#defense {
        grid-area: defense;
    }
}
</style>