package my.TNTBuilder.service;

import my.TNTBuilder.dao.ItemDao;
import my.TNTBuilder.dao.TeamDao;
import my.TNTBuilder.dao.UnitDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemService {

    private final ItemDao itemDao;
    private final UnitDao unitDao;

    private final TeamService teamService;

    public ItemService(ItemDao itemDao, UnitDao unitDao, TeamService teamService){
        this.itemDao = itemDao;
        this.unitDao = unitDao;
        this.teamService = teamService;
    }

    public List<Item> getItemsForPurchase(){
        List<Item> purchaseList;
        try {
            purchaseList = itemDao.getListOfItemsForPurchase();
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }

        return purchaseList;
    }


    public int addItemToUnit(int itemReferenceId, int unitId, int userId, boolean isFree){

        Item referenceItem = null;
        Unit unit = null;
        try {
            referenceItem = itemDao.lookupReferenceItem(itemReferenceId);
            unit = unitDao.getUnitById(unitId, userId);

        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }


        //TODO logic to validate purchase

        int itemId = 0;
        if (isFree){
            itemId = gainItemForFree(referenceItem, unit);
        } else {
            itemId = purchaseItem(referenceItem, unit);
        }
        return itemId;
    }

    public int addItemToTeam(int itemReferenceId, int teamId, int userId, boolean isFree){

        Item referenceItem = null;
        Team team = null;
        try {
            referenceItem = itemDao.lookupReferenceItem(itemReferenceId);
            team = teamService.getTeamById(teamId, userId);

        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }


        //TODO logic to validate purchase

        int itemId = 0;
        if (isFree){
            itemId = gainItemForFree(referenceItem, team);
        } else {
            itemId = purchaseItem(referenceItem, team);
        }
        return itemId;
    }

    public void transferItem(int itemId, int unitId, int userId){
        Team team = teamService.getTeamByUnitId(unitId);
        if (team.getUserId() == userId){
            try {
                itemDao.transferItem(itemId, unitId, team.getId());
            } catch (DaoException e){
                throw new ServiceException(e.getMessage(), e);
            }

        } else {
            throw new ServiceException("Unit does not belong to user.");
        }

    }

    public void deleteItem(int itemId, int userId) {
        try {
            Team team = teamService.getTeamById(itemDao.getTeamIdByItemId(itemId), userId);
            deleteItemFromDatabase(itemId, userId, team);
        }catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void sellItem(int itemId, int userId){
        try {
            Team team = teamService.getTeamById(itemDao.getTeamIdByItemId(itemId), userId);
            updateTeamMoneyFromSellingItem(itemId, userId, team);
            deleteItemFromDatabase(itemId, userId, team);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }



    /*
    PRIVATE METHODS
     */
    private void updateTeamMoneyFromSellingItem(int itemId, int userId, Team team) {
        Item item = itemDao.getItemById(itemId);
        if (item.getCost()/2 != 0){
            team.setMoney(team.getMoney() + (item.getCost()/2) );
            teamService.updateTeam(team, userId);
        }
    }

    private void deleteItemFromDatabase(int itemId, int userId, Team team) throws DaoException {

        if (team.getUserId() == userId) {
            itemDao.deleteItem(itemId);
        } else {
            throw new ServiceException("User does not own item.");
        }

    }

    private int purchaseItem(Item referenceItem, Unit unit) {
        int itemId = 0;
        try {
            teamService.spendMoney( referenceItem.calculateCostToPurchase(unit), teamService.getTeamByUnitId(unit.getId()));
            itemId = itemDao.addItemToUnit(referenceItem.getReferenceId(), unit.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return itemId;
    }

    private int purchaseItem(Item referenceItem, Team team) {
        int itemId = 0;
        try {
            teamService.spendMoney(referenceItem.getCost(), team);
            itemId = itemDao.addItemToTeam(referenceItem.getReferenceId(), team.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return itemId;
    }

    private int gainItemForFree(Item referenceItem, Unit unit) {
        int itemId = 0;
        try {
            itemId = itemDao.addItemToUnit(referenceItem.getReferenceId(), unit.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return itemId;
    }

    private int gainItemForFree(Item referenceItem, Team team) {
        int itemId = 0;
        try {
            itemId = itemDao.addItemToTeam(referenceItem.getReferenceId(), team.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return itemId;
    }


}
