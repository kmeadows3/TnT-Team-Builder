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
import my.TNTBuilder.model.inventory.Weapon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        if (referenceItem.isRelic()){
            validateTeamCanHaveRelic(referenceItem, teamService.getTeamByUnitId(unit.getId()));
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

        if (referenceItem.getName().equals("War Banner")){
            validateTeamDoesNotHaveWarBanner(team);
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
                Unit unit = unitDao.getUnitById(unitId, userId);
                if (teamToUnit){
                    validateUnitCanHaveItem(itemDao.getItemById(itemId), unit);
                } else {
                    validateUnitToTeamTransfer(itemId, unit);
                    unequipItemDuringTransfer(itemId, unitId, userId);
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

            ifPreFallAmmoRemovedUnequipFromWeapons(userId, item, unit);

            item.setEquippedValidated(!item.isEquipped(), unit);
            handleBayonetRules(item, unit);

            if (item.isEquipped() && ( item.getName().equals("Battle Force Field") || item.getName().equals("Power Armor") )){
                boolean hasPowerArmor = unit.getInventory().stream()
                        .filter( inventoryItem -> inventoryItem.isEquipped() && inventoryItem.getName().equals("Power Armor"))
                        .count() > 0;
                boolean hasBattleForceField = unit.getInventory().stream()
                        .filter( inventoryItem -> inventoryItem.isEquipped() && inventoryItem.getName().equals("Battle Force Field"))
                        .count() > 0;
                if ( (item.getName().equals("Battle Force Field") && hasPowerArmor)
                    || (item.getName().equals("Power Armor") && hasBattleForceField)){
                    throw new ValidationException("Unit cannot equip both Power Armor and a Battle Force Field at the same time.");
                }
            }

            itemDao.updateEquipped(item);
        } catch (DaoException|ValidationException e) {
            throw new ServiceException(e.getMessage(), e);
        }

    }

    public void unequipItemDuringTransfer(int itemId, int unitId, int userId) throws ServiceException{

        Item item = null;
        Unit unit = null;
        try {
            item = itemDao.getItemById(itemId);
            if (!item.isEquipped()) {
                return;
            }

            validateAndSetupUnequip(unitId, userId, item);

            item.setEquipped(false);
            itemDao.updateEquipped(item);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

    }

    public void updateWeaponUpgrade(Item item, int userId) throws ServiceException {

        try {
            validateWeaponUpgrade(item, userId);
            spendMoneyForWeaponUpgrade(item, userId);
            itemDao.updateWeaponBonuses((Weapon) item);
        } catch (DaoException e){
            throw new ServiceException(e.getMessage(), e);
        }

    }



    /*
    PRIVATE METHODS
     */
    private void validateUnitToTeamTransfer(int itemId, Unit unit) throws ValidationException {
        if (unit.getRank().equals("Freelancer")){
            Item item = itemDao.getItemById(itemId);
            if ( !item.getCategory().equals("Armor") && !item.getCategory().equals("Equipment")){
                throw new ValidationException("Freelancers cannot transfer weapons.");
            }
        }
    }

    private void validateUnitCanHaveItem(Item itemToAdd, Unit unit) throws ServiceException {
        if (itemToAdd.getCategory().equals("Support Weapon")
                ^ (unit.getUnitClass().equals("Broiler") && itemToAdd.getName().equals("Flamethrower"))){

            validateUnitCanEquipSupportWeapon(unit);
        }

        if (itemToAdd.isRelic()){
            validateUnitCanHaveRelic(unit, itemToAdd);
        }

        if (unit.getUnitClass().equals("Berserker") && itemToAdd.getCategory().equals("Armor")){
            throw new ValidationException("Berserkers cannot wear armor");
        }

        if (itemToAdd.getCategory().equals("Melee Weapon") && ((Weapon)itemToAdd).isMasterwork() ){
            validateNoDoubleMasterwork((Weapon)itemToAdd, unit);
        }

        if (itemToAdd.getName().equals("War Banner")){
            Team team = teamService.getTeamByUnitId(unit.getId());
            validateTeamDoesNotHaveWarBanner(team);
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

    private void validateTeamDoesNotHaveWarBanner(Team team) throws ValidationException {
        for (Item inventoryItem : team.getInventory()){
            if (inventoryItem.getName().equals("War Banner")){
                throw new ValidationException("Team may not have more than one War Banner.");
            }
        }
        for (Unit teamUnit : team.getUnitList()){
            for (Item inventoryItem : teamUnit.getInventory()){
                if (inventoryItem.getName().equals("War Banner")){
                    throw new ValidationException("Team may not have more than one War Banner.");
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
        } else if (unit.getRank().equals("Freelancer")){
            throw new ValidationException("Freelancers may not be given relics");
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
        int sellPrice = item.getCost() / 2;

        if (item.getClass() == Weapon.class && ( ((Weapon)item).isMasterwork() || ((Weapon)item).isLargeCaliber() ) ){
            sellPrice = item.getCost();
        }

        if (sellPrice != 0){
            team.setMoney(team.getMoney() + (sellPrice) );
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

    private void validateWeaponUpgrade(Item item, int userId) throws ServiceException {
        Team team = teamService.getTeamById(itemDao.getTeamIdByItemId(item.getId()), userId);

        if (item.getClass() != Weapon.class) {
            throw new ValidationException("This item cannot have weapon upgrades.");
        }

        Weapon weapon = (Weapon) item;

        validateWeaponCanHaveUpgrade(weapon);
        validateItemNotBeingDowngraded(userId, weapon);
        validateNoDoubleMasterwork(weapon, userId);
        confirmUnitHasPrefallAmmoEquipped(weapon, userId);

    }

    private void validateNoDoubleMasterwork(Weapon weapon, int userId) throws ValidationException {
        if (weapon.isMasterwork()){
            if (itemDao.isItemOwnedByUnit(weapon.getId())){
                Unit unit = unitDao.getUnitById(itemDao.getUnitIdByItemId(weapon.getId()), userId);
                boolean unitHasMasterworkAlready = unit.getInventory().stream()
                        .anyMatch(inventoryItem -> inventoryItem.getCategory().equals("Melee Weapon") && ((Weapon) inventoryItem).isMasterwork());
                if (unitHasMasterworkAlready){
                    throw new ValidationException("Unit may only have one masterwork item.");
                }
            }
        }
    }

    private void validateNoDoubleMasterwork(Weapon weapon, Unit unit) throws ValidationException {
        if (weapon.isMasterwork()){
            boolean unitHasMasterworkAlready = unit.getInventory().stream()
                    .anyMatch(inventoryItem -> inventoryItem.getCategory().equals("Melee Weapon") && ((Weapon) inventoryItem).isMasterwork());
            if (unitHasMasterworkAlready){
                throw new ValidationException("Unit may only have one masterwork item.");
            }
        }
    }

    private void validateWeaponCanHaveUpgrade(Weapon weapon) throws ValidationException {
        if (weapon.isMasterwork() && (!weapon.getCategory().equals("Melee Weapon") || weapon.isRelic())){
            throw new ValidationException("Only melee weapons that are not relics may have Masterwork");
        } else if (weapon.isLargeCaliber() && (!weapon.getCategory().equals("Ranged Weapon") || weapon.isRelic())){
            throw new ValidationException("Only ranged weapons that are not relics may have large caliber");
        } else if (weapon.isHasPrefallAmmo() && (!weapon.getCategory().equals("Ranged Weapon") || weapon.isRelic()))  {
            throw new ValidationException("Only firearms that are not relics may be equipped with Pre-Fall Ammo.");
        }
    }

    private void validateItemNotBeingDowngraded(int userId, Weapon weapon) throws ServiceException {
        Weapon originalItem = (Weapon)itemDao.getItemById(weapon.getId());

        if ( !weapon.isLargeCaliber() && originalItem.isLargeCaliber() ) {
            throw new ValidationException("Weapon cannot be downgraded after being upgraded to Large Caliber.");
        } else if (!weapon.isMasterwork() && originalItem.isMasterwork()) {
            throw new ValidationException("Weapon cannot be downgraded after being upgraded to Masterwork.");
        } else if (weapon.equals(originalItem)) {
            throw new ValidationException("Error: Weapon already has this upgrade.");
        }
    }

    private void validateAndSetupUnequip(int unitId, int userId, Item item) throws ServiceException {
        Unit unit;
        unit = unitDao.getUnitById(unitId, userId);
        if (!unit.getInventory().contains(item)){
            throw new ValidationException("This item is not equipped to the indicated unit.");
        }

        ifPreFallAmmoRemovedUnequipFromWeapons(userId, item, unit);

        if (item.getClass() == Weapon.class && ((Weapon) item).isHasPrefallAmmo()) {
            ((Weapon) item).setHasPrefallAmmo(false);
            itemDao.updateWeaponBonuses((Weapon) item);
        }
    }

    private void confirmUnitHasPrefallAmmoEquipped(Weapon weapon, int userId) throws ValidationException {
        if (weapon.isHasPrefallAmmo()) {

            Unit unit = unitDao.getUnitById(itemDao.getUnitIdByItemId(weapon.getId()), userId);
            boolean unitHasPreFallAmmo = false;
            for (Item inventoryItem : unit.getInventory()) {
                if (inventoryItem.getName().equals("Pre-Fall Ammo") && inventoryItem.isEquipped()) {
                    unitHasPreFallAmmo = true;
                }
                if (inventoryItem.getClass() == Weapon.class && ((Weapon) inventoryItem).isHasPrefallAmmo()) {
                    throw new ValidationException("Weapon in inventory already is equipped with the Pre-Fall Ammo.");
                }
            }
            if (!unitHasPreFallAmmo) {
                throw new ValidationException("Unit must be carrying Pre-Fall Ammo to attach it to weapon.");
            }
        }
    }


    private void ifPreFallAmmoRemovedUnequipFromWeapons(int userId, Item item, Unit unit) throws ServiceException {
        if (item.getName().equals("Pre-Fall Ammo") && item.isEquipped()) {
            for (Item inventoryItem : unit.getInventory()) {
                if (inventoryItem.getClass() == Weapon.class && ((Weapon) inventoryItem).isHasPrefallAmmo()) {
                    ((Weapon) inventoryItem).setHasPrefallAmmo(false);
                    updateWeaponUpgrade(inventoryItem, userId);
                }
            }
        }
    }

    private void spendMoneyForWeaponUpgrade(Item item, int userId) throws ServiceException {
        Weapon itemAsWeapon = (Weapon) item;
        if (itemAsWeapon.isLargeCaliber() || itemAsWeapon.isMasterwork()){
            Weapon originalItem = (Weapon)itemDao.getItemById(item.getId());

            if (originalItem.isLargeCaliber() != itemAsWeapon.isLargeCaliber()
                    || originalItem.isMasterwork() != itemAsWeapon.isMasterwork()) {
                int teamId = itemDao.getTeamIdByItemId(item.getId());
                teamService.spendMoney(item.getCost(), teamService.getTeamById(teamId, userId));
            }
        }
    }

    private void handleBayonetRules(Item item, Unit unit) throws ValidationException {
        confirmBayonetIsAttachedToLegalWeapon(item, unit);

        if (item.getCategory().equals("Ranged Weapon") && !item.isEquipped()){
            unequipAnyAttachedBayonet(unit);
        }
    }

    private void confirmBayonetIsAttachedToLegalWeapon(Item item, Unit unit) throws ValidationException {
        if(item.isEquipped() &&  item.getName().equals("Bayonet")){
            boolean bayonetIsEquippable = false;
            List<Item> equippedItems = unit.getInventory().stream()
                    .filter( inventoryItem -> inventoryItem.isEquipped()).collect(Collectors.toList());

            for (Item equippedItem : equippedItems) {
                if (equippedItem.getName().equals("Rifle")
                        || equippedItem.getName().equals("Musket")
                        || equippedItem.getName().equals("Shotgun")
                        || equippedItem.getName().equals("Assault Rifle")) {
                    bayonetIsEquippable = true;
                }
            }

            if (!bayonetIsEquippable){
                throw new ValidationException("Bayonets can only be equipped with an Assault Rifle, Musket, Shotgun, or Assault Rifle.");
            }
        }
    }

    private void unequipAnyAttachedBayonet(Unit unit) {
        boolean hasEquippedBayonet = unit.getInventory().stream()
                .anyMatch(inventoryItem -> inventoryItem.isEquipped() && inventoryItem.getName().equals("Bayonet"));

        if ( hasEquippedBayonet ){
            Item bayonet = unit.getInventory().stream()
                    .filter(inventoryItem -> inventoryItem.isEquipped() && inventoryItem.getName().equals("Bayonet"))
                    .findFirst().get();
            bayonet.setEquipped(false);
            itemDao.updateEquipped(bayonet);
        }
    }

}
