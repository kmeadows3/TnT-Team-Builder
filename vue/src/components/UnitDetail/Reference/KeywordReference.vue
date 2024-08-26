<template>
    <section id="keyword-reference">
        <h1 class="section-title">Keyword Reference</h1>

        <ReferenceList title="Skills, Mutations, and Special Rules" :content="$store.state.unitSkillsSorted" 
            v-show="$store.state.unitSkillsSorted.length > 0"/>
        <ReferenceList title="Injuries" :content="$store.state.unitInjuriesSorted"
            v-show="$store.state.unitInjuriesSorted.length > 0"/>
        <ReferenceList title="Item Keywords" :content="$store.state.unitInventoryTraits" 
            v-show="$store.state.unitInventoryTraits.length > 0" />

    </section>
</template>

<script>
import ReferenceList from './ReferenceList.vue';

export default {
    components: {
        ReferenceList
    }
}
</script>

<style scoped>

h1.section-title {
    border-bottom: solid var(--thick-border) var(--border-color);
}

</style>