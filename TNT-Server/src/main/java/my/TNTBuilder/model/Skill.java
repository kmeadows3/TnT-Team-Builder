package my.TNTBuilder.model;

import java.util.Objects;

public class Skill {
    //Instance variables
    private String name;
    private String description;
    private int id;
    private int skillsetId;
    private String skillsetName;

    public Skill(int id, String name, String description, int skillsetId, String skillsetName){
        this.id = id;
        this.name = name;
        this.description = description;
        this.skillsetId = skillsetId;
        this.skillsetName = skillsetName;
    }

    public Skill() {
    }

    //Getters and Setters
    public int getSkillsetId() {
        return skillsetId;
    }

    public void setSkillsetId(int skillsetId) {
        this.skillsetId = skillsetId;
    }

    public String getSkillsetName() {
        return skillsetName;
    }

    public void setSkillsetName(String skillsetName) {
        this.skillsetName = skillsetName;
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
        Skill skill = (Skill) o;
        return id == skill.id && name.equals(skill.name) && description.equals(skill.description);
    }

    public int hashCode() {
        return Objects.hash(name, description, id);
    }


}