<template>
    <div class="unit-preview-box">
        <div class="basic-box title">
            <div class="class">Title</div>
            <div>Type</div>
            <div>Defense</div>
            <div>Wounds</div>
        </div>
        <div class="basic-box">
            <div class="class">{{ previewUnit.unitClass }}</div>
            <div>{{ previewUnit.species }}</div>
            <div>{{ previewUnit.defense }}</div>
            <div>{{ previewUnit.wounds }}</div>
        </div>
        <div class="basic-box title">
            <div>Move</div>
            <div>Melee</div>
            <div>Ranged</div>
            <div>Strength</div>
            <div>Mettle</div>
        </div>
        <div class="basic-box">
            <div>{{ previewUnit.move }}</div>
            <div>{{ previewUnit.melee }}</div>
            <div>{{ previewUnit.ranged }}</div>
            <div>{{ previewUnit.strength }}</div>
            <div>{{ previewUnit.mettle }}</div>
        </div>
        <div class="bigger-box">
            <div class="title">Skillsets</div>
            <div class="content">
                <span v-for="(skillset, index) in onlySkillsets" :key="'skillset-id-' + skillset.id">
                    {{ skillset.name }}{{ index == onlySkillsets.length - 1 ? '' : ',&nbsp;' }}
                </span>
            </div>
        </div>
        <div class="bigger-box">
            <div class="title">Special Ablities</div>
            <div class="content">
                <template v-for="(skill, index) in previewUnit.skills" :key="'skill-id-'+skill.id">
                    {{ skill.skillsetId != 16 ? skill.name : skill.description }}{{ index == previewUnit.skills.length -
                        1
                        ? '' : ',&nbsp;' }}
                </template>
            </div>
        </div>
        <div class="bigger-box">
            <div class="title">Starting Skills</div>
            <div class="content">
                {{ previewUnit.emptySkills }}
            </div>
        </div>
        <div class="bigger-box" v-if="previewUnit.specialRules != ''">
            <div class="title">Purchase Note</div>
            <div class="content">
                {{ previewUnit.specialRules }}
            </div>
        </div>
    </div>

</template>

<script>

export default {
    props: ['preview-unit'],
    computed: {
        onlySkillsets() {
            return this.previewUnit.availableSkillsets.filter(skillset => skillset.category == "Skill");
        }
    }
}

</script>


<style scoped>
div.unit-preview-box {
    max-width: 50vw;
    min-width: 50vw;
    border: solid var(--thick-border) var(--border-color);
    border-radius: var(--border-radius-card);

    >div:first-child{
        border-radius: var(--border-radius-card-title) var(--border-radius-card-title) 0 0;
    }

    >div:last-child{
        >div:first-child {
            border-radius: 0 0 0 var(--border-radius-card-title);
        }
    }
    
}

div.basic-box {
    width: 100%;
    display: flex;

    &.title {
        border-top: solid var(--thin-border) var(--border-color);
        background-color: var(--standard-medium);
    }

    &.title:first-child {
        border-top: none;
    }
}


div.basic-box>div {
    display: flex;
    flex-basis: 20%;
    align-items: center;
    justify-content: center;

    border-left: solid var(--thin-border) var(--border-color);

    &:first-child {
        border-left: none;
    }


}

div.basic-box>div.class {
    flex-basis: 40%;
}

div.bigger-box {
    display: flex;
    border-top: solid var(--thin-border) var(--border-color);

    &:last-child {
        border-bottom: none;
    }
}

div.title {
    font-weight: bold;
}

div.bigger-box>div {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    padding: 0px var(--standard-padding);

    &.title {
        flex-basis: 25%;
        background-color: var(--standard-medium);
    }

    &.content {
        flex-basis: 75%;
        max-width: 75%;
        text-wrap: wrap;
        &>span {
            padding: 0px;
        }
    }
}
</style>