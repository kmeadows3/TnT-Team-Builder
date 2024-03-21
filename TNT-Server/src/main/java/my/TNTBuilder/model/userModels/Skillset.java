package my.TNTBuilder.model.userModels;

import java.util.Objects;

public class Skillset {

    private int id;
    private String name;
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skillset skillset = (Skillset) o;
        return id == skillset.id && name.equals(skillset.name) && category.equals(skillset.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category);
    }

}