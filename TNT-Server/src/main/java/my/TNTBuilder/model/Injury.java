package my.TNTBuilder.model;

import my.TNTBuilder.exception.ValidationException;

import java.util.Objects;

public class Injury {
    private int id;
    private String name;
    private String description;
    private boolean isStatDamage;
    private String statDamaged;
    private boolean isRemovable;
    private boolean isStackable;
    private int count = 1;

    private int grants = 0;

    //CONSTRUCTOR

    public Injury(int id, String name, String description, boolean isStatDamage, String statDamaged, boolean isRemovable, boolean isStackable, int count, int grants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isStatDamage = isStatDamage;
        this.statDamaged = statDamaged;
        this.isRemovable = isRemovable;
        this.isStackable = isStackable;
        this.count = count;
        this.grants = grants;
    }

    public Injury() {}


    //GETTERS AND SETTERS

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatDamage() {
        return isStatDamage;
    }

    public void setStatDamage(boolean statDamage) {
        isStatDamage = statDamage;
    }

    public String getStatDamaged() {
        return statDamaged;
    }

    public void setStatDamaged(String statDamaged) {
        this.statDamaged = statDamaged;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    public void setRemovable(boolean removable) {
        isRemovable = removable;
    }

    public boolean isStackable() {
        return isStackable;
    }

    public void setStackable(boolean stackable) {
        isStackable = stackable;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) throws ValidationException{
        if (count < this.count){
            throw new ValidationException("This injury cannot be healed, it can only grow worse");
        } else if (count > 1 && !this.isStackable){
            throw new ValidationException("Injury cannot be gained more than once");
        } else {
            this.count = count;
        }
    }

    public void setCount(int count, boolean skipVerificaiton){
        if (skipVerificaiton){
            this.count = count;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Injury injury = (Injury) o;
        return id == injury.id && isStatDamage == injury.isStatDamage && isRemovable == injury.isRemovable
                && isStackable == injury.isStackable && count == injury.count && grants == injury.grants
                && Objects.equals(name, injury.name)
                && Objects.equals(description, injury.description) && Objects.equals(statDamaged, injury.statDamaged);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isStatDamage, statDamaged, isRemovable, isStackable, count, grants);
    }
}
