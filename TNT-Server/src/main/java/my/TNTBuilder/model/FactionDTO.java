package my.TNTBuilder.model;

import java.util.Objects;

public class FactionDTO {
    private int factionId;
    private String factionName;

    public int getFactionId() {
        return factionId;
    }

    public void setFactionId(int factionId) {
        this.factionId = factionId;
    }


    public FactionDTO(int factionId, String factionName) {
        this.factionId = factionId;
        this.factionName = factionName;
    }

    public FactionDTO() {
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
        FactionDTO that = (FactionDTO) o;
        return factionId == that.factionId && Objects.equals(factionName, that.factionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(factionId, factionName);
    }
}
