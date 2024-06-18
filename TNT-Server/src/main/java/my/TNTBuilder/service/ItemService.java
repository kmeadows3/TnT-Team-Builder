package my.TNTBuilder.service;

import my.TNTBuilder.dao.ItemDao;
import my.TNTBuilder.dao.UnitDao;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.exception.ValidationException;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemService {

    private final ItemDao itemDao;
    private final UnitDao unitDao;
    private final TeamService teamService;

    public ItemService(ItemDao itemDao, UnitDao unitDao, TeamService teamService) {
        this.itemDao = itemDao;
        this.unitDao = unitDao;
        this.teamService = teamService;
    }

    public List<Item> getItemsForPurchase()  throws ServiceException{
        List<Item> purchaseList;
        try {
            purchaseList = itemDao.getListOfItemsForPurchase();
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }

        return purchaseList;
    }

    public int addItemToUnit(int itemReferenceId, int unitId, int userId, boolean isFree) throws ServiceException{

        Item referenceItem = null;
        Unit unit = null;
        try {
            referenceItem = itemDao.lookupReferenceItem(itemReferenceId);
            unit = unitDao.getUnitById(unitId, userId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }

        validateUnitCanHaveItem(referenceItem, unit);

        int itemId = 0;
        if (isFree){
            itemId = gainItemForFree(referenceItem, unit);
        } else {
            itemId = purchaseItem(referenceItem, unit);
        }
        return itemId;
    }

    public int addItemToTeam(int itemReferenceId, int teamId, int userId, boolean isFree) throws ServiceException{

        Item referenceItem = null;
        Team team = null;
        try {
            referenceItem = itemDao.lookupReferenceItem(itemReferenceId);
            team = teamService.getTeamById(teamId, userId);

        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }

        if (referenceItem.isRelic()){
            validateTeamCanHaveRelic(referenceItem, team);
        }

        int itemId = 0;
        if (isFree){
            itemId = gainItemForFree(referenceItem, team);
        } else {
            itemId = purchaseItem(referenceItem, team);
        }
        return itemId;
    }

    public void transferItem(int itemId, int unitId, int userId) throws ServiceException{
        Team team = teamService.getTeamByUnitId(unitId);
        if (team.getUserId() == userId){
            try {
                boolean teamToUnit = itemDao.isItemOwnedByTeam(itemId, team.getId(), unitId);

                if (teamToUnit){
                    validateUnitCanHaveItem(itemDao.getItemById(itemId), unitDao.getUnitById(unitId, userId));
                } else {
                    unequipItem(itemId, unitId, userId);
                }

                itemDao.transferItem(itemId, unitId, team.getId(), teamToUnit);
            } catch (DaoException e){
                throw new ServiceException(e.getMessage(), e);
            }

        } else {
            throw new ServiceException("Unit does not belong to user.");
        }

    }

    public void deleteItem(int itemId, int userId)  throws ServiceException{
        try {
            Team team = teamService.getTeamById(itemDao.getTeamIdByItemId(itemId), userId);
            deleteItemFromDatabase(itemId, userId, team);
        }catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void sellItem(int itemId, int userId) throws ServiceException{
        try {
            Team team = teamService.getTeamById(itemDao.getTeamIdByItemId(itemId), userId);
            updateTeamMoneyFromSellingItem(itemId, userId, team);
            deleteItemFromDatabase(itemId, userId, team);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void toggleEquipItem(int itemId, int unitId, int userId) throws ServiceException{
        Unit unit = null;
        Item item = null;
        try {
            unit = unitDao.getUnitById(unitId, userId);
            item = itemDao.getItemById(itemId);
            if (!unit.getInventory().contains(item)){
                throw new ValidationException("This item is not equipped to the indicated unit.");
            }
            item.setEquippedValidated(!item.isEquipped(), unit);
            itemDao.updateEquipped(item);
        } catch (DaoException|ValidationException e) {
            throw new ServiceException(e.getMessage(), e);
        }

    }

    public void unequipItem(int itemId, int unitId, int userId) throws ServiceException{

        Item item = null;
        Unit unit = null;
        try {
            item = itemDao.getItemById(itemId);
            if (!item.isEquipped()) {
                return;
            }

            unit = unitDao.getUnitById(unitId, userId);

            if (!unit.getInventory().contains(item)){
                throw new ValidationException("This item is not equipped to the indicated unit.");
            }

            item.setEquipped(false);
            itemDao.updateEquipped(item);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

    }






    /*
    PRIVATE METHODS
     */
    private void validateUnitCanHaveItem(Item itemToAdd, Unit unit) throws ServiceException {
        if (itemToAdd.getCategory().equals("Support Weapon")){
            validateUnitCanEquipSupportWeapon(unit);
        }

        if (itemToAdd.isRelic()){
            validateUnitCanHaveRelic(unit, itemToAdd);
        }

        for (Skill skill : unit.getSkills()){
            if (skill.getName().equals("RagTag")){
                int totalEquipmentBS = itemToAdd.getCost();
                for (Item item : unit.getInventory()){
                    totalEquipmentBS += item.getCost();
                }
                if (totalEquipmentBS > 15){
                    throw new ValidationException("Units with RagTag cannot have more than 15BS worth of equipment");
                }
            }
        }
    }

    private void validateUnitCanEquipSupportWeapon(Unit unit) throws ValidationException {
        boolean hasUpArmed = false;
        for (Skill skill : unit.getSkills()){
            if (skill.getName().equals("Up-Armed")) {
                hasUpArmed = true;
                break;
            }
        }
        if (!hasUpArmed){
            throw new ValidationException("Unit must have Up-Armed to equip a support weapon");
        }
    }
    private void validateUnitCanHaveRelic(Unit unit, Item itemToAddToInventory) throws ServiceException {

        if (unit.getRank().equals("Rank and File")){
            throw new ValidationException("Rank and File units cannot carry relics");
        } else if (unit.getInventory().stream().filter(Item::isRelic).count() >=2){
            throw new ValidationException("Unit cannot carry more than 2 relics");
        }

        Team team = teamService.getTeamByUnitId(unit.getId());
        List<Item> relicList = new ArrayList<>();
        team.getUnitList().forEach(teamUnit ->
                teamUnit.getInventory().stream().filter(Item::isRelic).forEach(relicList::add));

        boolean isPreservers = team.getFaction().equals("Preservers");

        if ( relicList.size() >= 3 && !isPreservers || relicList.size() >= 4){
            throw new ValidationException("Team cannot take another relic into battle. Other relics must be sent to " +
                    "the team inventory before this item can be added to this unit.");
        }

        validateTeamCanHaveRelic(itemToAddToInventory, team);
    }

    private void validateTeamCanHaveRelic(Item itemToAddToInventory, Team team) throws ValidationException {
        boolean isPreservers = team.getFaction().equals("Preservers");
        boolean isPowerArmor = itemToAddToInventory.getName().equals("Power Armor");

        List<Item> relicList = new ArrayList<>();
        team.getUnitList().forEach(teamUnit -> {
            teamUnit.getInventory().stream().filter(Item::isRelic).forEach(relicList::add);
        });
        team.getInventory().stream().filter( (Item::isRelic)).forEach(relicList::add);

        for (Item relic : relicList){
            if (relic.getReferenceId() == itemToAddToInventory.getReferenceId() && !(isPreservers && isPowerArmor)) {
                throw new ValidationException("Team may not have copies of the same relic, unless it is Power Armor worn by a Reclaimer.");
            }
        }
    }

    private void updateTeamMoneyFromSellingItem(int itemId, int userId, Team team)  throws ServiceException, DaoException{
        Item item = itemDao.getItemById(itemId);
        if (item.getCost()/2 != 0){
            team.setMoney(team.getMoney() + (item.getCost()/2) );
            teamService.updateTeam(team, userId);
        }
    }

    private void deleteItemFromDatabase(int itemId, int userId, Team team)  throws ServiceException, DaoException {

        if (team.getUserId() == userId) {
            itemDao.deleteItem(itemId);
        } else {
            throw new ServiceException("User does not own item.");
        }

    }

    private int purchaseItem(Item referenceItem, Unit unit)  throws ServiceException {
        int itemId = 0;
        try {
            teamService.spendMoney( referenceItem.calculateCostToPurchase(unit), teamService.getTeamByUnitId(unit.getId()));
            itemId = itemDao.addItemToUnit(referenceItem.getReferenceId(), unit.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return itemId;
    }

    private int purchaseItem(Item referenceItem, Team team)  throws ServiceException{
        int itemId = 0;
        try {
            teamService.spendMoney(referenceItem.getCost(), team);
            itemId = itemDao.addItemToTeam(referenceItem.getReferenceId(), team.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return itemId;
    }

    private int gainItemForFree(Item referenceItem, Unit unit)  throws ServiceException{
        int itemId = 0;
        try {
            itemId = itemDao.addItemToUnit(referenceItem.getReferenceId(), unit.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return itemId;
    }

    private int gainItemForFree(Item referenceItem, Team team) throws ServiceException {
        int itemId = 0;
        try {
            itemId = itemDao.addItemToTeam(referenceItem.getReferenceId(), team.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return itemId;
    }


}
