<template>
    <section class="subsection">
        <UnitName />
        <UnitStats />
        <UnitInventory />
        <KeywordReference />
    </section>
</template>

<script>
import UnitName from './UnitName.vue';
import UnitStats from './UnitStats/UnitStats.vue';
import UnitInventory from './Inventory/UnitInventory.vue';
import KeywordReference from './Reference/KeywordReference.vue';

export default {
    components: {
        UnitName,
        UnitStats,
        UnitInventory,
        KeywordReference
    }
}
</script>
