<template>
    <section class="reference-container">
        <div>
            <h1 class="title">{{ title }}</h1>
            <div class="reference">
                <div v-for="item in content" :key="'ref-item-' + item.name + '-' + item.id">
                    <h2 class="reference-label">
                        {{ item.name }}
                        <span>{{ item.addedString ? '(From ' + item.addedString + ')' : ''}}</span>
                    </h2>
                    <p class="reference-desc">
                        {{ item.description ? item.description : item.effect }}
                    </p>
                </div>
            </div>
        </div>

    </section>
</template>


<script>
export default {
    props: ['title', 'content'],
}
</script>


<style scoped>
section.reference-container {
    padding: var(--wide-padding);

    >div {
        border: solid var(--thick-border) var(--border-color);
        border-radius: var(--border-radius-card);

        >h1.title {
            background-color: var(--standard-medium);
            border-radius: var(--border-radius-card-title) var(--border-radius-card-title) 0 0;
            margin: 0px;
            padding: var(--standard-padding);
            font-size: 1.4em;
            text-align: center;
        }
    }

}



div.reference>div {
    display: grid;
    gap: var(--wide-padding);

    grid-template-columns: 1fr 8fr;
    grid-template-areas: 'name description';

    text-align: left;
    padding: 0px;
    border-bottom: dotted 1px black;

    &:first-child {
        padding-top: var(--standard-padding);
    }

    &:last-child {
        border-bottom: 0px;
    }
}

div.reference h2.reference-label {
    grid-area: name;

    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    margin: 0px;
    padding: var(--standard-padding);
    text-align: center;
    word-wrap: normal;
    font-size: 1.1em;

    & span {
        font-size: .8rem;
        font-weight: normal;
    }


}

div.reference p.reference-desc {
    grid-area: description;
    margin: auto 0px;
}



@media only screen and (max-width: 768px) {

    div.reference {
        >div {
            grid-template-columns: 1fr 5fr;
        }

        h2.reference-label {
            font-size: 1.1em;
        }
    }

}


@media only screen and (max-width: 600px) {
    div.reference {
        >div {
            grid-template-columns: 1fr 3fr;
        }

        h2.reference-label {
            font-size: 1em;
        }

        p {
            font-size: 1em;
        }
    }
}
</style>