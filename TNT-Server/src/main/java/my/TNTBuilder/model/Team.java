package my.TNTBuilder.model;

import my.TNTBuilder.model.inventory.Item;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team {
    private int id;
    private int userId;
    @NotNull(message = "A valid faction_id must be provided.")
    private int factionId;
    private String name = "";
    @NotNull(message = "Barter Scrip may not be null.")
    @Min(value = 0, message = "Barter Script may not fall below zero.")
    private int money;
    private List<Unit> unitList = new ArrayList<>();
    private List<Item> inventory = new ArrayList<>();

    //Constructors
    public Team (){
    }
    public Team (int id, int userId, String name, int factionId, int money,
                 List<Unit> unitList, List<Item> inventory){
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.factionId = factionId;
        this.money = money;
        this.unitList = unitList;
        this.inventory = inventory;
    }

    /*
    Derived instance variables
     */

    public int getBSCost(){
        int bsCost = 0;
        for (Unit unit : unitList){
            bsCost += unit.getBSCost();
        }
        //TODO: add inventory BS cost later
        return bsCost;
    }

    public int relicCount(){
        return 0; // TODO: ADD METHOD
    }

    public int getUpkeep(){
        int upkeep = 0;
        for (Unit unit : unitList){
            upkeep += unit.getUnitUpkeep();
        }
        // TODO: Deal with relics in inventory
        return upkeep;
    }





    /*
    getters and setters
     */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFactionId(int factionId) {
        this.factionId = factionId;
    }

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

    public int getFactionId() {
        return factionId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    /*
    Override Equals
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        boolean unitsEqual= true;
        for(Unit unit : unitList){
            if (!team.getUnitList().contains(unit)){
                unitsEqual = false;
                break;
            }
        }
        boolean inventoryEqual= true;
        for(Item item : inventory){
            if (!team.getInventory().contains(item)){
                inventoryEqual = false;
                break;
            }
        }

        return id == team.id && userId == team.userId && factionId == team.factionId && money == team.money &&
                Objects.equals(name, team.name) && unitsEqual &&
                inventoryEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, factionId, name, money, unitList, inventory);
    }
}
