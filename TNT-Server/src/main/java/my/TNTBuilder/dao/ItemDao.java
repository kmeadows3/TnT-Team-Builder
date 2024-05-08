package my.TNTBuilder.dao;

import my.TNTBuilder.model.inventory.Item;

import java.util.List;

public interface ItemDao {


    /**
     * Adds an item to the team-item join table
     * @param itemRefId the id of the item in the reference table
     */
    void purchaseItemForTeam(int itemRefId, int teamId);

    List<Item> getAllItemsForUnit(int unitId);

    List<Item> getAllItemsForTeam(int teamId);

}
