package my.TNTBuilder.model;

import java.util.Objects;

public class Skill {
    //Instance variables
    private String name;
    private String description;
    private int id;
    private int skillsetId;
    private String skillsetName;
    private String phase;
    private int cost;
    private int count;

    public Skill(int id, String name, String description, int skillsetId, String skillsetName, String phase, int cost, int count){
        this.id = id;
        this.name = name;
        this.description = description;
        this.skillsetId = skillsetId;
        this.skillsetName = skillsetName;
        this.phase = phase;
        this.cost = cost;
        this.count = count;
    }

    public Skill() {
    }

    public boolean isMutation() {
        return this.skillsetId > 8 && this.skillsetId <=14;
    }

    public boolean isDetriment() {
        return this.skillsetId == 13 || this.skillsetId ==14;
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

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return id == skill.id && cost == skill.cost && name.equals(skill.name)
                && description.equals(skill.description) && phase.equals(skill.phase);
    }

    public int hashCode() {
        return Objects.hash(name, description, id, phase, cost);
    }


}