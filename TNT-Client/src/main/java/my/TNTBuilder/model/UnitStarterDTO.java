package my.TNTBuilder.model;

public class UnitStarterDTO {
    private int id;
    private int teamId;
    private String name = "";


    public UnitStarterDTO(){}

    public UnitStarterDTO(int id, int teamId, String name) {
        this.id = id;
        this.teamId = teamId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
