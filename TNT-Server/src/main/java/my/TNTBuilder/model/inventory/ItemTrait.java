package my.TNTBuilder.model.inventory;

import java.util.Objects;

public class ItemTrait {
    private int id;
    private String name;
    private String effect;

    public ItemTrait(int id, String name, String effect){
        this.id = id;
        this.name = name;
        this.effect = effect;
    }

    public ItemTrait() {    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemTrait itemTrait = (ItemTrait) o;
        return id == itemTrait.id && name.equals(itemTrait.name) && effect.equals(itemTrait.effect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, effect);
    }
}
