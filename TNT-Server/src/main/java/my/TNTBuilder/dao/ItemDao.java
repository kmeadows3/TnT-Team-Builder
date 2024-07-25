package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ValidationException;
import my.TNTBuilder.model.inventory.Item;
import my.TNTBuilder.model.inventory.Weapon;

import java.util.List;

public interface ItemDao {


    /**
     * Adds an item to the team-item join table
     * @param itemRefId the id of the item in the reference table
     */
    int addItemToTeam(int itemRefId, int teamId) throws DaoException;

    List<Item> getAllItemsForUnit(int unitId) throws DaoException;

    List<Item> getAllItemsForTeam(int teamId)throws DaoException;

    int addItemToUnit(int itemRefId, int unitId) throws DaoException;

    void transferItem(int itemId, int unitId, int teamId, boolean teamToUnit) throws DaoException;

    void deleteItem(int itemId) throws DaoException;

    Item lookupReferenceItem(int itemRefId) throws DaoException;

    List<Item> getListOfItemsForPurchase() throws DaoException;

    Item getItemById(int itemId) throws DaoException;

    int getTeamIdByItemId(int itemId) throws DaoException;

    void updateEquipped(Item item) throws DaoException;

    boolean isItemOwnedByTeam(int itemId, int teamId, int unitId) throws ValidationException;

    void updateWeaponBonuses(Weapon weapon) throws DaoException;

    int getUnitIdByItemId(int itemId);
}
