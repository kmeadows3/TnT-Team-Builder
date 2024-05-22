package my.TNTBuilder.service;

import my.TNTBuilder.dao.ItemDao;
import my.TNTBuilder.dao.UnitDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemService {

    ItemDao itemDao;
    UnitDao unitDao;

    TeamService teamService;

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


    public int purchaseItemForUnit(int itemReferenceId, int unitId, int userId){

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
        try {

            teamService.spendMoney( referenceItem.calculateCostToPurchase(unit), teamService.getTeamByUnitId(unitId));

            itemId = itemDao.addItemToUnit(referenceItem.getReferenceId(), unit.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return itemId;
    }

}
