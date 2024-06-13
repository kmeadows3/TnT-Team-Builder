<template>
    <div class="stat-container">
        <div class="stat-box">
            <p class="stat-label">METTLE</p>
            <p class="stat-text">{{ $store.state.currentUnit.mettle }}</p>
        </div>
        <div class="stat-box">
            <p class="stat-label">WOUNDS</p>
            <p class="stat-text">{{ $store.state.currentUnit.wounds }}</p>
        </div>
    </div>
    <div class="stat-container">
        <div class="stat-box">
            <p class="stat-label">MOVE</p>
            <div>
                <p class="stat-text">{{ calculatedMove }}{{ calculatedMove == $store.state.currentUnit.move ? "" : " (" +
                    $store.state.currentUnit.move + ")" }}</p>
            </div>
        </div>
        <div class="stat-box">
            <p class="stat-label">MELEE</p>
            <div v-show="!hasShield">
                <p class="stat-text">{{ $store.state.currentUnit.melee }}</p>
            </div>
            <div v-show="hasShield" class="display-split-stat">
                <div>
                    <p class="stat-text stat-label two-rows">Attacking</p>
                    <p class="stat-text two-rows">{{ $store.state.currentUnit.melee }}</p>
                </div>
                <div>
                    <p class="stat-text stat-label two-rows">Defending</p>
                    <p class="stat-text two-rows" two-rows>{{ ($store.state.currentUnit.melee + 1) + " (" + $store.state.currentUnit.melee
                        + ")"}}</p>
                </div>
            </div>

        </div>
        <div class="stat-box">
            <p class="stat-label">RANGED</p>
            <div>
                <p class="stat-text">{{ $store.state.currentUnit.ranged }}</p>
            </div>
        </div>
        <div class="stat-box">
            <p class="stat-label">STRENGTH</p>
            <div>
                <p class="stat-text">{{ calculatedStrength }}{{ calculatedStrength == $store.state.currentUnit.strength ? ""
                    : " (" + $store.state.currentUnit.strength + ")"}}</p>
            </div>
        </div>
        <div class="stat-box">
            <p class="stat-label">DEFENSE</p>
            <div v-show="showRegularDefense">
                <p class="stat-text" v-show="!showUnarmoredDefense">{{ $store.state.currentUnit.defense }}</p>
                <p class="stat-text" v-show="showUnarmoredDefense">{{ calculatedRangedDefense + " (" +
                    $store.state.currentUnit.defense + ")" }}</p>
            </div>
            <div v-show="!showRegularDefense" class="display-split-stat">
                <div>
                    <p class="stat-text stat-label two-rows">Melee</p>
                    <p class="stat-text two-rows">{{ calculatedMeleeDefense + " (" + $store.state.currentUnit.defense + ")" }}</p>
                </div>
                <div>
                    <p class="stat-text stat-label two-rows">Ranged</p>
                    <p class="stat-text two-rows">{{ calculatedRangedDefense + " (" + $store.state.currentUnit.defense + ")" }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    computed: {
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

            return this.$store.state.currentUnit.move - inventory.length;
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

<style scoped>
div.stat-container {
    display: flex;
    text-align: center;
}

div.stat-box {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    flex-basis: 20%;
    border: solid 3px black;
    border-radius: 7px;
    margin: 3px;
}

div.stat-box>div{
    flex-grow: 1;
    display: flex;
    justify-content: center;
    align-items: center;
}

div.stat-box>div.display-split-stat {
    gap: 10px;
}

p.stat-label {
    font-weight: bold;
    margin: 10px 0px 0px 0px;
    border-bottom: solid 1px black;
}

p.stat-text {
    margin: 12px 0px;
}

p.stat-text.two-rows{
    margin: 5px 0px;
}

p.stat-text.stat-label {
    font-weight: normal;
    border-bottom: solid 1px black;
}
</style>