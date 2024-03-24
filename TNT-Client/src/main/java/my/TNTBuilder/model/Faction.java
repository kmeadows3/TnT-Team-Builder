package my.TNTBuilder.model;

import java.util.Objects;

public class Faction {
    private int factionId;
    private String factionName;

    public int getFactionId() {
        return factionId;
    }

    public void setFactionId(int factionId) {
        this.factionId = factionId;
    }


    public Faction(int factionId, String factionName) {
        this.factionId = factionId;
        this.factionName = factionName;
    }

    public Faction() {
    }

    public String getFactionName() {
        return factionName;
    }

    public void setFactionName(String factionName) {
        this.factionName = factionName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faction that = (Faction) o;
        return factionId == that.factionId && Objects.equals(factionName, that.factionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(factionId, factionName);
    }
}
