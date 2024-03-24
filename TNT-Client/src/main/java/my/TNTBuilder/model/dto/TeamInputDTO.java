package my.TNTBuilder.model.dto;

public class TeamInputDTO {
    private String name;
    private int factionId;
    private int money;


    public TeamInputDTO(String name, int factionId, int money){
        this.name = name;
        this.factionId = factionId;
        this.money = money;
    }

    public TeamInputDTO(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFactionId() {
        return factionId;
    }

    public void setFactionId(int factionId) {
        this.factionId = factionId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
