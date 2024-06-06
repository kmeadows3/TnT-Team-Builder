package my.TNTBuilder.dao;

import my.TNTBuilder.model.inventory.Item;

import java.util.List;

public interface ItemDao {


    /**
     * Adds an item to the team-item join table
     * @param itemRefId the id of the item in the reference table
     */
    int addItemToTeam(int itemRefId, int teamId);

    List<Item> getAllItemsForUnit(int unitId);

    List<Item> getAllItemsForTeam(int teamId);

    int addItemToUnit(int itemRefId, int unitId);

    void transferItem(int itemId, int unitId, int teamId);

    void deleteItem(int itemId);

    Item lookupReferenceItem(int itemRefId);

    List<Item> getListOfItemsForPurchase();

    Item getItemById(int itemId);

    int getTeamIdByItemId(int itemId);

}
