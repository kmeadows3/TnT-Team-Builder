package my.TNTBuilder.model;

import java.util.Objects;

public class UnitTrait {
    //Instance variables
    private String name;
    private String description;
    private int id;

    public UnitTrait(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public UnitTrait() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitTrait unitTrait = (UnitTrait) o;
        return id == unitTrait.id && name.equals(unitTrait.name) && description.equals(unitTrait.description);
    }

    public int hashCode() {
        return Objects.hash(name, description, id);
    }
}